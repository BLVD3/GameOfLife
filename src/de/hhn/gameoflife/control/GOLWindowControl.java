package de.hhn.gameoflife.control;

import de.hhn.gameoflife.model.GameOfLife;
import de.hhn.gameoflife.util.GOLCellChangedListener;
import de.hhn.gameoflife.util.GOLMode;
import de.hhn.gameoflife.view.BufferedImageRendererPanel;
import de.hhn.gameoflife.view.GOLWindow;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import static de.hhn.gameoflife.GameOfLifeApplication.getMode;

public class GOLWindowControl implements Runnable, GOLCellChangedListener, MouseWheelListener, KeyListener {
    private static int NEXT_ID = 0;
    private volatile int waitTime;
    private volatile Color aliveColor;
    private volatile Color deadColor;
    private final GOLWindow window;
    private final BufferedImageRendererPanel panel;
    private final GameOfLife gol;
    private final Thread thread;

    public GOLWindowControl(GOLWindow window, BufferedImageRendererPanel panel, int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and/or height below 0. Width: " + width + " Height: " + height);
        }
        this.window = window;
        this.panel = panel;
        waitTime = 100;
        aliveColor = Color.BLACK;
        deadColor = Color.WHITE;
        gol = new GameOfLife(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gol.setAlive(i, j, Math.random() < 0.5);
            }
        }
        updateAllCells();

        panel.addMouseWheelListener(this);
        window.addKeyListener(this);

        thread = new Thread(this);
        thread.start();
    }

    public void updateAllCells() {
        for (int i = 0; i < gol.getWidth(); i++)
            for (int j = 0; j < gol.getHeight(); j++)
                cellChangedEvent(i, j, gol.getAlive(i, j));
        panel.repaint();
    }

    public synchronized void golStep() {
        gol.step();
        gol.forEachChange(this);
        panel.repaint();
    }

    @Override
    public void cellChangedEvent(int x, int y, boolean alive) {
        panel.setPixelNoRepaint(x, y, (alive ? aliveColor : deadColor).getRGB());
    }

    @Override
    public void run() {
        long start;
        while (true) {
            if (getMode() == GOLMode.RUN) {
                golStep();
                start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < waitTime)
                    Thread.onSpinWait();
            } else
                Thread.onSpinWait();
        }
    }

    public static String getNextName() {
        return "Fenster: " + NEXT_ID++;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        panel.setZoomDelta(0.1f * mouseWheelEvent.getWheelRotation());
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        System.out.println(keyEvent.getKeyCode());
        switch (keyEvent.getKeyCode()) {
            case 107 -> panel.setZoomDelta(.5f);
            case 109 -> panel.setZoomDelta(-.5f);
            case 37 -> panel.setShiftDelta(-0.1f / panel.getZoom(), 0);
            case 38 -> panel.setShiftDelta(0, -0.1f / panel.getZoom());
            case 39 -> panel.setShiftDelta(0.1f / panel.getZoom(), 0);
            case 40 -> panel.setShiftDelta(0, 0.1f / panel.getZoom());
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
