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
    BufferedImage buffer;
    GOLWindow window;
    GameOfLife gol;
    Thread thread;
    static final Object SYNC_OBJ = new Object();

    public GOLSimulationPanel(int width, int height, GOLWindow window) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and/or height below 0. Width: " + width + " Height: " + height);
        }

        setOpaque(true);

        this.window = window;
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        gol = new GameOfLife(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                gol.setAlive(i, j, Math.random() < 0.5);
            }
        }
        updateAllCells();

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
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
                while (getMode() != GOLMode.RUN)
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


}
