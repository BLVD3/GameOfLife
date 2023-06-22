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
    private final JPopupMenu popupMenu;

    public GOLWindow(int width, int height, GOLWindowControl control) {
        super(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB));

        this.control = control;

        popupMenu = new JPopupMenu();
        JMenuItem saveShapeItem = new JMenuItem("Feld als Figur Speichern");
        saveShapeItem.addActionListener(e -> control.save());
        JMenuItem clearItem = new JMenuItem("Feld leeren");
        clearItem.addActionListener(e -> control.clear());
        JMenuItem randomItem = new JMenuItem("Feld Zufällig füllen");
        randomItem.addActionListener(e -> control.randomizeAllCells());
        JMenu colorMenu = new JMenu("Farben ändern");
        JMenuItem liveItem = new JMenuItem("Lebende Zellen");
        liveItem.addActionListener(e -> control.changeLifeColor());
        JMenuItem deadItem = new JMenuItem("Tote Zellen");
        deadItem.addActionListener(e -> control.changeDeadColor());
        colorMenu.add(liveItem);
        colorMenu.add(deadItem);
        popupMenu.add(colorMenu);
        popupMenu.add(saveShapeItem);
        popupMenu.add(clearItem);
        popupMenu.add(randomItem);
        JButton menuButton = new JButton(UIManager.getIcon("FileChooser.detailsViewIcon"));
        menuButton.addActionListener(e -> popupMenu.show(menuButton, menuButton.getWidth() - popupMenu.getWidth(), menuButton.getHeight()));

        speedSlider = new JSlider();
        speedSlider.setMinimum(0);
        speedSlider.setMaximum(101);
        speedSlider.setValue(10);

        JButton stepButton = new JButton("Schritt");
        stepButton.addActionListener(e -> this.control.tryStep());

        speedLabel = new JLabel("2");
        speedLabel.setForeground(new Color(140, 140, 140));

        speedSlider.addChangeListener(this.control::fpsSliderChanged);

        fpsLabel = new JLabel("fps: 0");

        JPanel rightAlign = new JPanel();
        rightAlign.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JPanel leftAlign = new JPanel();
        leftAlign.setLayout(new FlowLayout(FlowLayout.LEFT));
        leftAlign.add(stepButton);
        leftAlign.add(new JLabel("Ziel FPS:"));
        leftAlign.add(speedSlider);
        leftAlign.add(speedLabel);
        rightAlign.add(fpsLabel);
        rightAlign.add(menuButton);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(leftAlign, BorderLayout.WEST);
        topPanel.add(rightAlign, BorderLayout.EAST);
        add(topPanel, BorderLayout.PAGE_START);

        setTitle(GOLWindowControl.getNextName());
        pack();
    }

    public void setSpeedLabel(String text) {
        speedLabel.setText(text);
    }

    public void changeFpsDisplay(int fps) {
        fpsLabel.setText("fps: " + fps);
    }
}
