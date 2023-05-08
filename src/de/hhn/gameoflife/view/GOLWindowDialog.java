package de.hhn.gameoflife.view;

import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GOLWindowDialog extends JInternalFrame {

    MainWindow window;
    JTextField widthInput;
    JTextField heightInput;

    public GOLWindowDialog(MainWindow window) {
        this.window = window;

        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setTitle("Neues Fenster");
        setLayout(null);
        getContentPane().setPreferredSize(new Dimension(180, 93));
        pack();
        setResizable(false);
        setClosable(true);
        JLabel widthLabel = new JLabel("Breite:");
        JLabel heightLabel = new JLabel("Höhe:");
        widthInput = new JTextField();
        heightInput = new JTextField();
        JButton button = new JButton("Fertig");
        button.addActionListener(e -> click());

        widthLabel.setBounds(5, 10, 70, 21);
        heightLabel.setBounds(5, 36, 70, 21);
        widthInput.setBounds(75, 10, 100, 21);
        heightInput.setBounds(75, 36, 100, 21);
        button.setBounds(40, 62, 100, 21);

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
            height = Integer.parseInt(widthInput.getText());
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
