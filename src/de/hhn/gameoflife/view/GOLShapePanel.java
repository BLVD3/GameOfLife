package de.hhn.gameoflife.view;

import de.hhn.gameoflife.model.GOLCellArray;
import de.hhn.gameoflife.model.GOLShape;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

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
        JLabel image = new JLabel(new ImageIcon(scaleImage(shape.getImage())));
        image.setPreferredSize(new Dimension(38, 38));
        leftPanel.add(image);
        leftPanel.add(new JLabel(shape.getName()));
        if (!justPreview) {
            selectButton = new JButton(UIManager.getIcon("Tree.closedIcon")) {
                @Override
                protected void fireActionPerformed(ActionEvent event) {
                    if (!selected) {
                        super.fireActionPerformed(event);
                    }
                }
            };
            deleteButton = new JButton("X");
            deleteButton.setBackground(new Color(0xDA0000));
            rightPanel.add(selectButton, BorderLayout.PAGE_START);
            rightPanel.add(deleteButton, BorderLayout.PAGE_END);
        }
        else {
            selectButton = null;
            deleteButton = null;
        }
    }

    private Image scaleImage(BufferedImage image) {
        float ratio = (float) image.getWidth() / image.getHeight();
        if (ratio >= 1)
            return image.getScaledInstance(38, Math.round(38 / ratio), Image.SCALE_SMOOTH);
        return image.getScaledInstance(Math.round(38 * ratio), 38, Image.SCALE_SMOOTH);
    }

    public void setSelected(boolean state) {
        selected = state;
        if (selected) {
            selectButton.setIcon(UIManager.getIcon("Tree.openIcon"));
            setBackground(UIManager.getColor("ScrollBar.thumb"));
            setBorder(SELECTED_BORDER);
        }
        else {
            selectButton.setIcon(UIManager.getIcon("Tree.closedIcon"));
            setBackground(UIManager.getColor("Panel.background"));
            setBorder(UNSELECTED_BORDER);
        }
    }

    public GOLShape getShape() {
        return shape;
    }


}
