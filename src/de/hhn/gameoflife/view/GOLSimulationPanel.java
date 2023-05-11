package de.hhn.gameoflife.view;

import de.hhn.gameoflife.GameOfLifeApplication;
import de.hhn.gameoflife.model.GameOfLife;
import de.hhn.gameoflife.util.GOLCellChangedListener;
import de.hhn.gameoflife.util.GOLMode;
import de.hhn.gameoflife.util.GOLModeChangedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import static de.hhn.gameoflife.util.RenderedImageHelper.fillRenderedImage;
import static de.hhn.gameoflife.GameOfLifeApplication.getMode;

public class GOLSimulationPanel extends JPanel implements Runnable, GOLModeChangedListener, GOLCellChangedListener, ComponentListener {
    private final BufferedImage buffer;
    private final BufferedImageZoom zoom;
    private final GOLWindow window;
    private final GameOfLife gol;

    public GOLSimulationPanel(int width, int height, GOLWindow window) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and/or height below 0. Width: " + width + " Height: " + height);
        }

        this.window = window;

        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        gol = new GameOfLife(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gol.setAlive(i, j, Math.random() < 0.5);
            }
        }
        updateAllCells();
        zoom = new BufferedImageZoom(new Dimension(getWidth(), getHeight()), new Dimension(buffer.getWidth(), buffer.getHeight()));

        addComponentListener(this);
        GameOfLifeApplication.addListener(this);
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void paint(Graphics g) {
        Rectangle source = zoom.getSourceRect();
        Rectangle target = zoom.getTargetRect();
        g.drawImage(buffer,
                target.x, target.y,
                target.x + target.width,
                target.y + target.height,
                source.x, source.y,
                source.x + source.width,
                source.y + source.height,
                null);
    }

    @Override
    public void run() {
        long start;
        while (true) {
            if (getMode() == GOLMode.RUN) {
                golStep();
                start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start < window.getWaitTime())
                    Thread.onSpinWait();
            } else {
                Thread.onSpinWait();
            }
        }
    }

    public synchronized void golStep() {
        gol.step();
        gol.forEachChange(this);
        repaint();
    }

    public void updateAllCells() {
        for (int i = 0; i < gol.getWidth(); i++) {
            for (int j = 0; j < gol.getHeight(); j++) {
                cellChangedEvent(i, j, gol.getAlive(i, j));
                //System.out.println(gol.getAlive(i, j));
            }
        }
        repaint();
    }


    @Override
    public void modeChangedEvent(GOLMode mode) {

    }

    @Override
    public void cellChangedEvent(int x, int y, boolean alive) {
        buffer.setRGB(x, y, (alive ? window.getAliveColor() : window.getDeadColor()).getRGB());
    }

    @Override
    public void componentResized(ComponentEvent e) {
        zoom.resizeRenderTarget(getSize());
        repaint();
    }

    @Override
    public void componentMoved(ComponentEvent e) { }

    @Override
    public void componentShown(ComponentEvent e) { }

    @Override
    public void componentHidden(ComponentEvent e) { }

    public void setZoom(byte zoomLevel) {
        zoom.setZoom(zoomLevel);
        if (zoom.getZoomLevel() == 1) {
            zoom.setShift(0.5f,0.5f);
        }
    }

    public void setShift(float shiftX, float shiftY) {
        zoom.setShift(shiftX, shiftY);
    }
}
