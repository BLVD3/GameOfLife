package de.hhn.gameoflife.view;

import javax.swing.*;
import java.awt.*;

public class GOLWindow extends JInternalFrame {
    private volatile int waitTime;
    private volatile Color aliveColor;
    private volatile Color deadColor;

    public GOLWindow(int width, int height) {

    }

    public int getWaitTime() {
        return waitTime;
    }

    public Color getAliveColor() {
        return aliveColor;
    }

    public Color getDeadColor() {
        return deadColor;
    }
}
