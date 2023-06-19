package de.hhn.gameoflife.util;

import de.hhn.gameoflife.util.listeners.PixelListener;

public class PixelStreams {
    /**
     * Bersenham Algorithm
     * @param x0 start x
     * @param y0 start y
     * @param x1 end x
     * @param y1 end y
     */
    public static void forEachPixelInLine(int x0, int y0, int x1, int y1, PixelListener listener) {
        if (Math.abs(y1 - y0) < Math.abs(x1 - x0)) {
            if (x0 > x1)
                lineLow(x1, y1, x0, y0, listener);
            else
                lineLow(x0, y0, x1, y1, listener);
        }
        else {
            if (y0 > y1)
                lineHigh(x1, y1, x0, y0, listener);
            else
                lineHigh(x0, y0, x1, y1, listener);
        }
    }

    private static void lineLow(int x0, int y0, int x1, int y1, PixelListener listener) {
        int dx = x1 - x0;
        int dy = y1 - y0;
        int yi = 1;
        if (dy < 0) {
            yi = -1;
            dy = -dy;
        }
        int d = 2 * dy - dx;
        int y = y0;

        for (int x = x0; x <= x1; x += dx / Math.abs(dx)) {
            listener.pixelEvent(x, y);
            if (d > 0) {
                y = y + yi;
                d = d + (2 * (dy - dx));
            }
            else
                d = d + 2 * dy;
        }
    }

    private static void lineHigh(int x0, int y0, int x1, int y1, PixelListener listener) {
        int dx = x1 - x0;
        int dy = y1 - y0;
        int xi = 1;
        if (dx < 0) {
            xi = -1;
            dx = -dx;
        }
        int d = 2 * dx - dy;
        int x = x0;

        for (int y = y0; y <= y1; y += dy / Math.abs(dy)) {
            listener.pixelEvent(x, y);
            if (d > 0) {
                x = x + xi;
                d = d + (2 * (dx - dy));
            }
            else
                d = d + 2 * dx;
        }
    }
}
