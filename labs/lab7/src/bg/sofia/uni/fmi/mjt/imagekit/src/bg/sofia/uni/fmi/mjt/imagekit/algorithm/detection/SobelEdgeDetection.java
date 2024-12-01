package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.ImageAlgorithm;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class SobelEdgeDetection implements EdgeDetectionAlgorithm {
    private final ImageAlgorithm greyscaler;

    private final int oneByte = 255;
    private final int shiftWithOneByte = 8;
    private final int shiftWithTwoBytes = 16;

    private final int[][] verticalKernel = {
        {-1, 0, 1},
        {-2, 0, 2},
        {-1, 0, 1}
    };
    private final int[][] horizontalKernel = {
        {-1, -2, -1},
        {0, 0, 0},
        {1, 2, 1}
    };

    public SobelEdgeDetection(ImageAlgorithm grayscaleAlgorithm) {
        greyscaler = grayscaleAlgorithm;
    }

    @Override
    public BufferedImage process(BufferedImage image) {
        if (Objects.isNull(image)) {
            throw new IllegalArgumentException("Image is null");
        }

        BufferedImage grayscaleImage = greyscaler.process(image);
        int width = grayscaleImage.getWidth();
        int height = grayscaleImage.getHeight();
        BufferedImage edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int sumX = 0;
                int sumY = 0;

                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int gray = (grayscaleImage.getRGB(x + kx, y + ky) & oneByte);
                        sumX += verticalKernel[ky + 1][kx + 1] * gray;
                        sumY += horizontalKernel[ky + 1][kx + 1] * gray;
                    }
                }
                int magnitude = (int) Math.min(oneByte, Math.sqrt(sumX * sumX + sumY * sumY));
                int edgeRgb = (magnitude << shiftWithTwoBytes) | (magnitude << shiftWithOneByte) | magnitude;

                edgeImage.setRGB(x, y, edgeRgb);
            }
        }
        return edgeImage;
    }
}
