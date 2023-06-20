package de.hhn.gameoflife.control;

import de.hhn.gameoflife.model.GOLShape;
import de.hhn.gameoflife.util.ShapeIO;
import de.hhn.gameoflife.util.listeners.GOLModeChangedListener;
import de.hhn.gameoflife.view.GOLShapeSelectorWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class GOLShapeSelector {
    private static final List<GOLModeChangedListener> MODE_CHANGED_LISTENERS = new ArrayList<>();
    private final GOLShapeSelectorWindow window;
    private final HashSet<GOLShape> shapes;

    public GOLShapeSelector() {
        this.window = new GOLShapeSelectorWindow(this);
        shapes = new HashSet<>();
        Arrays.stream(ShapeIO.loadShapes()).forEach(this::addShape);
    }

    public GOLShape[] getShapes() {
        return shapes.toArray(new GOLShape[0]);
    }

    public void deleteShape(GOLShape shape) {
        ShapeIO.deleteShape(shape.getName());
    }

    public void selectShape(GOLShape shape) {

    }

    public void addShape(GOLShape shape) {
        if (shapes.add(shape))
            window.addShape(shape);
    }

    public boolean toggleVisibility() {
        window.setVisible(!window.isVisible());
        return window.isVisible();
    }

    public GOLShapeSelectorWindow getWindow() {
        return window;
    }

    public void shapeSelected(GOLShape shape) {
        if (shapes.contains(shape)) {
            window.setSelectedShape(shape);
            GOLMain.getInstance().setSelectedShape(shape);
        }
    }

    public void shapeDeleteButtonPressed(GOLShape shape) {
        if (shapes.contains(shape)) {
            shapes.remove(shape);
            window.removeShape(shape);
            if (GOLMain.getInstance().getSelectedShape() == shape) {
                GOLMain.getInstance().setSelectedShape(GOLShape.DOT);
            }
            ShapeIO.deleteShape(shape.getName());
        }
    }
}
