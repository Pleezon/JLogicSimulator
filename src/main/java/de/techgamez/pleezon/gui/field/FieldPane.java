package de.techgamez.pleezon.gui.field;

import de.techgamez.pleezon.backend.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;

public class FieldPane extends JPanel {
    World world;


    int translateX = 0;
    int translateY = 0;
    double scaleX = 1.0;
    double scaleY = 1.0;


    public void setWorld(World world) {
        this.world = world;
    }

    public FieldPane() {
        super();
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                translateX += e.getX() - translateX;
                translateY += e.getY() - translateY;
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
            }
        });
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

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
        });
        this.addMouseWheelListener(e -> {
            if (!e.isControlDown()) return;
            if (e.getWheelRotation() > 0) {
                scaleX *= 1.1;
                scaleY *= 1.1;
            } else {
                scaleX /= 1.1;
                scaleY /= 1.1;
            }
            repaint();
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform at = new AffineTransform();
        at.translate(translateX, translateY);
        at.scale(scaleX, scaleY);
        g2d.setTransform(at);

        //test
        g2d.drawString("BLAH", 20, 20);
        g2d.drawRect(200, 200, 200, 200);
    }

}
