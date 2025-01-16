package bg.sofia.uni.fmi.mjt.poll.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_HOST = "localhost";
    private static final int BUFFER_SIZE = 512;
    private static final String DISCONNECT_COMMAND = "disconnect";

    private static ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

    public static void sendMessage(String message, SocketChannel socketChannel) throws IOException {
        System.out.println("Sending message <" + message + "> to the server...");

        buffer.clear();
        buffer.put(message.getBytes());
        buffer.flip();
        socketChannel.write(buffer);
    }

    public static void getAnswer(SocketChannel socketChannel) throws IOException {
        buffer.clear();
        socketChannel.read(buffer);
        buffer.flip();

        byte[] byteArray = new byte[buffer.remaining()];
        buffer.get(byteArray);
        String reply = new String(byteArray, StandardCharsets.UTF_8);

        System.out.println("The server replied <" + reply + ">");
    }

    public static void main(String[] args) {

        try (SocketChannel socketChannel = SocketChannel.open();
             Scanner scanner = new Scanner(System.in)) {

            socketChannel.connect(new InetSocketAddress(SERVER_HOST, SERVER_PORT));

            System.out.println("Connected to the server.");

            while (true) {
                System.out.print("Enter message: ");
                String message = scanner.nextLine();

                if (DISCONNECT_COMMAND.equals(message)) {
                    break;
                }

                sendMessage(message, socketChannel);

                getAnswer(socketChannel);
            }

        } catch (IOException e) {
            throw new RuntimeException("There is a problem with the network communication", e);
        }
    }
}