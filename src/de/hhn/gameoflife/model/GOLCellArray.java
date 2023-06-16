package de.hhn.gameoflife.model;

import java.io.Serializable;

public class GOLCellArray implements Serializable {
    // Contains the state of each cell. One bit per cell
    // Layout: 7 6 5 4 3 2 1 0 | 15 14 13 12 11 10 9 8 | 23 ...
    protected byte[] fieldData;
    protected final int width;
    protected final int height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GOLCellArray(int width, int height) {
        this.width = width;
        this.height = height;
        fieldData = new byte[(width * height >> 3) + Math.min(width * height & 7, 1)];
    }

    /**
     * Checks for index out of Bounds exceptions
     */
    protected void assertBounds(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IndexOutOfBoundsException(
                    x + "|" + y + " out of bounds of this GOLField. Width is " + width + " and height is " + height);
    }


    /**
     * @return true when a given cell is alive
     */
    public boolean getAlive(int x, int y) {
        assertBounds(x, y);
        int bit = y * width + x;
        return (fieldData[bit >> 3] >> (bit & 7) & 1) == 1;
    }

    /**
     * @param bit x + y * width = bit
     * @return true when the specified cell is alive
     */
    public boolean getAlive(int bit) {
        return (fieldData[bit >> 3] >> (bit & 7) & 1) == 1;
    }

    /**
     * Set the state of a cell
     */
    public void setAlive(int x, int y, boolean alive) {
        assertBounds(x, y);
        int bit = y * width + x;
        if (alive)
            fieldData[bit >> 3] |= 1 << (bit & 7);
        else
            fieldData[bit >> 3] &= ~(1 << (bit & 7));
    }


    /**
     * Apply the content of another GOLCellArray inside of this CellArray.
     * @param x X Coordinate of the top left Corner of the Shape
     * @param y Y Coordinate of the top left Corner of the Shape
     */
    public void applyShape(GOLCellArray shape, int x, int y) {
        if (shape.width > width || shape.height > this.height)
            throw new IllegalArgumentException("Shape should not be bigger then the GOLCellArray its applied to");
        for (int i = 0; i < shape.getHeight(); i++) {
            for (int j = 0; j < shape.getWidth(); j++) {
                setAlive(x + j % width, y + i % height, shape.getAlive(j, i));
            }
        }
    }
}
