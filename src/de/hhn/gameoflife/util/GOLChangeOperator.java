package de.hhn.gameoflife.util;

/**
 * Interface for the change stream in GameOfLife
 */
public interface GOLChangeOperator {
    void apply(int x, int y, boolean alive);
}
