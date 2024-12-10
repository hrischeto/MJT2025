package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LocalFileSystemImageManagerTest {
    private final LocalFileSystemImageManager fileSystemImageManager = new LocalFileSystemImageManager();
    private static File imageMock;

    @BeforeEach
    public void setUp() {
        imageMock = mock(File.class);
    }

    @Test
    void testLoadingNullImage() {
        assertThrows(IllegalArgumentException.class, () -> fileSystemImageManager.loadImage(null),
            "Attempting to load a null image should throw IllegalArgumentException.");
    }

    @Test
    void testLoadNonExistentImage() {
        when(imageMock.exists()).thenReturn(false);

        assertThrows(IOException.class, () -> fileSystemImageManager.loadImage(imageMock),
            "Attempting to load a nonexistent image should throw IOException.");

        verify(imageMock).exists();
        verify(imageMock, times(1)).exists();
    }

    @Test
    void testLoadNonRegularImage() {
        Path imagePathMock = mock(Path.class);
        when(imageMock.exists()).thenReturn(true);
        when(imageMock.toPath()).thenReturn(imagePathMock);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.isRegularFile(imagePathMock)).thenReturn(false);

            assertThrows(IOException.class, () -> fileSystemImageManager.loadImage(imageMock),
                "Attempting to load a not regular file should throw IOException.");
        }

        verify(imageMock).toPath();
        verify(imageMock, times(1)).toPath();
    }

    @Test
    void testNotSupportedFileFormat() {
        Path imagePathMock = mock(Path.class);
        Path fileNameMock = mock(Path.class);

        when(imageMock.exists()).thenReturn(true);
        when(imageMock.toPath()).thenReturn(imagePathMock);

        when(imagePathMock.getFileName()).thenReturn(fileNameMock);

        when(fileNameMock.toString()).thenReturn("hrisi.java");

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.isRegularFile(imagePathMock)).thenReturn(true);

            assertThrows(IOException.class, () -> fileSystemImageManager.loadImage(imageMock),
                "Attempting to load an unsupported file format should throw IOException.");
        }
    }

    @Test
    void testSavingNullImage() {
        assertThrows(IllegalArgumentException.class, () -> fileSystemImageManager.saveImage(null, null),
            "Attempting to load a null image should throw IllegalArgumentException.");
    }
    
    /*@Test
    void testSavingToNonExistingDirectoryImage() {
        Path filePathMock = mock(Path.class);
        when(imageMock.exists()).thenReturn(true);
        when(imageMock.toPath()).thenReturn(filePathMock);

        Path parentMock=mock(Path.class);
        when(filePathMock.getParent()).thenReturn(parentMock);
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.notExists(parentMock)).thenReturn(true);

            assertThrows(IOException.class, () -> fileSystemImageManager.saveImage(imageMock, ),
                "Attempting to load a not regular file should throw IOException.");
        }

        verify(imageMock).toPath();
        verify(imageMock, times(1)).toPath();
    }

      @Test
    void testLoadAValidImage() {
        Path imagePathMock = mock(Path.class);
        Path fileNameMock = mock(Path.class);

        when(imageMock.exists()).thenReturn(true);
        when(imageMock.toPath()).thenReturn(imagePathMock);

        when(imagePathMock.getFileName()).thenReturn(fileNameMock);

        when(fileNameMock.toString()).thenReturn("hrisi.jpg");

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.isRegularFile(imagePathMock)).thenReturn(true);
        }

        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

        try (MockedStatic<ImageIO> mockedImageIO = mockStatic(ImageIO.class)) {
            mockedImageIO.when(() -> ImageIO.read(imageMock)).thenReturn(true);
        }

    }
     */
}
