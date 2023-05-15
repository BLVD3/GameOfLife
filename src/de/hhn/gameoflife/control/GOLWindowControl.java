package de.hhn.gameoflife.control;

import de.hhn.gameoflife.model.GameOfLife;
import de.hhn.gameoflife.util.GOLCellChangedListener;
import de.hhn.gameoflife.util.GOLMode;
import de.hhn.gameoflife.util.ZoomHandler;
import de.hhn.gameoflife.view.panels.ImageViewer;
import de.hhn.gameoflife.view.GOLWindow;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import static de.hhn.gameoflife.GameOfLifeApplication.getMode;

public class GOLWindowControl implements
        Runnable,
        GOLCellChangedListener,
        MouseWheelListener,
        KeyListener,
        MouseListener,
        InternalFrameListener {
    private static int NEXT_ID = 0;
    private volatile int waitTime;
    private volatile boolean threadStop;
    private volatile Color aliveColor;
    private volatile Color deadColor;
    private final ImageViewer viewer;
    private final GameOfLife gol;
    private final ZoomHandler zoomHandler;

    public GOLWindowControl(GOLWindow window, ImageViewer viewer, int width, int height) {
        this.viewer = viewer;
        zoomHandler = viewer.getZoomHandler();
        waitTime = 100;
        aliveColor = Color.BLACK;
        deadColor = Color.WHITE;
        gol = new GameOfLife(width, height);
        threadStop = false;

        updateAllCells();

        viewer.addMouseWheelListener(this);
        window.addKeyListener(this);
        window.addInternalFrameListener(this);
        viewer.addImageMouseListener(this);

        Thread thread = new Thread(this);
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
        viewer.getImage().setRGB(x, y, (alive ? aliveColor : deadColor).getRGB());
    }

    @Override
    public void run() {
        long start;
        while (!threadStop) {
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
        if (mouseWheelEvent.getWheelRotation() > 0)
            zoomHandler.scaleZoom(1.25);
        else
            zoomHandler.scaleZoom(0.8);
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

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point imageCoordinate = zoomHandler.transformToImageCoordinate(mouseEvent.getX(), mouseEvent.getY());
        if (imageCoordinate == null)
            return;
        gol.setAlive(imageCoordinate.x, imageCoordinate.y, !gol.getAlive(imageCoordinate.x, imageCoordinate.y));
        viewer.getImage().setRGB(
                imageCoordinate.x,
                imageCoordinate.y,
                (gol.getAlive(imageCoordinate.x, imageCoordinate.y) ? aliveColor : deadColor).getRGB());
        viewer.repaint();
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) { }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) { }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) { }

    @Override
    public void mouseExited(MouseEvent mouseEvent) { }


    @Override
    public void internalFrameOpened(InternalFrameEvent internalFrameEvent) { }

    @Override
    public void internalFrameClosing(InternalFrameEvent internalFrameEvent) {
        threadStop = true;
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent internalFrameEvent) { }

    @Override
    public void internalFrameIconified(InternalFrameEvent internalFrameEvent) { }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent internalFrameEvent) { }

    @Override
    public void internalFrameActivated(InternalFrameEvent internalFrameEvent) { }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent internalFrameEvent) { }
}
