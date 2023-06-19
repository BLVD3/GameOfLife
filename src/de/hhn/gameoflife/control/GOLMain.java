package de.hhn.gameoflife.control;

import de.hhn.gameoflife.util.GOLMode;
import de.hhn.gameoflife.util.listeners.GOLModeChangedListener;
import de.hhn.gameoflife.view.GOLMainWindow;

import java.util.ArrayList;
import java.util.List;

public class GOLMain {
    private final List<GOLModeChangedListener> modeChangedListeners = new ArrayList<>();
    private static GOLMain instance;
    private volatile GOLMode mode;
    private final GOLMainWindow window;
    private final GOLShapeSelector shapeSelector;

    private GOLMain() {
        instance = this;
        mode = GOLMode.SET;
        window = new GOLMainWindow(this);
        shapeSelector = new GOLShapeSelector();
        window.addInternalFrame(shapeSelector.getWindow());
    }

    public static GOLMain getInstance() {
        return instance;
    }

    public GOLMode getMode() {
        return mode;
    }

    public static void createInstance() {
        if (instance == null)
            instance = new GOLMain();
    }

    public void addGOLWindow(int width, int height) {
        GOLWindowControl golControl = new GOLWindowControl(width, height);
        window.addInternalFrame(golControl.getWindow());
    }

    public void shapeWindowButtonPressed() {
        shapeSelector.toggleVisibility();
    }
    public void setMode(GOLMode mode) {
        if (this.mode == mode)
            return;
        this.mode = mode;
        System.out.println("Switched Mode to " + mode);
        fireModeChanged();
    }

    private void fireModeChanged() {
        for (GOLModeChangedListener listener : modeChangedListeners) {
            listener.modeChangedEvent(mode);
        }
    }

    public void addListener(GOLModeChangedListener listener) {
        modeChangedListeners.add(listener);
    }

    public void removeListener(GOLModeChangedListener listener) {
        modeChangedListeners.remove(listener);
    }
}
