package de.hhn.gameoflife.control;

import de.hhn.gameoflife.model.GameOfLife;
import de.hhn.gameoflife.util.GOLCellChangedListener;
import de.hhn.gameoflife.util.GOLMode;
import de.hhn.gameoflife.view.GOLWindow;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.awt.event.*;

import static de.hhn.gameoflife.GameOfLifeApplication.getMode;

public class GOLWindowControl implements
        Runnable,
        GOLCellChangedListener,
        MouseListener,
        InternalFrameListener {
    private static int NEXT_ID = 0;
    private volatile int waitTime;
    private volatile boolean threadStop;
    private volatile Color aliveColor;
    private volatile Color deadColor;
    private final GameOfLife gol;
    private final GOLWindow window;

    public GOLWindowControl(GOLWindow window, int width, int height) {
        waitTime = 100;
        aliveColor = Color.BLACK;
        deadColor = Color.WHITE;
        gol = new GameOfLife(width, height);
        threadStop = false;
        this.window = window;

        updateAllCells();

        window.addInternalFrameListener(this);
        window.addImageMouseListener(this);

        Thread thread = new Thread(this);
        thread.start();
    }

    public void updateAllCells() {
        for (int i = 0; i < gol.getWidth(); i++)
            for (int j = 0; j < gol.getHeight(); j++)
                cellChangedEvent(i, j, gol.getAlive(i, j));
        window.repaint();
    }

    public synchronized void golStep() {
        gol.step();
        gol.forEachChange(this);
        window.repaint();
    }

    @Override
    public void cellChangedEvent(int x, int y, boolean alive) {
        window.getImage().setRGB(x, y, (alive ? aliveColor : deadColor).getRGB());
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
    public void mouseClicked(MouseEvent mouseEvent) {
        Point imageCoordinate = window.getZoomHandler().transformToImageCoordinate(mouseEvent.getX(), mouseEvent.getY());
        if (imageCoordinate == null)
            return;
        gol.setAlive(imageCoordinate.x, imageCoordinate.y, !gol.getAlive(imageCoordinate.x, imageCoordinate.y));
        window.getImage().setRGB(
                imageCoordinate.x,
                imageCoordinate.y,
                (gol.getAlive(imageCoordinate.x, imageCoordinate.y) ? aliveColor : deadColor).getRGB());
        window.repaint();
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
