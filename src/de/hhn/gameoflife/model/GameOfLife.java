package de.hhn.gameoflife.model;

/**
 * Highly optimized Torus shaped Game of Life implementation
 * @author Nico Vogel 215998
 * @version 1.0
 */
public class GameOfLife {
    // Contains the state of each cell. One bit per cell
    // Layout: 7 6 5 4 3 2 1 0 | 15 14 13 12 11 10 9 8 | 23 ...
    private byte[] fieldData;
    private byte[] nextStep;
    private final int width;
    private final int height;
    // For rule sets 1 means change 0 mean no change
    // Bit 0 no neighbours bit 8 8 neighbours
    // Ruleset for dead cells
    private final short birthRule;
    // Ruleset for alive cells
    private final short deathRule;

    public GameOfLife(int width, int height, short birthRule, short deathRule) {
        this.width = width;
        this.height = height;
        this.birthRule = birthRule;
        this.deathRule = deathRule;
        fieldData = new byte[width * height >> 3 + Math.min(width * height & 7, 1)];
        nextStep = new byte[fieldData.length];
    }

    public GameOfLife(int width, int height) {
        this(width, height, (short)0b100000, (short)0b110011111);
    }

    /**
     * @return true when a given cell is alive
     */
    public boolean getAlive(int x, int y) {
        int bit = y * width + x;
        return (fieldData[bit >> 3] >> (bit & 7) & 1) == 1; // Inversion correct?
    }

    /**
     * Sets the Cell State for the next Step. Should only be called in {@link #step()}.
     */
    private void setAliveNextStep(int x, int y, boolean alive) {
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
        int bit = y * width + x;
        if (alive)
            fieldData[bit >> 3] |= 1 << (bit & 7);
        else
            fieldData[bit >> 3] ^= 1 << (bit & 7);
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
                if ((targetX = (x + i + width)) > width)
                    targetX -= width;
                if ((targetY = (y + j + height)) > height)
                    targetY -= height;
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
     * @return Information on what cells changed their state.
     */
    public byte[] step() {
        System.arraycopy(fieldData, 0, nextStep, 0, fieldData.length);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                //For each cell
                byte neighbours = getNeighbours(x, y);
                boolean alive = getAlive(x, y);
                short ruleset = alive ? deathRule : birthRule;
                if ((ruleset >> (8 - neighbours) & 1) == 1) { // Inversion correct?
                    setAliveNextStep(x, y, !alive);
                }
            }
        }
        byte[] changedBits = new byte[fieldData.length];
        for (int i = 0; i < fieldData.length; i++) {
            changedBits[i] = (byte)(nextStep[i] ^ fieldData[i]);
        }
        byte[] temp = fieldData;
        fieldData = nextStep;
        nextStep = temp;
        return changedBits;
    }
}
