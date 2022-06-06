package de.techgamez.pleezon.gui.field.actions.impl.component;

import de.techgamez.pleezon.gui.field.FieldPane;
import de.techgamez.pleezon.gui.field.actions.ActionHandler;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class ComponentHandler extends ActionHandler {


    public ComponentHandler(FieldPane gui) {
        super(gui);
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (gui.getWorld() == null) return;
        for (WorldComponent component : gui.getWorld().getComponents().values()) {
            component.draw(g2d, 0, 0);
        }
    }

    @Override
    public MouseAdapter mouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                Point2D p = gui.screenToWorld(new Point2D.Float(e.getX(), e.getY()));
                if (gui.getWorld() != null) {
                    for (WorldComponent component : gui.getWorld().getComponents().values()) {
                        boolean clicked = component.isInHitbox(p, false);
                        if (clicked) {
                            component.component.triggerClick(gui.getWorld());
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }
        };
    }

    @Override
    public KeyAdapter keyAdapter() {
        return null;
    }
}
