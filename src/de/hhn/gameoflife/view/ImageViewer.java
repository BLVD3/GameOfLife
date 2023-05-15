package de.hhn.gameoflife.view;

import de.hhn.gameoflife.util.ZoomHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ImageViewer extends JInternalFrame implements MouseWheelListener {
    private final ImageRendererPanel panel;
    private final JSlider zoomSlider;
    private final JScrollBar scrollBarV;
    private final JScrollBar scrollBarH;

    public ImageViewer(BufferedImage image, Container container) {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Hi");
            }
        });
        float sizeFactor = 64;
        while ((int)(image.getHeight() * sizeFactor) > container.getHeight() * 4 / 5)
            sizeFactor *= .5f;
        while ((int)(image.getWidth() * sizeFactor) > container.getWidth() * 4 / 5)
            sizeFactor *= .5f;
        getContentPane().setPreferredSize(new Dimension(Math.max((int)(image.getWidth() * sizeFactor), 300), (int)(image.getHeight() * sizeFactor)));
        pack();
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

        panel.addMouseWheelListener(this);

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

    public void pressedSomething() {
        System.out.println("Hallo");
    }

    public BufferedImage getImage() {
        return panel.getImage();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {
        if (mouseWheelEvent.getWheelRotation() > 0)
            getZoomHandler().scaleZoom(1.25);
        else
            getZoomHandler().scaleZoom(0.8);
    }

    public ZoomHandler getZoomHandler() {
        return panel.getZoomHandler();
    }

    public void addImageMouseListener(MouseListener listener) {
        panel.addMouseListener(listener);
    }
}
