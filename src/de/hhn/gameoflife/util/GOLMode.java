package de.hhn.gameoflife.util;

/**
 * enum with all available modes
 * @author Nico Vogel
 * @version 1.0
 */
public enum GOLMode {
    RUN, // Constantly update
    SET, // Place alive cells
    DRAW, // Draw alive cells
    TOGGLE, // Toggle a cells between being alive and dead
    SHAPES; // Place shapes

    @Override
    public String toString() {
        return switch (this) {
            case RUN -> "Laufen";
            case SET -> "Setzen";
            case DRAW -> "Malen";
            case TOGGLE -> "Umschalten";
            case SHAPES -> "Figuren";
        };
    }
}
