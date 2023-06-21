package de.hhn.gameoflife.util;

public enum GOLMode {
    RUN,
    SET,
    DRAW,
    TOGGLE,
    SHAPES;

    @Override
    public String toString() {
        return switch (this) { //TODO String Resources
            case RUN -> "Laufen";
            case SET -> "Setzen";
            case DRAW -> "Malen";
            case TOGGLE -> "Umschalten";
            case SHAPES -> "Figuren";
        };
    }
}
