package org.example;

import model.ImageItem;
import model.ImageRepository;

public class Main {
    public static void main(String[] args) {
        ImageRepository repository = new ImageRepository();

        repository.addImage(new ImageItem(
                "img001",
                "Barcelona City View",
                "/images/Barcelona.jpg",
                1920 * 1080,
                1920,
                1080,
                "JPG"
        ));
        repository.displayAllImages();
    }
}