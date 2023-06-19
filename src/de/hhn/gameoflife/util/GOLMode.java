package de.hhn.gameoflife.util;

public enum GOLMode {
    RUN,
    DRAW,
    SET,
    SHAPES;

    @Override
    public String toString() {
        return switch (this) { //TODO String Resources
            case RUN -> "Laufen";
            case DRAW -> "Malen";
            case SET -> "Setzen";
            case SHAPES -> "Figuren";
        };
    }
}
