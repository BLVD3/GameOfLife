package de.hhn.gameoflife.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class GOLShape implements Serializable {
    private static long serialVersionUID = 1493152L;
    private final String name;
    private final GOLCellArray shape;
    private transient BufferedImage image;

    public String getName() {
        return name;
    }

    public GOLCellArray getShape() {
        return shape;
    }

    public GOLShape(String name, GOLCellArray shape) {
        this.name = name;
        this.shape = shape.getCellArray();
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

    public boolean save(String path) {
        if (new File(path).exists())
            return false;
        try {
            FileOutputStream fStream = new FileOutputStream(path);
            ObjectOutputStream oStream = new ObjectOutputStream(fStream);
            oStream.writeObject(this);
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }
}
