package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LocalFileSystemImageManager implements FileSystemImageManager {
    public LocalFileSystemImageManager() {
    }

    @Override
    public BufferedImage loadImage(File imageFile) throws IOException {
        validateFile(imageFile);

        return ImageIO.read(imageFile);
    }

    private void validateFile(File imageFile) throws IOException {

        if (Objects.isNull(imageFile)) {
            throw new IllegalArgumentException("Image file is null.");
        }

        if (!imageFile.exists()) {
            throw new IOException("File does not exist.");
        }

        Path imagePath = imageFile.toPath();

        if (!Files.isRegularFile(imagePath)) {
            throw new IOException("File is not regular.");
        }

        String extension = getFileExtension(imagePath);
        if (extension.isEmpty() || !SupportedFormats.isSupported(extension)) {
            throw new IOException("File format not supported.");
        }
    }

    private String getFileExtension(Path filePath) {
        if (Objects.isNull(filePath)) {
            throw new IllegalArgumentException("FilePath was null.");
        }

        String fileName = filePath.getFileName().toString();

        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex != -1) {
            return fileName.substring(lastIndex + 1);
        }
        return "";
    }

    @Override
    public List<BufferedImage> loadImagesFromDirectory(File imagesDirectory) throws IOException {
        validateDirectory(imagesDirectory);

        Path directoryPath = imagesDirectory.toPath();
        List<BufferedImage> toReturn = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directoryPath)) {

            for (Path file : stream) {
                BufferedImage image = loadImage(file.toFile());
                toReturn.add(image);
            }
        } catch (IOException | DirectoryIteratorException e) {
            throw new IOException("Error while reading from directory.");
        }

        return toReturn;
    }

    private void validateDirectory(File imageDirectory) throws IOException {
        if (Objects.isNull(imageDirectory)) {
            throw new IllegalArgumentException("Image directory is null.");
        }
        Path directoryPath = imageDirectory.toPath();
        if (!Files.exists(directoryPath)) {
            throw new IOException("Directory does not exist.");
        }
        if (!Files.isDirectory(directoryPath)) {
            throw new IOException("File is not a directory.");
        }
    }

    @Override
    public void saveImage(BufferedImage image, File imageFile) throws IOException {

        if (Objects.isNull(image)) {
            throw new IllegalArgumentException("Image is null.");
        }
        validateSavingLocation(imageFile);

        ImageIO.write(image, getFileExtension(imageFile.toPath()), imageFile);
    }

    private void validateSavingLocation(File imageFile) throws IOException {

        if (Objects.isNull(imageFile)) {
            throw new IllegalArgumentException("File to save the image to is null.");
        }

        Path filePath = imageFile.toPath();

        if (Files.exists(filePath)) {
            throw new IOException("File already exists.");
        }
        if (Objects.nonNull(filePath.getParent()) && Files.notExists(filePath.getParent())) {
            throw new IOException("Parent directory does not exist.");
        }

        String extension = getFileExtension(filePath);
        if (extension.isEmpty() || !SupportedFormats.isSupported(extension)) {
            throw new IOException("File format not supported.");
        }
    }
}
