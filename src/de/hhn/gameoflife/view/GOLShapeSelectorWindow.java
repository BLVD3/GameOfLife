package de.hhn.gameoflife.view;

import de.hhn.gameoflife.model.GOLCellArray;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GOLShapeSelectorWindow extends JInternalFrame {
    JScrollPane scrollPane;
    JPanel container;
    GridBagConstraints constraints;

    public GOLShapeSelectorWindow() {
        container = new JPanel();
        container.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        scrollPane = new JScrollPane(container, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(200, 800));
        getContentPane().add(scrollPane);
        pack();
        setFrameIcon(null);
        setTitle("Figurwahl");
        setVisible(false);
    }
}
