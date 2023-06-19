package de.hhn.gameoflife.control;

import de.hhn.gameoflife.model.GOLShape;
import de.hhn.gameoflife.util.ShapeIO;
import de.hhn.gameoflife.view.GOLShapeSelectorWindow;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class GOLShapeSelectorControl {
    private final GOLShapeSelectorWindow window;
    private final HashSet<GOLShape> shapes;

    public GOLShapeSelectorControl(GOLShapeSelectorWindow window) {
        this.window = window;
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
}
