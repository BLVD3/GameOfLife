package de.hhn.gameoflife.util.listeners;

/**
 * Listener for PixelStreams
 * Currently only used to draw Lines
 * @author Nico Vogel
 * @version 1.0
 */
public interface PixelListener {
    void pixelEvent(int x, int y);
}
