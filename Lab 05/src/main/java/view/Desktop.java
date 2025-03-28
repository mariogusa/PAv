package view;

import model.ImageItem;
import javax.swing.*;
import java.net.URL;

public class Desktop {
    public static void displayImage(ImageItem image) {
        if (image == null) {
            System.out.println("Cannot display null image");
            return;
        }

        try {
            // Get resource URL properly
            URL imageUrl = Desktop.class.getResource(image.getFilePath());
            if (imageUrl == null) {
                throw new Exception("Image not found at: " + image.getFilePath());
            }

            ImageIcon icon = new ImageIcon(imageUrl);
            JFrame frame = new JFrame(image.getTitle());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JLabel label = new JLabel(icon);
            frame.getContentPane().add(label);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            System.out.println("Displayed: " + image.getTitle());
        } catch (Exception e) {
            System.err.println("ERROR displaying '" + image.getTitle() + "': " + e.getMessage());
        }
    }
}