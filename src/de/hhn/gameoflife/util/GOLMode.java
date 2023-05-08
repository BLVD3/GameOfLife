package de.hhn.gameoflife.util;

public enum GOLMode {
    STEP,
    RUN,
    DRAW,
    SET;

    @Override
    public String toString() {
        return switch (this) { //TODO String Resources
            case STEP -> "Schritt";
            case RUN -> "Laufen";
            case DRAW -> "Malen";
            case SET -> "Setzen";
        };
    }
}
