package de.hhn.gameoflife.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Class containing Information about a shape
 * @author Nico Vogel
 * @version 1.0
 */
public class GOLShape implements Serializable {
    @Serial
    private static final long serialVersionUID = -831734053756909342L;
    // Dot represents the most Basic shape and can be used instead of "null" as a placeholder
    public static final GOLShape DOT;
    static {
        GOLCellArray cellArray = new GOLCellArray(3, 3);
        cellArray.setAlive(1, 1, true);
        DOT = new GOLShape("Dot", cellArray);
    }
    private final String name;
    private final GOLCellArray shape;
    private transient BufferedImage image;

    /**
     * @return the name of the shape
     */
    public String getName() {
        return name;
    }

    /**
     * @return the shape in Form of a CellArray
     */
    public GOLCellArray getShape() {
        return shape;
    }

    /**
     * Creates an instance of a shape
     * @param name the name of the shape
     * @param shape The object will save a copy of the GOLCellArray
     */
    public GOLShape(String name, GOLCellArray shape) {
        this.name = name;
        this.shape = shape.getCellArray();
    }

    /**
     * @return a preview image of the shape
     */
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
