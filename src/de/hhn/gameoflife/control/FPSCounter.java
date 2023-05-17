package de.hhn.gameoflife.control;

public class FPSCounter {
    private int frames;
    private int fps;
    private long lastUpdate;

    public int getFps() {
        return fps;
    }

    public FPSCounter() {
        frames = 0;
        fps = 0;
        lastUpdate = System.currentTimeMillis();
    }

    public synchronized void add() {
        if (System.currentTimeMillis() - lastUpdate > 1000) {
            fps = frames;
            frames = 0;
            lastUpdate = System.currentTimeMillis();
            System.out.println(fps);
        }
        else
            frames++;
    }
}
