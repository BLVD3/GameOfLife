package de.hhn.gameoflife.control;

import de.hhn.gameoflife.model.GOLShape;
import de.hhn.gameoflife.view.GOLShapeSelectorWindow;

import java.io.*;
import java.util.HashSet;

public class GOLShapeSelectorControl {
    public static final String SHAPE_PATH = "data/shapes";
    public static final String SHAPE_FILE_EXTENSION = ".gsh";

    private final GOLShapeSelectorWindow window;
    private final HashSet<GOLShape> shapes;

    public GOLShapeSelectorControl(GOLShapeSelectorWindow window) {
        this.window = window;
        shapes = new HashSet<>();
    }

    public void loadShapes() {
        File folder = new File(SHAPE_PATH);
        File[] files = folder.listFiles();
        if (files == null)
            return;
        for (File file : files) {
            if (!file.isDirectory() && file.getName().endsWith(SHAPE_FILE_EXTENSION)) {
                FileInputStream fStream = null;
                try {
                    fStream = new FileInputStream(file);
                    ObjectInputStream oStream = new ObjectInputStream(fStream);
                    GOLShape shape = (GOLShape) oStream.readObject();
                    shapes.add(shape);
                } catch (Exception ignored) { }
            }
        }
    }

    public HashSet<GOLShape> getShapes() {
        return (HashSet<GOLShape>) shapes.clone();
    }
}
