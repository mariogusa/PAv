package model;

import java.util.ArrayList;
import java.util.List;
import view.Desktop;

public class ImageRepository {
    private final List<ImageItem> images = new ArrayList<>();

    public void addImage(ImageItem image) {
        if (image == null) {
            throw new IllegalArgumentException("Image cannot be null");
        }
        images.add(image);
        System.out.println("Added image: " + image.getTitle());
    }

    public void displayAllImages() {
        if (images.isEmpty()) {
            System.out.println("Repository is empty");
            return;
        }

        System.out.println("Displaying " + images.size() + " images...");
        images.forEach(image -> {
            try {
                Desktop.displayImage(image);
            } catch (Exception e) {
                System.err.println("Failed to display " + image.getTitle() + ": ");
            }
        });
    }

    public int getImageCount() {
        return images.size();
    }
}