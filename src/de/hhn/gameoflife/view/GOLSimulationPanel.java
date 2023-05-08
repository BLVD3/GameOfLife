package de.hhn.gameoflife.view;

import de.hhn.gameoflife.model.GameOfLife;
import de.hhn.gameoflife.util.GOLCellChangedListener;
import de.hhn.gameoflife.util.GOLMode;
import de.hhn.gameoflife.util.GOLModeChangedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static de.hhn.gameoflife.util.RenderedImageHelper.fillRenderedImage;
import static de.hhn.gameoflife.GameOfLifeApplication.getMode;

public class GOLSimulationPanel extends JPanel implements Runnable, GOLModeChangedListener, GOLCellChangedListener {

    BufferedImage renderImage;
    GOLWindow window;
    GameOfLife gol;
    Thread thread;

    public GOLSimulationPanel(int width, int height, GOLWindow window) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and/or height below 0. Width: " + width + " Height: " + height);
        }

        this.window = window;
        renderImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        gol = new GameOfLife(width, height);
        fillRenderedImage(renderImage, Color.WHITE);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(renderImage, 0, 0, getWidth(), getHeight(), null);
    }

    @Override
    public void run() {
        long start;
        while (true) {
            if (getMode() == GOLMode.RUN) {
                golStep();
                start = System.currentTimeMillis();
                while (System.currentTimeMillis() - start > window.getWaitTime())
                    Thread.onSpinWait();
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    public synchronized void golStep() {
        gol.step();
        gol.forEachChange(this);
        repaint();
    }

    @Override
    public void modeChangedEvent(GOLMode mode) {
        notifyAll();
    }

    @Override
    public void cellChangedEvent(int x, int y, boolean alive) {
        renderImage.setRGB(x, y, (alive ? window.getAliveColor() : window.getDeadColor()).getRGB());
    }
}
