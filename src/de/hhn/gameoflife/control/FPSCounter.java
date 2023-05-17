package de.hhn.gameoflife.control;

import de.hhn.gameoflife.util.FPSChangedListener;

import java.util.Formattable;
import java.util.HashSet;

public class FPSCounter {
    private HashSet<FPSChangedListener> listeners;
    private int frames;
    private int fps;
    private long lastUpdate;

    public int getFps() {
        return fps;
    }

    public FPSCounter() {
        listeners = new HashSet<>();
        frames = 0;
        fps = 0;
        lastUpdate = System.currentTimeMillis();
    }

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

    public void addListener(FPSChangedListener listener) {
        listeners.add(listener);
    }

    public void removeListener(FPSChangedListener listener) {
        listeners.remove(listener);
    }

    public void fireFPSChangedEvent() {
        listeners.forEach(listener -> listener.fpsChanged(fps));
    }
}
