package de.hhn.gameoflife.view;

import java.awt.*;

public class BufferedImageZoom {
    private final Rectangle renderTarget;
    private final Dimension imageSize;
    private final Rectangle imagePosition;
    private float scale;
    private byte zoomLevel;
    private float xShift;
    private float yShift;

    private Rectangle sourceRect;
    private Rectangle targetRect;

    public BufferedImageZoom(Dimension targetSize, Dimension imageSize) {
        renderTarget = new Rectangle();
        this.imageSize = new Dimension();
        renderTarget.width = targetSize.width;
        renderTarget.height = targetSize.height;
        renderTarget.setBounds(0, 0, targetSize.width, targetSize.height);
        this.imageSize.width = imageSize.width;
        this.imageSize.height = imageSize.height;
        imagePosition = new Rectangle();
        setZoom(1);
        setShift(.5f, .5f);
        calculateAll();
    }

    public Rectangle getSourceRect() {
        return sourceRect;
    }

    public Rectangle getTargetRect() {
        return targetRect;
    }

    public Rectangle getImagePosition() {
        return imagePosition;
    }

    public byte getZoomLevel() {
        return zoomLevel;
    }

    public void resizeRenderPane(Dimension newSize) {
        renderTarget.width = newSize.width;
        renderTarget.height = newSize.height;
        calculateAll();
    }

    public void setShift(float x, float y) {
        xShift = x;
        yShift = y;
        calculateImagePosition();
        calculateRenderBounds();
    }

    public void setZoom(int level) {
        zoomLevel = (byte)Math.max(Math.min(level, 8),1);
        calculateAll();
    }

    private void calculateAll() {
        calculateImageScale();
        calculateImagePosition();
        calculateRenderBounds();
    }

    private void calculateImageScale() {
        float targetRatio = (float) renderTarget.width / renderTarget.height;
        float imageRatio = (float) imageSize.width / imageSize.height;
        scale = targetRatio > imageRatio ? (float) renderTarget.height / imageSize.height : (float) renderTarget.width / imageSize.width;
        scale *= zoomLevel;
        imagePosition.setSize(Math.round(imageSize.width * scale), Math.round(imageSize.height * scale));
    }

    private void calculateImagePosition() {
        imagePosition.x = Math.round((float) renderTarget.width / 2 - imagePosition.width * xShift);
        imagePosition.y = Math.round((float) renderTarget.height / 2 - imagePosition.height * yShift);
    }

    private void calculateRenderBounds() {
        targetRect = renderTarget.intersection(imagePosition);
        sourceRect = (Rectangle) targetRect.clone();
        sourceRect.x += renderTarget.x - imagePosition.x;
        sourceRect.y += renderTarget.y - imagePosition.y;
        sourceRect.x = Math.round(sourceRect.x / scale);
        sourceRect.y = Math.round(sourceRect.y / scale);
        sourceRect.height = Math.round(sourceRect.height / scale);
        sourceRect.width = Math.round(sourceRect.width / scale);
    }
}
