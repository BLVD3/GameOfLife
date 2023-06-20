package de.hhn.gameoflife.view;

import de.hhn.gameoflife.control.GOLShapeSelector;
import de.hhn.gameoflife.model.GOLShape;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class GOLShapeSelectorWindow extends JInternalFrame {
    JScrollPane scrollPane;
    JPanel container;
    GridBagConstraints constraints;
    GOLShapeSelector control;

    HashMap<GOLShape, GOLShapePanel> shapes;
    GOLShape currentlySelected;

    public GOLShapeSelectorWindow(GOLShapeSelector control) {
        this.control = control;
        container = new JPanel();
        shapes = new HashMap<>();
        currentlySelected = null;
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
        if (!shapes.containsKey(shape)) {
            GOLShapePanel panel = new GOLShapePanel(shape, false);
            shapes.put(shape, panel);
            container.add(panel, constraints);
        }
    }

    public void removeShape(GOLShape shape) {
        if (shapes.containsKey(shape)) {
            container.remove(shapes.get(shape));
            shapes.remove(shape);
        }
    }

    public void setSelectedShape(GOLShape shape) {
        if (shapes.containsKey(shape)) {
            if (currentlySelected != null)
                shapes.get(currentlySelected).setSelected(false);
            shapes.get(shape).setSelected(true);
            currentlySelected = shape;
        }
    }
}
