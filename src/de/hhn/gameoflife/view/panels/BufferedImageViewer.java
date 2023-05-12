package de.hhn.gameoflife.view.panels;

import javax.swing.*;
import java.awt.*;

public class BufferedImageViewer extends JPanel {
    BufferedImageRendererPanel panel;
    JPanel topPanel;
    JPanel bottomPanel;
    JSlider zoomSlider;

    public BufferedImageViewer(int width, int height) {
        setLayout(new BorderLayout());
        panel = new BufferedImageRendererPanel(width, height);

        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(new JScrollBar(Adjustable.VERTICAL), BorderLayout.EAST);
        topPanel.add(new JScrollBar(Adjustable.HORIZONTAL), BorderLayout.SOUTH);

        topPanel.add(panel, BorderLayout.CENTER);

        zoomSlider = new JSlider();
        zoomSlider.setMinimum(0);
        zoomSlider.setMaximum(1000);
        zoomSlider.setValue(0);



        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(new JButton("-"));
        bottomPanel.add(zoomSlider);
        bottomPanel.add(new JButton("+"));

        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.PAGE_END);
    }
}
