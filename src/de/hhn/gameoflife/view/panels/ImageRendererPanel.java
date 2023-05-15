package de.hhn.gameoflife.view.panels;

import de.hhn.gameoflife.util.ZoomChangedListener;
import de.hhn.gameoflife.util.ZoomHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

/**
 * Renders a BufferedImage on itself. Aspect ratio is considered during rendering.<br/>
 * Panel is also able to zoom into the image.
 */
public class ImageRendererPanel extends JPanel implements ComponentListener, ZoomChangedListener {
    private final BufferedImage image; // Image which will be drawn on the Panel
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

    @Override
    public void paint(Graphics g) {
        double scale = zoom.getScale();
        Image scaledImage = image.getScaledInstance((int) (image.getWidth() * scale),
                (int) (image.getHeight() * scale),
                Image.SCALE_FAST);
        g.setColor(new Color(0x333333));
        g.fillRect(0, 0, getWidth(), getHeight());
        Rectangle source = zoom.getSourceRect();
        Rectangle target = zoom.getTargetRect();
        g.drawImage(scaledImage,
                target.x, target.y,
                target.x + target.width,
                target.y + target.height,
                source.x, source.y,
                source.x + source.width,
                source.y + source.height,
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
    public void positionChanged() {
        repaint();
    }

    @Override
    public void scaleChanged() {

    }

    public ZoomHandler getZoomHandler() {
        return zoom;
    }
}
