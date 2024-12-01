package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

import java.util.Objects;

public enum SupportedFormats {
    JPEG("jpg"),
    PNG("png"),
    BPM("bpm");

    private final String extension;

    SupportedFormats(String extension) {
        this.extension = extension;
    }

    public static boolean isSupported(String extension) {
        if (Objects.isNull(extension)) {
            throw new IllegalArgumentException("Extension is null.");
        }

        return extension.equals(JPEG.extension) ||
            extension.equals(PNG.extension) || extension.equals(BPM.extension);
    }
}
