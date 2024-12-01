package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class LuminosityGrayscale implements GrayscaleAlgorithm {
    private final double redIntensity = 0.21;
    private final double greenIntensity = 0.72;
    private final double blueIntensity = 0.07;

    private final int oneByte = 255;
    private final int shiftWithOneByte = 8;
    private final int shiftWithTwoBytes = 16;

    public LuminosityGrayscale() {
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (Objects.isNull(image)) {
            throw new IllegalArgumentException("Image is null");
        }

        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int j = 0; j < image.getHeight(); j++) {

            for (int i = 0; i < image.getWidth(); i++) {
                int originalPixel = image.getRGB(i, j);

                int red = (originalPixel >> shiftWithTwoBytes) & oneByte;
                int green = (originalPixel >> shiftWithOneByte) & oneByte;
                int blue = originalPixel & oneByte;

                int resultColorValue = (int) (redIntensity * red + greenIntensity * green + blueIntensity * blue);
                int resultPixel =
                    (resultColorValue << shiftWithTwoBytes) | (resultColorValue << shiftWithOneByte) |
                        resultColorValue;
                result.setRGB(i, j, resultPixel);
            }
        }
        return result;
    }
}