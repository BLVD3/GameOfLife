package de.hhn.gameoflife.control;

import de.hhn.gameoflife.model.GOLShape;
import de.hhn.gameoflife.util.ShapeIO;
import de.hhn.gameoflife.view.GOLShapeSelectorWindow;

import java.util.HashSet;
import java.util.List;

public class GOLShapeSelectorControl {
    private final GOLShapeSelectorWindow window;
    private final HashSet<GOLShape> shapes;

    public GOLShapeSelectorControl(GOLShapeSelectorWindow window) {
        this.window = window;
        shapes = new HashSet<>();
        shapes.addAll(List.of((ShapeIO.loadShapes())));
        shapes.forEach(window::addShape);
    }

    public GOLShape[] getShapes() {
        return shapes.toArray(new GOLShape[0]);
    }

    public void deleteShape(GOLShape shape) {

    }

    public void selectShape(GOLShape shape) {

    }

    public void addShape(GOLShape shape) {

    }
}
