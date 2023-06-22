package de.hhn.gameoflife.util.listeners;

/**
 * Listener for changes in the ZoomHandler
 * @author Nico Vogel
 * @version 1.0
 */
public interface ZoomChangedListener {
    void positionChanged(double newX, double newY);

    void scaleChanged(double newZoom);
}
