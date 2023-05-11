package de.hhn.gameoflife.control;

import java.awt.*;

/**
 * Class to make zooming in on a Buffered Image easier
 * Could maybe be refactored to incorporate Matrix multiplication
 * @author Nico Vogel
 * @version 1.0
 */
public class BufferedImageZoom {
    private final Rectangle renderRect; // The Bounds of the Render Target
    private final Dimension imageSize; // The Size of the Buffered Image
    private final Rectangle imagePosition; // The Position of the Image after Zooming/Shifting
    private float scale; // The Scale of the image: imageSize -> imagePosition.size
    private float zoomLevel; // The Target zoom level
    private float xShift, yShift; // The Position of the Image -> 0, 0 top Left corner centered; 1, 1 bottom right

    private Rectangle sourceRect; // Area on the image that should get Rendered
    private Rectangle targetRect; // Area on the Target Container that will be rendered on

    /**
     * @param targetSize Size of the Container that will be rendered on
     * @param imageSize <b>!Resolution!</b> of the Image that will be Rendered
     */
    public BufferedImageZoom(Dimension targetSize, Dimension imageSize) {
        renderRect = new Rectangle(targetSize);
        this.imageSize = (Dimension)imageSize.clone();
        imagePosition = new Rectangle();
        setZoom(1f);
        setShift(.5f, .5f);
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
     * @return scaled position of the Image
     */
    public Rectangle getImagePosition() {
        return imagePosition;
    }

    /**
     * @return current zoom level
     */
    public float getZoomLevel() {
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
    public void setShift(float x, float y) {
        xShift = x;
        yShift = y;
        calculateImagePosition();
        calculateRenderBounds();
    }

    /**
     * Sets the current zoom level
     */
    public void setZoom(float level) {
        if (level <= 1.0f) {
            reset();
            return;
        }
        zoomLevel = Math.max(Math.min(level, 8f), 1f);
        calculateAll();
    }

    /**
     * Sets zoom level to 1 and centers the image
     */
    public void reset() {
        xShift = .5f;
        yShift = .5f;
        zoomLevel = 1.0f;
        calculateAll();
    }

    /**
     * Moves the image from its current position
     * @param x Horizontal Movement
     * @param y Vertical Movement
     */
    public void moveImage(float x, float y) {
        xShift = Math.max(Math.min(x + xShift, 1f), 0f);
        yShift = Math.max(Math.min(y + yShift, 1f), 0f);
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
        float targetRatio = (float) renderRect.width / renderRect.height;
        float imageRatio = (float) imageSize.width / imageSize.height;
        scale = targetRatio > imageRatio ? (float) renderRect.height / imageSize.height : (float) renderRect.width / imageSize.width;
        scale *= zoomLevel;
        imagePosition.setSize(Math.round(imageSize.width * scale), Math.round(imageSize.height * scale));
    }

    /**
     * Repositions the pseudo image
     */
    private void calculateImagePosition() {
        imagePosition.x = Math.round((float) renderRect.width / 2 - imagePosition.width * xShift);
        imagePosition.y = Math.round((float) renderRect.height / 2 - imagePosition.height * yShift);
    }

    /**
     * Calculates the Intersections between the pseudo image and the rendering target
     */
    private void calculateRenderBounds() {
        targetRect = renderRect.intersection(imagePosition);
        sourceRect = (Rectangle) targetRect.clone();
        sourceRect.x = Math.round((sourceRect.x - imagePosition.x) / scale);
        sourceRect.y = Math.round((sourceRect.y - imagePosition.y) / scale);
        sourceRect.height = Math.round(sourceRect.height / scale);
        sourceRect.width = Math.round(sourceRect.width / scale);
    }
}
