package de.hhn.gameoflife.view;

import de.hhn.gameoflife.control.GOLWindowControl;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GOLWindow extends ImageViewer {
    private GOLWindowControl control;
    private JSlider speedSlider;
    private JLabel speedLabel;
    private JButton stepButton;

    public GOLWindow(int width, int height, Container container) {
        super(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB), container);
        control = new GOLWindowControl(this, width, height);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        speedSlider = new JSlider();
        speedSlider.setMinimum(0);
        speedSlider.setMaximum(101);
        speedSlider.setValue(10);
        speedSlider.addChangeListener(control::fpsSliderChanged);
        stepButton = new JButton("Schritt");
        stepButton.addActionListener(e -> control.stepButtonPressed());
        speedLabel = new JLabel("2");
        speedLabel.setForeground(new Color(140, 140, 140));
        topPanel.add(stepButton);
        topPanel.add(new JLabel("Ziel FPS:"));
        topPanel.add(speedSlider);
        topPanel.add(speedLabel);
        add(topPanel, BorderLayout.PAGE_START);
        setTitle(GOLWindowControl.getNextName());
        pack();
    }

    public void setSpeedLabel(String text) {
        speedLabel.setText(text);
    }
}
