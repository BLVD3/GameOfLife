package de.hhn.gameoflife.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GOLShape {
    private final String name;
    private final GOLCellArray shape;
    private BufferedImage image;

    public String getName() {
        return name;
    }

    public GOLCellArray getShape() {
        return shape;
    }

    public GOLShape(String name, GOLCellArray shape) {
        this.name = name;
        this.shape = shape;
    }

    public BufferedImage getImage() {
        if (image == null) {
            image = new BufferedImage(shape.getWidth(), shape.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < shape.getHeight(); i++) {
                for (int j = 0; j < shape.getWidth(); j++) {
                    image.setRGB(j, i, shape.getAlive(j, i) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }
        }
        return image;
    }
}
