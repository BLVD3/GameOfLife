package de.hhn.gameoflife.view;

import de.hhn.gameoflife.model.GOLCellArray;
import de.hhn.gameoflife.model.GOLShape;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GOLShapePanel extends JPanel {
    private static Border UNSELECTED_BORDER = BorderFactory.createLineBorder(new Color(0x000000));
    private static Border SELECTED_BORDER = BorderFactory.createLineBorder(new Color(0xFFFFFF));

    private final GOLShape shape;
    private final boolean justPreview;
    private final JButton selectButton;
    private final JButton deleteButton;

    private boolean selected;


    public GOLShapePanel(GOLShape shape, boolean justPreview) {
        this.justPreview = justPreview;
        selected = false;
        setLayout(new BorderLayout());
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(25, 50));
        setPreferredSize(new Dimension(200, 50));
        setBorder(UNSELECTED_BORDER);
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        if (shape == null) {
            this.shape = null;
            selectButton = null;
            deleteButton = null;
            leftPanel.add(new JLabel("No Shape"));
            return;
        }
        this.shape = shape;
        JLabel image = new JLabel(new ImageIcon(shape.getImage().getScaledInstance(38, 38, 0)));
        image.setPreferredSize(new Dimension(38, 38));
        leftPanel.add(image);
        leftPanel.add(new JLabel(shape.getName()));
        if (!justPreview) {
            selectButton = new JButton("+") {
                @Override
                protected void fireActionPerformed(ActionEvent event) {
                    if (!selected) {
                        super.fireActionPerformed(event);
                    }
                }
            };
            deleteButton = new JButton("-");
            deleteButton.setBackground(new Color(0xDA0000));
            rightPanel.add(selectButton, BorderLayout.PAGE_START);
            rightPanel.add(deleteButton, BorderLayout.PAGE_END);
        }
        else {
            selectButton = null;
            deleteButton = null;
        }
    }

    public void setSelected(boolean state) {
        selected = state;
        if (selected) {

            setBackground(UIManager.getColor("ScrollBar.thumb"));
            setBorder(SELECTED_BORDER);
        }
        else {
            setBackground(UIManager.getColor("Panel.background"));
            setBorder(UNSELECTED_BORDER);
        }
    }

    public GOLShape getShape() {
        return shape;
    }
}
