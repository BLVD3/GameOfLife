package de.hhn.gameoflife.view;

import de.hhn.gameoflife.control.GOLWindowControl;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GOLWindow extends ImageViewer {
    private final GOLWindowControl control;
    private final JSlider speedSlider;
    private final JLabel speedLabel;
    private final JLabel fpsLabel;

    public GOLWindow(int width, int height, GOLWindowControl control) {
        super(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));
        this.control = control;
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        speedSlider = new JSlider();
        speedSlider.setMinimum(0);
        speedSlider.setMaximum(101);
        speedSlider.setValue(10);
        JButton stepButton = new JButton("Schritt");
        speedLabel = new JLabel("2");
        speedLabel.setForeground(new Color(140, 140, 140));
        fpsLabel = new JLabel("fps: 0");
        JButton randomizeButton = new JButton("Zufall");
        randomizeButton.addActionListener(e -> this.control.randomizeAllCells());
        JButton clearButton = new JButton("Leeren");
        clearButton.addActionListener(e -> this.control.clear());
        JButton saveAsShapeButton = new JButton("Als Figur Speichern");
        saveAsShapeButton.addActionListener(e -> this.control.save());
        topPanel.add(stepButton);
        topPanel.add(new JLabel("Ziel FPS:"));
        topPanel.add(speedSlider);
        topPanel.add(speedLabel);
        topPanel.add(randomizeButton);
        topPanel.add(clearButton);
        topPanel.add(fpsLabel);
        topPanel.add(saveAsShapeButton);
        add(topPanel, BorderLayout.PAGE_START);
        setTitle(GOLWindowControl.getNextName());
        pack();
        speedSlider.addChangeListener(this.control::fpsSliderChanged);
        stepButton.addActionListener(e -> this.control.stepButtonPressed());
    }

    public void setSpeedLabel(String text) {
        speedLabel.setText(text);
    }

    public void changeFpsDisplay(int fps) {
        fpsLabel.setText("fps: " + fps);
    }
}
