package de.hhn.gameoflife;

import de.hhn.gameoflife.util.GOLMode;
import de.hhn.gameoflife.util.GOLModeChangedListener;
import de.hhn.gameoflife.view.GOLWindowDialog;
import de.hhn.gameoflife.view.MainWindow;
import de.hhn.gameoflife.view.RenderTest;

import java.util.ArrayList;
import java.util.List;

public class GameOfLifeApplication {
    private static final List<GOLModeChangedListener> MODE_CHANGED_LISTENERS = new ArrayList<>();
    private static final Object lock = new Object();
    private static GOLMode mode = GOLMode.SET;

    public static void main(String[] args) {
        new MainWindow();
    }

    public static void setMode(GOLMode mode) {
        if (GameOfLifeApplication.mode == mode)
            return;
        GameOfLifeApplication.mode = mode; // TODO Threading logic?
        System.out.println("Switched Mode to " + mode);
        fireModeChanged();
    }

    public static GOLMode getMode() {
        return mode;
    }

    private static void fireModeChanged() {
        for (GOLModeChangedListener listener : MODE_CHANGED_LISTENERS) {
            listener.modeChangedEvent(mode);
        }

    }

    public static void addListener(GOLModeChangedListener listener) {
        MODE_CHANGED_LISTENERS.add(listener);
    }

    public static void removeListener(GOLModeChangedListener listener) {
        if (MODE_CHANGED_LISTENERS.contains(listener))
            removeListener(listener);
    }
}