package de.techgamez.pleezon.gui.field.handler.impl.wire;

import de.techgamez.pleezon.backend.data.IllegalStateModificationException;
import de.techgamez.pleezon.gui.field.FieldPane;
import de.techgamez.pleezon.gui.field.handler.ActionHandler;
import de.techgamez.pleezon.gui.field.handler.impl.component.WorldComponent;
import de.techgamez.pleezon.misc.DrawUtils;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class WireDragHandler extends ActionHandler {

    public WorldComponent startComponent;

    @Override
    public void draw(Graphics2D g2d) {
        // look into pcb/wire tracing algorithms
        Point p = gui.getMousePosition();
        if (p != null) {
            Point2D world_mousePos = gui.screenToWorld(new Point2D.Float((float) p.getX(), (float) p.getY()));
            if (startComponent != null) {
                g2d.setColor(Color.BLACK);
                int x0 = (int) startComponent.getMiddlePoint().getX();
                int y0 = (int) startComponent.getMiddlePoint().getY();
                int x1 = (int) world_mousePos.getX();
                int y1 = (int) world_mousePos.getY();
                DrawUtils.drawConnection(g2d, x0, x1, y0, y1, DrawUtils.BASIC_ARROW_HEAD, Color.RED, 2, 100);
            }
        }
    }


    @Override
    public MouseAdapter mouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (startComponent == null) return;
                Point p = e.getPoint();
                Point2D world_mousePos = gui.screenToWorld(new Point2D.Float((float) p.getX(), (float) p.getY()));
                WorldComponent[] worldComponents = gui.getWorld().getComponentsAt(world_mousePos);
                if (worldComponents.length > 0) {
                    WorldComponent scmp = worldComponents[worldComponents.length - 1];
                    if (scmp.component.maxInputs() > scmp.component.getInputs().size()) {

                        // cant wire a component to one that is already connecting to it
                        if (!scmp.component.hasOutput(startComponent.component.getID())) {

                            // cant wire a component to itself
                            if (scmp.component.getID() != startComponent.component.getID()) {

                                //draw
                                if (scmp.component.hasInput(startComponent.component.getID())) {
                                    try {
                                        scmp.component.removeInput(startComponent.component.getID());
                                        startComponent.component.removeOutput(scmp.component.getID());
                                    } catch (IllegalStateModificationException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                    //gui.getWorld().logicHandler.updateFrom(scmp.component.getID(),);//make it update lol. get inputs from current world -> boolean[]
                                } else {
                                    try {
                                        scmp.component.addInput(startComponent.component.getID());
                                        startComponent.component.addOutput(scmp.component.getID());
                                    } catch (IllegalStateModificationException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                            }
                        }
                    }
                }
                startComponent = null;
                gui.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!e.isControlDown() && !e.isAltDown() && gui.selectionHandler.getSelectedComponents().length == 0 && e.getButton() == 1) {
                    if (gui.getWorld() == null) return;
                    Point p = e.getPoint();
                    Point2D world_mousePos = gui.screenToWorld(new Point2D.Float((float) p.getX(), (float) p.getY()));
                    WorldComponent[] worldComponents = gui.getWorld().getComponentsAt(world_mousePos);
                    if (worldComponents.length > 0) {
                        WorldComponent scmp = worldComponents[worldComponents.length - 1];
                        if (scmp.component.getOuputs().size() < scmp.component.maxOutputs()) {
                            startComponent = scmp;
                        }
                    }
                }
                super.mousePressed(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (startComponent != null) {
                    gui.repaint();
                }
                super.mouseDragged(e);
            }
        };
    }


    @Override
    public KeyAdapter keyAdapter() {
        return null;
    }

    public WireDragHandler(FieldPane gui) {
        super(gui);
    }

}
