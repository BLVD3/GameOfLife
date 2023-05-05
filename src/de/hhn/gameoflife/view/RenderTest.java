package de.hhn.gameoflife.view;

import de.hhn.gameoflife.model.GameOfLife;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class RenderTest extends JFrame implements Runnable, MouseListener, KeyListener {
    volatile boolean wait;
    BufferedImage image;
    GameOfLife gol;
    Thread thread = new Thread(this);

    public RenderTest() {
        image = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
        gol = new GameOfLife(200, 200);
        JPanel canvas = new JPanel() {
            @Override
            public void paint(Graphics g) {
                int width = getWidth(), height = getHeight();
                g.drawImage(image, -200, -200, width, height, null);
            }
        };
        setContentPane(canvas);
        getContentPane().setPreferredSize(new Dimension(800, 800));
        pack();
        wait = true;
        thread.start();
        Graphics2D g = image.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0,200,200);
        g.dispose();
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        for (int i = 0; i < 200; i++) {
            foo(i, 100);
        }
        wait = false;
        repaint();
    }

    @Override
    public void run() {
        while (true) {
            while (wait) Thread.onSpinWait();
            gol.step();
            gol.forEachChange((x, y, alive) -> {
                image.setRGB(x, y, alive ? 0 : 0xffffff);
            });
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
            wait = !wait;
        System.out.println("Should Be Pressed");
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX() * getContentPane().getWidth() / 200;
        int y = e.getY() * getContentPane().getHeight() / 200;
        if (x >= 200)
            return;
        if (y >= 200)
            return;
        boolean state = !gol.getAlive(x, y);
        gol.setAlive(x, y, state);
        image.setRGB(x, y, state ? 0 : 0xffffff);
        System.out.println("Should be printed");
    }

    private void foo(int x, int y) {
        boolean state = !gol.getAlive(x, y);
        gol.setAlive(x, y, state);
        image.setRGB(x, y, state ? 0 : 0xffffff);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
