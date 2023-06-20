package de.hhn.gameoflife.util;

import de.hhn.gameoflife.model.GOLCellArray;
import de.hhn.gameoflife.model.GOLShape;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ShapeIO {
    public static final String SHAPE_PATH = "data/shapes";
    public static final String SHAPE_FILE_EXTENSION = ".gsh";

    private ShapeIO() { }

    public static void deleteShape(String shape) {
        File file = new File(SHAPE_PATH + "/" + shape + SHAPE_FILE_EXTENSION);
        if (file.exists())
            file.delete();
    }

    public static GOLShape[] loadShapes() {
        File folder = new File(SHAPE_PATH);
        File[] files = folder.listFiles();
        ArrayList<GOLShape> shapes = new ArrayList<>();
        if (files == null)
            return new GOLShape[0];
        for (File file : files) {
            if (!file.isDirectory() && file.getName().endsWith(SHAPE_FILE_EXTENSION)) {
                FileInputStream fStream = null;
                try {
                    fStream = new FileInputStream(file);
                    ObjectInputStream oStream = new ObjectInputStream(fStream);
                    GOLShape shape = (GOLShape) oStream.readObject();
                    shapes.add(shape);
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }
        return shapes.toArray(new GOLShape[0]);
    }

    public static GOLShape saveShape(GOLCellArray cellArray, String name) throws FileAlreadyExistsException {
        File file = new File(SHAPE_PATH + "/" + name + SHAPE_FILE_EXTENSION);
        if (file.exists()) {
            throw new FileAlreadyExistsException("A Shape named " + name + " already exists");
        }
        try {
            file.createNewFile();
            FileOutputStream stream = new FileOutputStream(file);
            ObjectOutputStream oStream = new ObjectOutputStream(stream);
            GOLShape shape = new GOLShape(name, cellArray);
            oStream.writeObject(shape);
            return shape;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static {
        Path directoryPath = Paths.get(SHAPE_PATH);

        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
