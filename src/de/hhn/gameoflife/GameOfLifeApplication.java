package de.hhn.gameoflife;

import de.hhn.gameoflife.control.GOLMain;

import javax.swing.*;

public class GameOfLifeApplication {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarculaLaf");
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                 IllegalAccessException ignored) { }
        GOLMain.createInstance();
    }


}
