package de.hhn.gameoflife.util.listeners;

public interface ZoomChangedListener {
    void positionChanged(double newX, double newY);

    void scaleChanged(double newZoom);
}
