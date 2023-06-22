package de.hhn.gameoflife.control;

import de.hhn.gameoflife.util.listeners.FPSChangedListener;

import java.util.HashSet;

/**
 * This object keeps track of Time and frames and calculates the frames per second every time a second passed.
 * @author Nico Vogel
 * @version 1.0
 */
public class FPSCounter {
    private HashSet<FPSChangedListener> listeners;
    private int frames;
    private int fps;
    private long lastUpdate;

    /**
     * @return the current frames per second
     */
    public int getFps() {
        return fps;
    }

    public FPSCounter() {
        listeners = new HashSet<>();
        frames = 0;
        fps = 0;
        lastUpdate = System.currentTimeMillis();
    }

    /**
     * call once every frame
     */
    public void add() {
        if (System.currentTimeMillis() - lastUpdate > 1000) {
            fps = frames;
            frames = 0;
            lastUpdate = System.currentTimeMillis();
            fireFPSChangedEvent();
        }
        else
            frames++;
    }

    /**
     * Listener gets called everytime add gets called after a second passed
     */
    public void addListener(FPSChangedListener listener) {
        listeners.add(listener);
    }

    public void removeListener(FPSChangedListener listener) {
        listeners.remove(listener);
    }

    private void fireFPSChangedEvent() {
        listeners.forEach(listener -> listener.fpsChanged(fps));
    }
}
