package de.hhn.gameoflife.view;

import de.hhn.gameoflife.control.GOLWindowControl;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GOLWindow extends ImageViewer {
    private GOLWindowControl control;

    public GOLWindow(int width, int height, Container container) {
        super(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB), container);
        control = new GOLWindowControl(this, width, height);
        setTitle(GOLWindowControl.getNextName());
    }
}
