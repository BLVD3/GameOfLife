package de.hhn.gameoflife.view;

import de.hhn.gameoflife.GameOfLifeApplication;
import de.hhn.gameoflife.model.GameOfLife;
import de.hhn.gameoflife.util.GOLCellChangedListener;
import de.hhn.gameoflife.util.GOLMode;
import de.hhn.gameoflife.util.GOLModeChangedListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;

import static de.hhn.gameoflife.util.RenderedImageHelper.fillRenderedImage;

/**
 * Renders a BufferedImage on itself. Aspect ratio is considered during rendering.<br/>
 * Panel is also able to zoom into the image.
 */
public class BufferedImageRendererPanel extends JPanel implements ComponentListener {
    private final BufferedImage buffer; // Image which will be drawn on the Panel
    private final BufferedImageZoom zoom; // Helper class to calculate Size, Zoom and Position of the Image

    public BufferedImageRendererPanel(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Width and/or height below 0. Width: " + width + " Height: " + height);
        }

        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        zoom = new BufferedImageZoom(new Dimension(getWidth(), getHeight()), new Dimension(buffer.getWidth(), buffer.getHeight()));

        addComponentListener(this);
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(new Color(0x333333));
        g.fillRect(0, 0, getWidth(), getHeight());
        Rectangle source = zoom.getSourceRect();
        Rectangle target = zoom.getTargetRect();
        g.drawImage(buffer,
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

    public void setZoom(float zoomLevel) {
        zoom.setZoom(zoomLevel);
        repaint();
    }

    public void setZoomDelta(float delta) {
        zoom.setZoom(zoom.getZoomLevel() + delta);
        repaint();
    }

    public void setShift(float shiftX, float shiftY) {
        zoom.setShift(shiftX, shiftY);
        repaint();
    }

    public void setShiftDelta(float x, float y) {
        zoom.moveImage(x, y);
        repaint();
    }

    public void setPixelNoRepaint(int x, int y, int rgb) {
        buffer.setRGB(x, y, rgb);
    }

    public void setPixel(int x, int y, int rgb) {
        buffer.setRGB(x, y, rgb);
        repaint();
    }

    public float getZoom() {
        return zoom.getZoomLevel();
    }
}
