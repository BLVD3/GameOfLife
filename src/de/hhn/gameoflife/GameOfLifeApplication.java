package de.hhn.gameoflife;

import de.hhn.gameoflife.control.GOLMain;

import javax.swing.*;

/**
 * Class containing the main method
 * @author Nico Vogel
 * @version 1.0
 */
public class GameOfLifeApplication {

    public static void main(String[] args) {
        try {
            //This line attempts to load another look and feel
            //This method of loading the laf ensures that the application still works in case adding the library
            //  doesn't work
            //In case the application looks "ugly" try to add "./extern/flatlaf-3.1.1-jar" as a library
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarculaLaf");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException ignored) { }
        GOLMain.createInstance();
    }
}
