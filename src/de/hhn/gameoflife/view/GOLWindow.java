package de.hhn.gameoflife.view;

import de.hhn.gameoflife.control.GOLWindowControl;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GOLWindow extends ImageViewer {
    private GOLWindowControl control;
    private JSlider speedSlider;

    public GOLWindow(int width, int height, Container container) {
        super(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB), container);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        speedSlider = new JSlider();
        speedSlider.setMinimum(0);
        speedSlider.setMaximum(240);
        speedSlider.setMajorTickSpacing(48);
        speedSlider.setMinorTickSpacing(6);
        speedSlider.setValue(30);
        speedSlider.setSnapToTicks(true);
        speedSlider.setPaintTrack(true);
        speedSlider.setPaintLabels(true);
        topPanel.add(new JLabel("Ziel FPS:"));
        topPanel.add(speedSlider);
        add(topPanel, BorderLayout.PAGE_START);
        control = new GOLWindowControl(this, width, height);
        setTitle(GOLWindowControl.getNextName());
        pack();
    }
}
