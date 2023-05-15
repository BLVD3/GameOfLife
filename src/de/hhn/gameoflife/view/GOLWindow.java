package de.hhn.gameoflife.view;

import de.hhn.gameoflife.control.GOLWindowControl;
import de.hhn.gameoflife.view.panels.ImageViewer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GOLWindow extends JInternalFrame {
    private GOLWindowControl control;
    private final ImageViewer viewer;

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
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        viewer = new ImageViewer(bufferedImage);
        control = new GOLWindowControl(this, viewer, width, height);
        add(viewer, BorderLayout.CENTER);
        getContentPane().setPreferredSize(new Dimension(Math.max((int)(width * sizeFactor), 300), (int)(height * sizeFactor)));
        pack();
        setTitle(GOLWindowControl.getNextName());
        setClosable(true);
        setFrameIcon(null);
        setMaximizable(true);
        setIconifiable(true);
        setResizable(true);
        setOpaque(true);
        setFocusable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
