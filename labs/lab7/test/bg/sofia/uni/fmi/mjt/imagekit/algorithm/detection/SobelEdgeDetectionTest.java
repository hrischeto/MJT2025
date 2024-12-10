package bg.sofia.uni.fmi.mjt.imagekit.algorithm.detection;

import bg.sofia.uni.fmi.mjt.imagekit.algorithm.grayscale.LuminosityGrayscale;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SobelEdgeDetectionTest {
    private final LuminosityGrayscale grayscaleAlgorithm = new LuminosityGrayscale();

    private final SobelEdgeDetection edgeDetectionAlgorithm = new SobelEdgeDetection(grayscaleAlgorithm);

    @Test
    void testNullImageToProcess() {
        assertThrows(IllegalArgumentException.class, () -> edgeDetectionAlgorithm.process(null),
            "When given image is null, IllegalArgumentException should be thrown.");
    }
}
