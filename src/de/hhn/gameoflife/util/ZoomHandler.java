package de.hhn.gameoflife.util;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
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

    private final Rectangle sourceRect; // Area on the image that should get Rendered
    private final Rectangle targetRect; // Area on the Target Container that will be rendered on

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
        targetRect = new Rectangle();
        sourceRect = new Rectangle();
        setZoom(1.);
        setShift(.5, .5);
        calculateAll();
    }

    /**
     * @return the area of the BufferedImage that shall be rendered
     */
    public Rectangle getSourceRect() {
        return sourceRect;
    }

    /**
     * @return the area on the Container that shall be rendered on
     */
    public Rectangle getTargetRect() {
        return targetRect;
    }

    /**
     * @return current zoom level
     */
    public double getZoomLevel() {
        return zoomLevel;
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
     * Sets the shifted position of the image.<br/>
     * Set both values to 0.5 to make it centered.
     */
    public void setShift(double x, double y) {
        xShift = Math.max(Math.min(x, 1.), 0.);
        yShift = Math.max(Math.min(y, 1.), 0.);
        calculateImagePosition();
        calculateRenderBounds();
    }


    public void setShiftDeltaRelative(double x, double y) {
        setShift(xShift + x / zoomLevel, yShift + y / zoomLevel);
    }

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
     * Moves the image from its current position
     * @param x Horizontal Movement
     * @param y Vertical Movement
     */
    public void moveImage(double x, double y) {
        xShift = Math.max(Math.min(x + xShift, 1), 0);
        yShift = Math.max(Math.min(y + yShift, 1), 0);
        calculateImagePosition();
        calculateRenderBounds();
    }

    /**
     * Recalculates everything<br/>
     * Use sparingly because it uses a lot of division
     */
    private void calculateAll() {
        calculateImageScale();
        calculateImagePosition();
        calculateRenderBounds();
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
    }

    /**
     * Repositions the pseudo image
     */
    private void calculateImagePosition() {
        imagePosition.x = renderRect.width / 2.f - imagePosition.width * xShift;
        imagePosition.y = renderRect.height / 2.f - imagePosition.height * yShift;
    }

    /**
     * Calculates the Intersections between the pseudo image and the rendering target
     */
    private void calculateRenderBounds() {
        Rectangle2D.Double intersection = (Rectangle2D.Double) renderRect.createIntersection(imagePosition);
        Rectangle2D.Double visibleImageArea = (Rectangle2D.Double) intersection.clone();
        visibleImageArea.x -= imagePosition.x;
        visibleImageArea.y -= imagePosition.y;
        visibleImageArea.x /= scale;
        visibleImageArea.y /= scale;
        visibleImageArea.width /= scale;
        visibleImageArea.height /= scale;
        targetRect.setBounds(
                (int)Math.round(intersection.x),
                (int)Math.round(intersection.y),
                (int)Math.round(intersection.width),
                (int)Math.round(intersection.height));
        sourceRect.setBounds(
                (int)Math.round(visibleImageArea.x),
                (int)Math.round(visibleImageArea.y),
                (int)Math.round(visibleImageArea.width),
                (int)Math.round(visibleImageArea.height));
        fireZoomChangedEvent();
    }

    public void addListener(ZoomChangedListener listener) {
        if (listeners.contains(listener))
            return;
        listeners.add(listener);
    }

    public void removeListener(ZoomChangedListener listener) {
        listeners.remove(listener);
    }


    private void fireZoomChangedEvent() {
        listeners.forEach(ZoomChangedListener::zoomChanged);
    }

    public Point transformToImageCoordinate(int x, int y) {
        Point val = new Point();
        val.x = (int)((x - imagePosition.x) / scale);
        val.y = (int)((x - imagePosition.x) / scale);
        return val;
    }
}
