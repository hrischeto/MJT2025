package bg.sofia.uni.fmi.mjt.imagekit.filesystem;

public enum SupportedFormats {
    JPG("jpg"),
    PNG("png"),
    BPM("bpm");

    private final String extension;

    SupportedFormats(String extension){
        this.extension =extension;
    }

    public static boolean isSupported(String extension){
        return extension.equals(JPG.extension)||
            extension.equals(PNG.extension)|| extension.equals(BPM.extension);
    }
}
