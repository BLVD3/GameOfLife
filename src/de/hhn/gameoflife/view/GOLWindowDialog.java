package de.hhn.gameoflife.view;

import javax.swing.*;
import java.awt.*;

public class GOLWindowDialog extends JInternalFrame {

    MainWindow window;
    JTextField widthInput;
    JTextField heightInput;

    public GOLWindowDialog(MainWindow window) {
        this.window = window;

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setTitle("Neues Fenster");
        setLayout(null);
        getContentPane().setPreferredSize(new Dimension(180, 111));
        pack();
        setResizable(false);
        setClosable(true);
        JLabel widthLabel = new JLabel("Breite:");
        JLabel heightLabel = new JLabel("Höhe:");
        widthInput = new JTextField();
        heightInput = new JTextField();
        JButton button = new JButton("Fertig");
        button.addActionListener(e -> click());

        widthLabel.setBounds(5, 10, 70, 27);
        heightLabel.setBounds(5, 42, 70, 27);
        widthInput.setBounds(75, 10, 100, 27);
        heightInput.setBounds(75, 42, 100, 27);
        button.setBounds(40, 74, 100, 27);

        add(widthLabel);
        add(heightLabel);
        add(widthInput);
        add(heightInput);
        add(button);
    }

    void click() {
        int width, height;
        try {
            width = Integer.parseInt(widthInput.getText());
            height = Integer.parseInt(heightInput.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Die Breite und Höhe müssen Nummern sein!");
            return;
        }
        if (width <= 0 || height <= 0) {
            JOptionPane.showMessageDialog(this, "Die Breite und Höhe müssen größer als 0 sein !");
            return;
        }
        window.addWindow(width, height);
        this.setVisible(false);
    }
}
