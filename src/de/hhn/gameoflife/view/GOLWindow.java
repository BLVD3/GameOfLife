package de.hhn.gameoflife.view;

import javax.swing.*;
import java.awt.*;

public class GOLWindow extends JInternalFrame  {
    private static int NEXT_ID = 1;
    private volatile int waitTime;
    private final GOLSimulationPanel simulationPanel;
    private volatile Color aliveColor;
    private volatile Color deadColor;

    public GOLWindow(int width, int height, Container container) {
        waitTime = 0;
        aliveColor = Color.BLACK;
        deadColor = Color.WHITE;
        float sizeFactor = 16;
        while (height * sizeFactor > container.getHeight())
            sizeFactor *= .5f;
        while (width * sizeFactor > container.getWidth())
            sizeFactor *= .5f;
        setLayout(new BorderLayout());
        simulationPanel = new GOLSimulationPanel(width, height, this);
        add(simulationPanel, BorderLayout.CENTER);
        getContentPane().setPreferredSize(new Dimension(Math.max((int)(width * sizeFactor), 300), (int)(height * sizeFactor)));
        pack();
        setTitle("Fenster " + NEXT_ID++);
        setVisible(true);
        setClosable(true);
        setResizable(true);
        setOpaque(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public int getWaitTime() {
        return waitTime;
    }

    public Color getAliveColor() {
        return aliveColor;
    }

    public Color getDeadColor() {
        return deadColor;
    }
}
