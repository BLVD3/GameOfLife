package de.hhn.gameoflife.control;

import de.hhn.gameoflife.model.GOLSimulation;
import de.hhn.gameoflife.util.FPSChangedListener;
import de.hhn.gameoflife.util.GOLCellChangedListener;
import de.hhn.gameoflife.util.GOLMode;
import de.hhn.gameoflife.view.GOLWindow;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.awt.event.*;

import static de.hhn.gameoflife.GameOfLifeApplication.getMode;

public class GOLWindowControl implements
        Runnable,
        GOLCellChangedListener,
        MouseListener,
        InternalFrameListener,
        FPSChangedListener {
    private static int NEXT_ID = 0;
    private volatile long waitTime;
    private volatile boolean threadStop;
    private volatile Color aliveColor;
    private volatile Color deadColor;
    private final GOLSimulation gol;
    private final GOLWindow window;
    private final FPSCounter fpsCounter;

    public GOLWindowControl(GOLWindow window, int width, int height) {
        aliveColor = Color.BLACK;
        deadColor = Color.WHITE;
        gol = new GOLSimulation(width, height);
        fpsCounter = new FPSCounter();
        threadStop = false;
        this.window = window;
        calculateFps(10);

        updateAllCells();

        window.addInternalFrameListener(this);
        window.addImageMouseListener(this);
        fpsCounter.addListener(this);

        Thread thread = new Thread(this);
        thread.start();
    }

    public void randomizeAllCells() {
        for (int i = 0; i < gol.getHeight(); i++) {
            for (int j = 0; j < gol.getWidth(); j++) {
                gol.setAlive(j, i, Math.random() > 0.5);
            }
        }
        updateAllCells();
    }

    public void clear() {
        for (int i = 0; i < gol.getHeight(); i++) {
            for (int j = 0; j < gol.getWidth(); j++) {
                gol.setAlive(j, i, false);
            }
        }
        updateAllCells();
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
                start = System.nanoTime();
                golStep();
                fpsCounter.add();
                while (System.nanoTime() - start < waitTime)
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
    public void internalFrameClosing(InternalFrameEvent internalFrameEvent) {
        threadStop = true;
    }

    public void stepButtonPressed() {
        if (getMode() != GOLMode.RUN) {
            golStep();
        }
    }

    public void fpsSliderChanged(ChangeEvent e) {
        if (e.getSource() instanceof JSlider slider) {
            if (slider.getValue() == 101) {
                waitTime = 0;
                window.setSpeedLabel("∞");
            }
            else {
                calculateFps(slider.getValue());
            }
        }
    }

    private void calculateFps(int sliderValue) {
        int fpsTarget = (int)Math.round(Math.pow(Math.pow(144, 0.01), sliderValue));
        waitTime = 1000000000L / fpsTarget;
        window.setSpeedLabel(String.valueOf(fpsTarget));
    }

    @Override
    public void fpsChanged(int fps) {
        window.changeFpsDisplay(fps);
    }

    //#region unused events
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
    public void internalFrameClosed(InternalFrameEvent internalFrameEvent) { }

    @Override
    public void internalFrameIconified(InternalFrameEvent internalFrameEvent) { }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent internalFrameEvent) { }

    @Override
    public void internalFrameActivated(InternalFrameEvent internalFrameEvent) { }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent internalFrameEvent) { }
    //#endregion
}
