package de.hhn.gameoflife.view;

import de.hhn.gameoflife.control.GOLWindowControl;

import javax.swing.*;
import java.awt.*;

public class GOLWindow extends JInternalFrame {
    private GOLWindowControl control;
    private final BufferedImageRendererPanel simulationPanel;

    public GOLWindow(int width, int height, Container container) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and/or height below 0. Width: " + width + " Height: " + height);
        }
        float sizeFactor = 16;
        while (height * sizeFactor > container.getHeight())
            sizeFactor *= .5f;
        while (width * sizeFactor > container.getWidth())
            sizeFactor *= .5f;
        setLayout(new BorderLayout());
        simulationPanel = new BufferedImageRendererPanel(width, height);
        control = new GOLWindowControl(this, simulationPanel, width, height);
        add(simulationPanel, BorderLayout.CENTER);
        getContentPane().setPreferredSize(new Dimension(Math.max((int)(width * sizeFactor), 300), (int)(height * sizeFactor)));
        pack();
        setTitle(GOLWindowControl.getNextName());
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setOpaque(true);
        setFocusable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
