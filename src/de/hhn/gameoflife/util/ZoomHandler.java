package de.hhn.gameoflife.util;

import de.hhn.gameoflife.util.listeners.ZoomChangedListener;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;

/**
 * Class to make zooming in on a Buffered Image easier
 * Could maybe be refactored to incorporate Matrix multiplication
 * @author Nico Vogel
 * @version 1.0
 */
public class ZoomHandler {
    private final Rectangle2D.Double renderRect; // The Bounds of the Render Target
    private final Dimension imageSize; // The Size of the Buffered Image
    private final Rectangle2D.Double imagePosition; // The Position of the Image after Zooming/Shifting
    private double scale; // The Scale of the image: imageSize -> imagePosition.size
    private double zoomLevel; // The Target zoom level
    private double xShift, yShift; // The Position of the Image -> 0, 0 top Left corner centered; 1, 1 bottom right
    private final HashSet<ZoomChangedListener> listeners;

    /**
     * @param targetSize Size of the Container that will be rendered on
     * @param imageSize <b>!Resolution!</b> of the Image that will be Rendered
     */
    public ZoomHandler(Dimension targetSize, Dimension imageSize) {
        renderRect = new Rectangle2D.Double(0, 0, targetSize.width, targetSize.height);
        this.imageSize = (Dimension)imageSize.clone();
        imagePosition = new Rectangle2D.Double();
        listeners = new HashSet<>(1);
        setZoom(1.);
        setShift(.5, .5);
        calculateAll();
    }

    public void resizeImage(Dimension size) {
        imageSize.width = size.width;
        imageSize.height = size.height;
        calculateAll();
    }

    /**
     * @return current zoom level
     */
    public double getZoomLevel() {
        return zoomLevel;
    }

    /**
     * @return the factor with which the image was resized
     */
    public double getScale() {
        return scale;
    }

    public double getXShift() {
        return xShift;
    }

    public double getYShift() {
        return yShift;
    }

    public Rectangle2D.Double getImagePosition() {
        return imagePosition;
    }

    /**
     * Sets the new Size of the RenderTarget <br/>
     * Also recalculates rendering bounds
     */
    public void resizeRenderTarget(Dimension newSize) {
        renderRect.width = newSize.width;
        renderRect.height = newSize.height;
        calculateAll();
    }

    /**
     * Sets the position.<br/>
     * Set both values to 0.5 to make it centered.
     */
    public void setShift(double x, double y) {
        xShift = Math.max(Math.min(x, 1.), 0.);
        yShift = Math.max(Math.min(y, 1.), 0.);
        calculateImagePosition();
    }


    /**
     * Moves the image from its current position. The distance is relative to the screen.
     * @param x Horizontal change
     * @param y Vertical change
     */
    public void setShiftDeltaRelative(double x, double y) {
        setShift(xShift + x / zoomLevel, yShift + y / zoomLevel);
    }

    /**
     * Moves the image from its current position. The distance is not relative to the screen.
     * @param x Horizontal change
     * @param y Vertical change
     */
    public void setShiftDeltaAbsolute(double x, double y) {
        setShift(xShift + x, yShift + y);
    }

    /**
     * Sets the current zoom level
     */
    public void setZoom(double level) {
        if (level <= 1.0) {
            reset();
            return;
        }
        zoomLevel = Math.max(level, 1);
        calculateAll();
    }

    public void setZoomDelta(double delta) {
        setZoom(zoomLevel + delta);
    }

    public void scaleZoom(double factor) {
        setZoom(zoomLevel * factor);
    }

    /**
     * Sets zoom level to 1 and centers the image
     */
    public void reset() {
        xShift = .5;
        yShift = .5;
        zoomLevel = 1.;
        calculateAll();
    }

    /**
     * Recalculates everything<br/>
     * Use sparingly because it uses a lot of division
     */
    private void calculateAll() {
        calculateImageScale();
        calculateImagePosition();
    }

    /**
     * Rescales the pseudo image
     */
    private void calculateImageScale() {
        double targetRatio = renderRect.width / renderRect.height;
        double imageRatio = (double) imageSize.width / imageSize.height;
        scale = targetRatio > imageRatio ? renderRect.height / imageSize.height : renderRect.width / imageSize.width;
        scale *= zoomLevel;
        imagePosition.width = imageSize.width * scale;
        imagePosition.height = imageSize.height * scale;
        fireScaleChangedEvent();
    }

    /**
     * Repositions the pseudo image
     */
    private void calculateImagePosition() {
        imagePosition.x = renderRect.width / 2.f - imagePosition.width * xShift;
        imagePosition.y = renderRect.height / 2.f - imagePosition.height * yShift;
        firePositionChangedEvent();
    }

    public void addListener(ZoomChangedListener listener) {
        if (listeners.contains(listener))
            return;
        listeners.add(listener);
    }

    public void removeListener(ZoomChangedListener listener) {
        listeners.remove(listener);
    }


    private void firePositionChangedEvent() {
        listeners.forEach(listener -> listener.positionChanged(xShift, yShift));
    }

    private void fireScaleChangedEvent() {
        listeners.forEach(listener -> listener.scaleChanged(zoomLevel));
    }

    public Point transformToImageCoordinate(int x, int y) {
        if (!imagePosition.contains(new Point(x, y)))
            return null;
        Point val = new Point();
        val.x = (int)((x - imagePosition.x) / scale);
        val.y = (int)((y - imagePosition.y) / scale);
        return val;
    }
}
