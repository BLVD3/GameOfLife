package de.hhn.gameoflife.util.listeners;

/**
 * Interface for the custom stream in GameOfLife
 */
public interface GOLCellChangedListener {
    /**
     * Applying the Change that happened
     * @param x X-Coordinate of the changed Cell
     * @param y Y-Coordinate of the changed Cell
     * @param alive The new state of the changed Cell
     */
    void cellChangedEvent(int x, int y, boolean alive);
}
