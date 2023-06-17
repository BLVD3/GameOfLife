package de.hhn.gameoflife.model;

import de.hhn.gameoflife.util.GOLCellChangedListener;

/**
 * Highly optimized Torus shaped Game of Life model
 * @author Nico Vogel 215998
 * @version 1.0
 */
public class GOLSimulation extends GOLCellArray {
    protected transient byte[] nextStep;
    protected transient final byte[] changedBits;
    // For rule sets 1 means change 0 mean no change
    // Bit 0 no neighbours bit 8 8 neighbours
    // Ruleset for dead cells
    protected final transient short birthRule;
    // Ruleset for alive cells
    protected final transient short deathRule;

    /**
     * @param width Width of this GOL field
     * @param height Height of this GOL field
     * @param birthRule A short describing the behaviour of a dead Cell.<br/>
     *                  Example:<br/>
     *                  A 1 at Position 3 makes it so a Cell becomes alive with 3 neighbouring alive cells.<br/>
     *                  Everything beyond the 9th bit will be ignored.
     * @param deathRule A short describing the behaviour of an alive Cell.
     */
    public GOLSimulation(int width, int height, short birthRule, short deathRule) {
        super(width, height);
        this.birthRule = birthRule;
        this.deathRule = deathRule;
        nextStep = new byte[fieldData.length];
        changedBits = new byte[fieldData.length];
    }

    /**
     * @param width Width of this GOL field
     * @param height Height of this GOL field
     */
    public GOLSimulation(int width, int height) {
        this(width, height, (short)0b100000, (short)0b110011111);
    }

    /**
     * Sets the Cell State for the next Step. Should only be called in {@link #step()}.
     */
    public void setAliveNextStep(int x, int y, boolean alive) {
        //assertBounds(x, y);
        int bit = y * width + x;
        if (alive)
            nextStep[bit >> 3] |= 1 << (bit & 7);
        else
            nextStep[bit >> 3] ^= 1 << (bit & 7);
    }

    /**
     * @return Count of Alive Neighbours
     */
    public byte getNeighbours(int x, int y) { //TODO Perfomance testen
        byte neighbourCount = 0;
        int[] xs = new int[3];
        int[] ys = new int[3];
        xs[1] = x;
        ys[1] = y;
        xs[0] = x < 1 ? x + width - 1 : x - 1;
        ys[0] = y < 1 ? y + height - 1 : y - 1;
        xs[2] = x < width - 1 ? x + 1 : x + 1 - width;
        ys[2] = y < height - 1 ? y + 1 : y + 1 - height;
        for (int i : xs) {
            for (int j : ys) {
                if (getAlive(i, j))
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
        performPreMadeStep();
    }

    public void performPreMadeStep() {
        for (int i = 0; i < fieldData.length; i++) {
            changedBits[i] = (byte)(nextStep[i] ^ fieldData[i]);
        }
        byte[] temp = fieldData;
        fieldData = nextStep;
        nextStep = temp;
    }

    /**
     * Calls the listeners {@link GOLCellChangedListener#cellChangedEvent(int, int, boolean)} for every cell that has changed in the last step
     * @param listener Listener containing the {@link GOLCellChangedListener#cellChangedEvent(int, int, boolean)}
     */
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
