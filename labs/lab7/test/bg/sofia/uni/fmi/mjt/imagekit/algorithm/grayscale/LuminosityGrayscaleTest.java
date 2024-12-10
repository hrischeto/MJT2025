package bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LuminosityGrayscaleTest {
    private final LuminosityGrayscale grayscale = new LuminosityGrayscale();
    private final double redIntensity = 0.21;
    private final double greenIntensity = 0.72;
    private final double blueIntensity = 0.07;

    private final int oneByte = 255;
    private final int shiftWithOneByte = 8;
    private final int shiftWithTwoBytes = 16;

    @Test
    void testNullImageToProcess() {
        assertThrows(IllegalArgumentException.class, () -> grayscale.process(null),
            "When given image is null, IllegalArgumentException should be thrown.");
    }

    @Test
    void testGrayscaleAPixel() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

        image.setRGB(0, 0, 123);
        int originalPixel = image.getRGB(0, 0);

        int red = (originalPixel >> shiftWithTwoBytes) & oneByte;
        int green = (originalPixel >> shiftWithOneByte) & oneByte;
        int blue = originalPixel & oneByte;

        int resultColorValue = (int) (redIntensity * red + greenIntensity * green + blueIntensity * blue);
        int resultPixel =
            (resultColorValue << shiftWithTwoBytes) | (resultColorValue << shiftWithOneByte) |
                resultColorValue;

        image.setRGB(0, 0, resultPixel);

        assertEquals(image.getRGB(0, 0), grayscale.process(image).getRGB(0, 0),
            "RGB value not correctly evaluated when applying grayscale to a pixel.");
    }
}
