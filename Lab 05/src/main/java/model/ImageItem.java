package model;

public class ImageItem {
    private final String imageId;
    private final String title;
    private final String filePath;
    private final long fileSize;
    private final int width;
    private final int height;
    private final String fileFormat;

    public ImageItem(String imageId, String title, String filePath,
                     long fileSize, int width, int height, String fileFormat) {
        if (filePath == null || !filePath.startsWith("/images/")) {
            throw new IllegalArgumentException("Image path must start with /images/");
        }

        this.imageId = imageId;
        this.title = title;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.width = width;
        this.height = height;
        this.fileFormat = fileFormat;
    }

    public String getImageId() { return imageId; }
    public String getTitle() { return title; }
    public String getFilePath() { return filePath; }
    public long getFileSize() { return fileSize; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public String getFileFormat() { return fileFormat; }

    @Override
    public String toString() {
        return title + " (" + width + "x" + height + ") " + fileFormat;
    }
}