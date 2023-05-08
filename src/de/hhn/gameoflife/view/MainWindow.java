package de.hhn.gameoflife.view;

import de.hhn.gameoflife.GameOfLifeApplication;
import de.hhn.gameoflife.util.GOLMode;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    //Components
    JPanel topPanel;
    JPanel bottomPanel;
    JButton newWindowButton;
    JButton shapeWindowButton;
    JComboBox<GOLMode> modeSelector;
    GOLWindowDialog newWindowDialog;

    public MainWindow() {
        //Window Setup
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 3 * 2, screenSize.height / 3 * 2);
        setLocation(100, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Component preparation
        modeSelector = new JComboBox<>(GOLMode.values());
        modeSelector.setSelectedItem(GameOfLifeApplication.getMode());
        modeSelector.addItemListener(e -> GameOfLifeApplication.setMode((GOLMode) e.getItem()));
        newWindowButton = new JButton("Neues GOLFenster"); //TODO String Resource
        newWindowButton.addActionListener(e -> newWindowButtonPressed());
        newWindowDialog = new GOLWindowDialog(this);
        shapeWindowButton = new JButton("Figurenfenster anzeigen");
        shapeWindowButton.addActionListener(e -> shapeWindowButtonPressed());
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(68, 68, 68));
        topPanel.add(modeSelector);
        topPanel.add(newWindowButton);
        topPanel.add(shapeWindowButton);
        bottomPanel = new JPanel();
        bottomPanel.setLayout(null);
        bottomPanel.setBackground(new Color(112, 112, 112));
        bottomPanel.add(newWindowDialog);


        //Placement
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.PAGE_START);
        add(bottomPanel, BorderLayout.CENTER);

        //Maximizing in the end so Components appear properly
        setExtendedState(MAXIMIZED_BOTH);
        setVisible(true);
    }

    private void newWindowButtonPressed() {
        newWindowDialog.setVisible(true);
    }

    private void shapeWindowButtonPressed() {

    }

    public void addWindow(int width, int height) {
        bottomPanel.add(new GOLWindow(width, height, bottomPanel));
    }
}
