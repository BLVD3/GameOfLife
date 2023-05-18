package de.hhn.gameoflife.util;

public interface ZoomChangedListener {
    void positionChanged(double newX, double newY);

    void scaleChanged(double newZoom);
}
