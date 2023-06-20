package de.hhn.gameoflife.control;

import de.hhn.gameoflife.model.GOLShape;
import de.hhn.gameoflife.util.GOLMode;
import de.hhn.gameoflife.util.listeners.GOLModeChangedListener;
import de.hhn.gameoflife.view.GOLMainWindow;

import java.util.ArrayList;
import java.util.List;

public class GOLMain {
    private static GOLMain instance;
    private volatile GOLMode mode;
    private final GOLMainWindow window;
    private final GOLShapeSelector shapeSelector;
    private GOLShape selectedShape;

    private GOLMain() {
        instance = this;
        mode = GOLMode.SET;
        selectedShape = GOLShape.DOT;
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

    public void setMode(GOLMode mode) {
        if (this.mode == mode)
            return;
        this.mode = mode;
        System.out.println("Switched Mode to " + mode);
    }

    public GOLShape getSelectedShape() {
        return selectedShape;
    }

    public GOLShapeSelector getShapeSelector() {
        return shapeSelector;
    }

    public void setSelectedShape(GOLShape shape) {
        selectedShape = shape;
        window.setShapePreview(shape);
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
}
