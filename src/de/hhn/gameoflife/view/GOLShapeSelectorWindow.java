package de.hhn.gameoflife.view;

import de.hhn.gameoflife.control.GOLShapeSelector;
import de.hhn.gameoflife.model.GOLShape;

import javax.swing.*;
import java.awt.*;

public class GOLShapeSelectorWindow extends JInternalFrame {
    JScrollPane scrollPane;
    JPanel container;
    GridBagConstraints constraints;
    GOLShapeSelector control;

    public GOLShapeSelectorWindow(GOLShapeSelector control) {
        this.control = control;
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
    }

    public void addShape(GOLShape shape) {
        container.add(new GOLShapePanel(shape, false), constraints);
    }
}
