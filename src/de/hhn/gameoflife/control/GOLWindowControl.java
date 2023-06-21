package de.hhn.gameoflife.control;

import de.hhn.gameoflife.model.GOLShape;
import de.hhn.gameoflife.model.GOLSimulation;
import de.hhn.gameoflife.util.*;
import de.hhn.gameoflife.util.listeners.FPSChangedListener;
import de.hhn.gameoflife.util.listeners.GOLCellChangedListener;
import de.hhn.gameoflife.view.GOLWindow;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.FileAlreadyExistsException;

public class GOLWindowControl implements
        Runnable,
        GOLCellChangedListener,
        MouseListener,
        MouseMotionListener,
        InternalFrameListener,
        FPSChangedListener {
    private static int NEXT_ID = 0;
    private final Point mousePosition;
    private boolean isDrawing;
    private volatile long waitTime;
    private volatile boolean threadStop;
    private volatile Color aliveColor;
    private volatile Color deadColor;
    private final GOLSimulation gol;
    private final GOLWindow window;
    private final FPSCounter fpsCounter;

    public GOLWindowControl(int width, int height) {
        mousePosition = new Point();
        aliveColor = Color.BLACK;
        deadColor = Color.WHITE;
        gol = new GOLSimulation(width, height);
        fpsCounter = new FPSCounter();
        threadStop = false;
        this.window = new GOLWindow(width, height, this);
        calculateFps(10);

        updateAllCells();

        window.addInternalFrameListener(this);
        window.addImageMouseListener(this);
        window.addImageMouseMotionListener(this);
        fpsCounter.addListener(this);

        Thread thread = new Thread(this);
        thread.start();
    }

    public GOLWindow getWindow() {
        return window;
    }

    public void randomizeAllCells() {
        if (GOLMain.getInstance().getMode() == GOLMode.RUN)
            return;
        for (int i = 0; i < gol.getHeight(); i++) {
            for (int j = 0; j < gol.getWidth(); j++) {
                gol.setAlive(j, i, Math.random() > 0.5);
            }
        }
        updateAllCells();
    }

    public void clear() {
        if (GOLMain.getInstance().getMode() == GOLMode.RUN)
            return;
        for (int i = 0; i < gol.getHeight(); i++) {
            for (int j = 0; j < gol.getWidth(); j++) {
                gol.setAlive(j, i, false);
            }
        }
        updateAllCells();
    }

    public void save() {
        String name = JOptionPane.showInputDialog("Name der Figur:");
        if (name == null)
            return;
        if (name.equals("")) {
            JOptionPane.showMessageDialog(null, "Eine Figur mit leerem Namen kann nicht gespeichert werden.");
            return;
        }
        try {
            GOLShape shape = ShapeIO.saveShape(gol, name);
            GOLMain.getInstance().getShapeSelector().addShape(shape);
        } catch (FileAlreadyExistsException e) {
            JOptionPane.showMessageDialog(null, "Figur existiert bereits");
        }
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
            if (GOLMain.getInstance().getMode() == GOLMode.RUN) {
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

    }

    private void setGOLPixel(Point imageCoordinate, boolean alive) {
        setGOLPixel(imageCoordinate.x, imageCoordinate.y, alive);
    }

    private void setGOLPixel(int x, int y, boolean alive) {
        gol.setAlive(x, y, alive);
        window.getImage().setRGB(x, y, (alive ? aliveColor : deadColor).getRGB());
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent internalFrameEvent) {
        threadStop = true;
    }

    public void stepButtonPressed() {
        if (GOLMain.getInstance().getMode() != GOLMode.RUN) {
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

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        mouseMoved(mouseEvent);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Point imageCoordinate = window.getZoomHandler().transformToImageCoordinate(mouseEvent.getX(), mouseEvent.getY());
        if (imageCoordinate == null)
            return;
        switch (GOLMain.getInstance().getMode()) {
            case RUN -> {
                return;
            }
            case SET -> {
                setGOLPixel(imageCoordinate, true);
                window.repaint();
            }
            case DRAW -> {
                mousePosition.setLocation(imageCoordinate);
                isDrawing = true;
            }
            case TOGGLE -> {
                setGOLPixel(imageCoordinate, !gol.getAlive(imageCoordinate.x, imageCoordinate.y));
                window.repaint();
            }
            case SHAPES -> {
                gol.applyShape(GOLMain.getInstance().getSelectedShape().getShape(), imageCoordinate.x, imageCoordinate.y);
                updateAllCells();
            }
        }
    }

    //#region unused events

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        isDrawing = false;
    }

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

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        if (!isDrawing || GOLMain.getInstance().getMode() != GOLMode.DRAW)
            return;
        Point newMousePosition = window.getZoomHandler().transformToImageCoordinate(mouseEvent.getX(), mouseEvent.getY());
        if (newMousePosition == null) {
            isDrawing = false;
            return;
        }
        PixelStreams.forEachPixelInLine(mousePosition.x, mousePosition.y, newMousePosition.x, newMousePosition.y,
                (x, y) -> setGOLPixel(x, y, true));
        mousePosition.setLocation(newMousePosition);
        window.repaint();
    }
    //#endregion
}
