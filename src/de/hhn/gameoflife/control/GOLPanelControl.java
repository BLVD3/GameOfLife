package de.hhn.gameoflife.control;

import de.hhn.gameoflife.model.GameOfLife;
import de.hhn.gameoflife.view.GOLSimulationPanel;
import de.hhn.gameoflife.view.GOLWindow;

public class GOLPanelControl {
    GOLWindow window;
    GOLSimulationPanel panel;
    GameOfLife gol;
    Thread thread;

    public GOLPanelControl(GOLWindow window, GOLSimulationPanel panel) {

    }
}
