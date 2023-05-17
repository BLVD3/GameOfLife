package de.hhn.gameoflife.view;

import de.hhn.gameoflife.util.ZoomChangedListener;
import de.hhn.gameoflife.util.ZoomHandler;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class ImageViewer extends JInternalFrame implements MouseWheelListener, KeyListener, ZoomChangedListener, AdjustmentListener {
    private final ImageRendererPanel panel;
    private final JSlider zoomSlider;
    private final JScrollBar scrollBarV;
    private final JScrollBar scrollBarH;

    public ImageViewer(BufferedImage image, Container container) {
        float sizeFactor = 64;
        while ((int)(image.getHeight() * sizeFactor) > container.getHeight() * 4 / 5)
            sizeFactor *= .5f;
        while ((int)(image.getWidth() * sizeFactor) > container.getWidth() * 4 / 5)
            sizeFactor *= .5f;
        panel = new ImageRendererPanel(image);
        panel.setPreferredSize(new Dimension(Math.max((int)(image.getWidth() * sizeFactor), 300), (int)(image.getHeight() * sizeFactor)));
        setLayout(new BorderLayout());
        scrollBarH = new JScrollBar(Adjustable.HORIZONTAL);
        scrollBarV = new JScrollBar(Adjustable.VERTICAL);
        zoomSlider = new JSlider();

        zoomSlider.setMinimum(0);
        zoomSlider.setMaximum(1000);
        zoomSlider.setValue(0);

        setUpScrollbars();
        scrollBarH.setValue(50);
        scrollBarV.setValue(50);

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
        addKeyListener(this);
        getZoomHandler().addListener(this);
        scrollBarH.addAdjustmentListener(this);
        scrollBarV.addAdjustmentListener(this);

        pack();
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
        if ((mouseWheelEvent.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK) {
            if (mouseWheelEvent.getWheelRotation() > 0)
                getZoomHandler().scaleZoom(1.25);
            else
                getZoomHandler().scaleZoom(0.8);
        } else if ((mouseWheelEvent.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) == MouseEvent.SHIFT_DOWN_MASK) {
            getZoomHandler().setShiftDeltaRelative(mouseWheelEvent.getWheelRotation() * 0.1, 0);
        } else {
            getZoomHandler().setShiftDeltaRelative(0, mouseWheelEvent.getWheelRotation() * 0.1);
        }
    }

    public ZoomHandler getZoomHandler() {
        return panel.getZoomHandler();
    }

    public void addImageMouseListener(MouseListener listener) {
        panel.addMouseListener(listener);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        System.out.println(keyEvent.getKeyCode());
        switch (keyEvent.getKeyCode()) {
            case 37 -> getZoomHandler().setShiftDeltaRelative(-0.1, 0);
            case 38 -> getZoomHandler().setShiftDeltaRelative(0, -0.1);
            case 39 -> getZoomHandler().setShiftDeltaAbsolute(0.1, 0);
            case 40 -> getZoomHandler().setShiftDeltaAbsolute(0, 0.1);
            case 107 -> getZoomHandler().scaleZoom(1.5);
            case 109 -> getZoomHandler().scaleZoom(2./3.);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    @Override
    public void positionChanged() {
        scrollBarH.setValue((int)Math.round(getZoomHandler().getXShift() * 100));
        scrollBarV.setValue((int)Math.round(getZoomHandler().getYShift() * 100));
    }

    @Override
    public void scaleChanged() {
        setUpScrollbars();
    }

    public void setUpScrollbars() {
        int prevExtend = scrollBarH.getModel().getExtent();
        int extent = (int)Math.max(Math.round(100. / getZoomHandler().getZoomLevel()), 1);
        if (prevExtend > extent) {
            scrollBarH.getModel().setExtent(extent);
            scrollBarH.getModel().setMaximum(100 + extent);
            scrollBarV.getModel().setExtent(extent);
            scrollBarV.getModel().setMaximum(100 + extent);
        } else if (prevExtend < extent){
            scrollBarH.getModel().setMaximum(100 + extent);
            scrollBarH.getModel().setExtent(extent);
            scrollBarV.getModel().setMaximum(100 + extent);
            scrollBarV.getModel().setExtent(extent);
        }
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
        getZoomHandler().setShift(scrollBarH.getValue() / 100., scrollBarV.getValue() / 100.);
    }
}