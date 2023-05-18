package de.hhn.gameoflife.view;

import de.hhn.gameoflife.util.ZoomChangedListener;
import de.hhn.gameoflife.util.ZoomHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Renders a BufferedImage on itself. Aspect ratio is considered during rendering.<br/>
 * Panel is also able to zoom into the image.
 */
public class ImageRendererPanel extends JPanel implements ComponentListener, ZoomChangedListener {
    private BufferedImage image; // Image which will be drawn on the Panel
    private final ZoomHandler zoom; // Helper class to calculate Size, Zoom and Position of the Image

    public BufferedImage getImage() {
        return image;
    }

    public ImageRendererPanel(BufferedImage image) {
        this.image = image;
        zoom = new ZoomHandler(new Dimension(getWidth(), getHeight()), new Dimension(image.getWidth(null), image.getHeight(null)));
        zoom.addListener(this);
        addComponentListener(this);
    }

    public void swapImage(BufferedImage image) {
        this.image = image;
        zoom.resizeImage(new Dimension(image.getWidth(), image.getHeight()));
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(0x333333));
        g.fillRect(0, 0, getWidth(), getHeight());
        Rectangle2D.Double rect =  zoom.getImagePosition();
        g.drawImage(image, (int)(rect.x), (int)(rect.y),
                (int)(rect.x + rect.width),
                (int)(rect.y + rect.height),
                0, 0,
                image.getWidth(),
                image.getHeight(),
                null);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        zoom.resizeRenderTarget(getSize());
        repaint();
    }

    @Override
    public void componentMoved(ComponentEvent e) { }

    @Override
    public void componentShown(ComponentEvent e) { }

    @Override
    public void componentHidden(ComponentEvent e) { }

    @Override
    public void positionChanged(double newX, double newY) {
        repaint();
    }

    @Override
    public void scaleChanged(double newZoom) { }

    public ZoomHandler getZoomHandler() {
        return zoom;
    }
}
