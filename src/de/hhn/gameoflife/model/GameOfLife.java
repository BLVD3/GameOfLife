package de.hhn.gameoflife.model;

import de.hhn.gameoflife.util.GOLCellChangedListener;

/**
 * Highly optimized Torus shaped Game of Life model
 * @author Nico Vogel 215998
 * @version 1.0
 */
public class GameOfLife {
    // Contains the state of each cell. One bit per cell
    // Layout: 7 6 5 4 3 2 1 0 | 15 14 13 12 11 10 9 8 | 23 ...
    protected byte[] fieldData;
    protected byte[] nextStep;
    protected final byte[] changedBits;
    protected final int width;
    protected final int height;
    // For rule sets 1 means change 0 mean no change
    // Bit 0 no neighbours bit 8 8 neighbours
    // Ruleset for dead cells
    protected final short birthRule;
    // Ruleset for alive cells
    protected final short deathRule;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * @param width Width of this GOL field
     * @param height Height of this GOL field
     * @param birthRule A short describing the behaviour of a dead Cell.
     *                  Example:
     *                  A 1 at Position 3 makes it so a Cell becomes alive with 3 neighbouring alive cells.
     *                  Everything beyond the 9th bit will be ignored.
     * @param deathRule A short describing the behaviour of an alive Cell.
     */
    public GameOfLife(int width, int height, short birthRule, short deathRule) {
        this.width = width;
        this.height = height;
        this.birthRule = birthRule;
        this.deathRule = deathRule;
        fieldData = new byte[(width * height >> 3) + Math.min(width * height & 7, 1)];
        nextStep = new byte[fieldData.length];
        changedBits = new byte[fieldData.length];
    }

    /**
     * @param width Width of this GOL field
     * @param height Height of this GOL field
     */
    public GameOfLife(int width, int height) {
        this(width, height, (short)0b100000, (short)0b110011111);
    }

    protected void assertBounds(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IndexOutOfBoundsException(
                    "Attempted to get Alive state of " + x + ", " + y
                            + " where width is " + width + " and height is " + height);
    }

    /**
     * @return true when a given cell is alive
     */
    public boolean getAlive(int x, int y) {
        assertBounds(x, y);
        int bit = y * width + x;
        return (fieldData[bit >> 3] >> (bit & 7) & 1) == 1;
    }

    public boolean getAlive(int bit) {
        return (fieldData[bit >> 3] >> (bit & 7) & 1) == 1;
    }

    /**
     * Sets the Cell State for the next Step. Should only be called in {@link #step()}.
     */
    protected void setAliveNextStep(int x, int y, boolean alive) {
        //assertBounds(x, y);
        int bit = y * width + x;
        if (alive)
            nextStep[bit >> 3] |= 1 << (bit & 7);
        else
            nextStep[bit >> 3] ^= 1 << (bit & 7);
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
     * @return Count of Alive Neighbours
     */
    public byte getNeighbours(int x, int y) {
        byte neighbourCount = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int targetX;
                int targetY;
                if ((targetX = (x + i)) >= width)
                    targetX -= width;
                if (targetX < 0)
                    targetX += width;
                if ((targetY = (y + j)) >= height)
                    targetY -= height;
                if (targetY < 0)
                    targetY += height;
                if (getAlive(targetX, targetY))
                    neighbourCount++;
            }
        }
        if (getAlive(x, y))
            neighbourCount--;
        return neighbourCount;
    }

    /**
     * Computes a whole step for this instance.
     */
    public void step() {
        System.arraycopy(fieldData, 0, nextStep, 0, fieldData.length);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //For each cell
                byte neighbours = getNeighbours(x, y);
                boolean alive = getAlive(x, y);
                short ruleset = alive ? deathRule : birthRule;
                if ((ruleset >> (8 - neighbours) & 1) == 1) {
                    setAliveNextStep(x, y, !alive);
                }
            }
        }
        for (int i = 0; i < fieldData.length; i++) {
            changedBits[i] = (byte)(nextStep[i] ^ fieldData[i]);
        }
        byte[] temp = fieldData;
        fieldData = nextStep;
        nextStep = temp;
    }

    public void forEachChange(GOLCellChangedListener listener) {
        int x = 0;
        int y = 0;
        for (int i = 0; i < width * height; i++) {
            if ((changedBits[i >> 3] >> (i & 7) & 1) == 1)
                listener.cellChangedEvent(x, y, getAlive(i));
            if (++x == width) {
                x = 0;
                y++;
            }
        }
    }
}
