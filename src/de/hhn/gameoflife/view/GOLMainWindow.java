package de.hhn.gameoflife.view;

import de.hhn.gameoflife.control.GOLMain;
import de.hhn.gameoflife.model.GOLShape;
import de.hhn.gameoflife.util.GOLMode;

import javax.swing.*;
import java.awt.*;

/**
 * Main Window of the application
 * @author Nico Vogel 215998
 * @version 1.0
 */
public class GOLMainWindow extends JFrame {
    GOLMain control;

    //Components
    JPanel topPanel;
    JDesktopPane bottomPanel;
    JButton newWindowButton;
    JButton shapeWindowButton;
    JComboBox<GOLMode> modeSelector;
    GOLWindowDialog newWindowDialog;
    JPanel shapePreviewPanel;

    public GOLMainWindow(GOLMain control) {
        this.control = control;
        //Window Setup
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width / 3 * 2, screenSize.height / 3 * 2);
        setLocation(100, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //Component preparation
        modeSelector = new JComboBox<>(GOLMode.values());
        modeSelector.setSelectedItem(GOLMain.getInstance().getMode());
        modeSelector.addItemListener(e -> GOLMain.getInstance().setMode((GOLMode) e.getItem()));
        newWindowButton = new JButton("Neues GOLFenster"); //TODO String Resource
        newWindowButton.addActionListener(e -> newWindowButtonPressed());
        newWindowDialog = new GOLWindowDialog(this);
        shapeWindowButton = new JButton("Sichtbarkeit Figurenfenster umschalten");
        shapeWindowButton.addActionListener(e -> control.shapeWindowButtonPressed());
        shapePreviewPanel = new JPanel();
        shapePreviewPanel.setLayout(new BorderLayout());
        shapePreviewPanel.setPreferredSize(new Dimension(200, 50));
        setShapePreview(GOLShape.DOT);
        topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(modeSelector);
        topPanel.add(newWindowButton);
        topPanel.add(shapeWindowButton);
        topPanel.add(shapePreviewPanel);
        bottomPanel = new JDesktopPane();
        bottomPanel.setLayout(null);
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

    /**
     * Adds a InternalFrame to the DesktopPane
     */
    public void addInternalFrame(JInternalFrame frame) {
        if (frame != null)
            bottomPanel.add(frame);
    }

    /**
     * Changes the preview of a shape that can be placed on a GOLSimulation
     */
    public void setShapePreview(GOLShape shape) {
        shapePreviewPanel.removeAll();
        shapePreviewPanel.add(new GOLShapePanel(shape, true));
        shapePreviewPanel.revalidate();
        shapePreviewPanel.repaint();
    }
}
