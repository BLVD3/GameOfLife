package de.hhn.gameoflife.view.panels;

import de.hhn.gameoflife.util.ZoomHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;

public class ImageViewer extends JPanel {
    private final ImageRendererPanel panel;
    private final JSlider zoomSlider;
    private final JScrollBar scrollBarV;
    private final JScrollBar scrollBarH;

    public ImageViewer(Image image) {
        setLayout(new BorderLayout());
        panel = new ImageRendererPanel(image);
        scrollBarH = new JScrollBar(Adjustable.HORIZONTAL);
        scrollBarV = new JScrollBar(Adjustable.VERTICAL);
        zoomSlider = new JSlider();

        zoomSlider.setMinimum(0);
        zoomSlider.setMaximum(1000);
        zoomSlider.setValue(0);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(scrollBarV, BorderLayout.EAST);
        JPanel bottomScrollPanel = new JPanel();
        bottomScrollPanel.setLayout(new BorderLayout());
        bottomScrollPanel.add(scrollBarH, BorderLayout.CENTER);
        JPanel distanceHolder = new JPanel();
        distanceHolder.setPreferredSize(new Dimension(10, 10));
        bottomScrollPanel.add(distanceHolder, BorderLayout.EAST);
        topPanel.add(bottomScrollPanel, BorderLayout.SOUTH);
        topPanel.add(panel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(new JButton("-"));
        bottomPanel.add(zoomSlider);
        bottomPanel.add(new JButton("+"));

        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.PAGE_END);
    }

    public Image getImage() {
        return panel.getImage();
    }

    public ZoomHandler getZoomHandler() {
        return panel.getZoomHandler();
    }

    public void addImageMouseListener(MouseListener listener) {
        panel.addMouseListener(listener);
    }
}
