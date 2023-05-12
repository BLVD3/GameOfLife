package de.hhn.gameoflife.control;

import de.hhn.gameoflife.model.GameOfLife;
import de.hhn.gameoflife.util.GOLCellChangedListener;
import de.hhn.gameoflife.util.GOLMode;
import de.hhn.gameoflife.util.ZoomHandler;
import de.hhn.gameoflife.view.panels.ImageViewer;
import de.hhn.gameoflife.view.GOLWindow;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import static de.hhn.gameoflife.GameOfLifeApplication.getMode;

public class GOLWindowControl implements Runnable, GOLCellChangedListener, MouseWheelListener, KeyListener {
    private static int NEXT_ID = 0;
    private volatile int waitTime;
    private volatile Color aliveColor;
    private volatile Color deadColor;
    private final ImageViewer viewer;
    private final BufferedImage image;
    private final GameOfLife gol;
    private final Thread thread;
    private final ZoomHandler zoomHandler;

    public GOLWindowControl(GOLWindow window, ImageViewer viewer) {
        this.viewer = viewer;
        if (!(viewer.getImage() instanceof BufferedImage))
            throw new IllegalArgumentException("Viewer is not a viewer of a Buffered Image");
        image = (BufferedImage) viewer.getImage();
        zoomHandler = viewer.getZoomHandler();
        waitTime = 100;
        aliveColor = Color.BLACK;
        deadColor = Color.WHITE;
        gol = new GameOfLife(image.getWidth(), image.getHeight());

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                gol.setAlive(i, j, Math.random() < 0.5);
            }
        }
        updateAllCells();

        viewer.addMouseWheelListener(this);
        window.addKeyListener(this);

        thread = new Thread(this);
        thread.start();
    }

    public void updateAllCells() {
        for (int i = 0; i < gol.getWidth(); i++)
            for (int j = 0; j < gol.getHeight(); j++)
                cellChangedEvent(i, j, gol.getAlive(i, j));
        viewer.repaint();
    }

    public synchronized void golStep() {
        gol.step();
        gol.forEachChange(this);
        viewer.repaint();
    }

    @Override
    public void cellChangedEvent(int x, int y, boolean alive) {
        image.setRGB(x, y, (alive ? aliveColor : deadColor).getRGB());
    }

    @Override
    public void run() {
        long start;
        while (true) {
            if (getMode() == GOLMode.RUN) {
                start = System.currentTimeMillis();
                golStep();
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
        zoomHandler.setZoomDelta(0.1f * mouseWheelEvent.getWheelRotation());
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case 107 -> zoomHandler.setZoomDelta(.5f);
            case 109 -> zoomHandler.setZoomDelta(-.5f);
            case 37 -> zoomHandler.setShiftDeltaRelative(-0.1f, 0);
            case 38 -> zoomHandler.setShiftDeltaRelative(0, -0.1f);
            case 39 -> zoomHandler.setShiftDeltaRelative(0.1f, 0);
            case 40 -> zoomHandler.setShiftDeltaRelative(0, 0.1f);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
