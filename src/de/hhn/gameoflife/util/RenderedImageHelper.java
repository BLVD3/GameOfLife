package de.hhn.gameoflife.util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RenderedImageHelper {
    private RenderedImageHelper() {}

    public static void fillRenderedImage(BufferedImage image, Color color) {
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
        g.dispose();
    }

    public static void fillRenderedImage(BufferedImage image, Color color, Graphics2D g) {
        g.setColor(color);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());
    }
}
