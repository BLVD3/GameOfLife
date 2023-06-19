package de.hhn.gameoflife.view;

import de.hhn.gameoflife.control.GOLShapeSelectorControl;
import de.hhn.gameoflife.model.GOLCellArray;
import de.hhn.gameoflife.model.GOLShape;
import de.hhn.gameoflife.util.ShapeIO;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GOLShapeSelectorWindow extends JInternalFrame {
    JScrollPane scrollPane;
    JPanel container;
    GridBagConstraints constraints;
    GOLShapeSelectorControl control;

    public GOLShapeSelectorWindow() {
        container = new JPanel();
        container.setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        scrollPane = new JScrollPane(container, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(250, 800));
        getContentPane().add(scrollPane);
        pack();
        setFrameIcon(null);
        setTitle("Figurwahl");
        setVisible(false);
        control = new GOLShapeSelectorControl(this);
    }

    public void addShape(GOLShape shape) {
        container.add(new GOLShapePanel(shape, false), constraints);
    }
}
