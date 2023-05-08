package de.hhn.gameoflife.view;

import de.hhn.gameoflife.GameOfLifeApplication;
import de.hhn.gameoflife.util.GOLMode;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    //Components
    JPanel topPanel;
    JButton newWindowButton;
    JButton shapeWindowButton;
    JComboBox<GOLMode> modeSelector;

    public MainWindow() {
        //Window Setup
        System.out.println("Setting up MainWindow");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 3 * 2, screenSize.height / 3 * 2);
        setLocation(100, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        //Component preparation
        modeSelector = new JComboBox<>(GOLMode.values());
        modeSelector.setSelectedItem(GameOfLifeApplication.getMode());
        modeSelector.addItemListener(e -> GameOfLifeApplication.setMode((GOLMode) e.getItem()));
        newWindowButton = new JButton("Neues GOLFenster"); //TODO String Resource
        newWindowButton.addActionListener(e -> newWindowButtonPressed());
        shapeWindowButton = new JButton("Figurenfenster anzeigen");
        shapeWindowButton.addActionListener(e -> shapeWindowButtonPressed());
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(68, 68, 68));
        topPanel.add(modeSelector);
        topPanel.add(newWindowButton);
        topPanel.add(shapeWindowButton);

        //Placement
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.PAGE_START);

        //Maximizing in the end so Components appear properly
        setExtendedState(MAXIMIZED_BOTH);
        System.out.println("MainWindow setup done!");
    }

    private void newWindowButtonPressed() {

    }

    private void shapeWindowButtonPressed() {

    }
}
