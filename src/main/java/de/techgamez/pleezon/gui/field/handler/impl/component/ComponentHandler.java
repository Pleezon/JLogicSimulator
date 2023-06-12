package de.techgamez.pleezon.gui.field.handler.impl.component;

import de.techgamez.pleezon.backend.data.LogicComponent;
import de.techgamez.pleezon.backend.data.impl.gates.*;
import de.techgamez.pleezon.backend.data.impl.misc.Switch;
import de.techgamez.pleezon.gui.field.FieldPane;
import de.techgamez.pleezon.gui.field.handler.ActionHandler;
import de.techgamez.pleezon.gui.field.handler.impl.undo.UndoableRedoable;
import de.techgamez.pleezon.misc.DrawUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;

public class ComponentHandler extends ActionHandler {


    public ComponentHandler(FieldPane gui) {
        super(gui);
    }

    @Override
    public void draw(Graphics2D g2d) {
        if (gui.getWorld() == null) return;
        for (WorldComponent component : gui.getWorld().getComponents().values()) {
            for (Long id : component.component.getOuputs()) {
                WorldComponent other = gui.getWorld().getComponents().get(id);
                Point2D start = component.getMiddlePoint();
                Point2D end = other.getMiddlePoint();
                DrawUtils.drawConnection(g2d, (int) start.getX(), (int) end.getX(), (int) start.getY(), (int) end.getY(), DrawUtils.BASIC_ARROW_HEAD, Color.RED, 3, 50);
            }
        }
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

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.isControlDown() || e.isAltDown()) return;
                Point2D p = gui.screenToWorld(new Point2D.Float(e.getX(), e.getY()));
                if (gui.getWorld() != null) {
                    WorldComponent[] clicked = gui.getWorld().getComponentsAt(p);
                    if (clicked.length > 0) {
                        WorldComponent comp = clicked[clicked.length - 1];
                        int status = comp.component.triggerClick(gui.getWorld());
                        if (status >= 0) {
                            gui.getWorld().logicHandler.updateChildren(comp.component, 0);
                        }
                    }
                }
            }
        };
    }

    @Override
    public KeyAdapter keyAdapter() {
        return null;
    }

    @Override
    public void registerInputs(InputMap map) {
        super.registerInputs(map);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "addAND");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0), "addOR");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "addXOR");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK), "addNAND");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK), "addNOR");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK), "addXNOR");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "addSwitch");
    }

    @Override
    public void registerActions(ActionMap map) {
        super.registerActions(map);
        map.put("addAND", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addComponent(new ANDGate());
            }
        });
        map.put("addOR", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addComponent(new ORGate());
            }
        });
        map.put("addXOR", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addComponent(new XORGate());
            }
        });
        map.put("addNAND", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addComponent(new NANDGate());
            }
        });
        map.put("addNOR", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addComponent(new NORGate());
            }
        });
        map.put("addXNOR", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addComponent(new XNORGate());
            }
        });
        map.put("addSwitch", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addComponent(new Switch());
            }
        });
    }

    private void addComponent(LogicComponent component) {
        WorldComponent comp = new WorldComponent(component, 0, 0);

        UndoableRedoable action = new UndoableRedoable() {
            @Override
            public void redo() {
                if (gui.getWorld() != null) {
                    Point p = gui.getMousePosition();
                    Point2D world_mousePos = gui.screenToWorld(new Point2D.Float((float) p.getX(), (float) p.getY()));
                    comp.x = (int) (world_mousePos.getX());
                    comp.y = (int) (world_mousePos.getY());
                    comp.component.setID(gui.getWorld().addComponent(comp));
                    gui.repaint();
                }
            }

            @Override
            public void undo() {
                gui.getWorld().deleteComponent(comp);
                gui.repaint();
            }
        };
        action.redo();
        gui.undoRedoHandler.addAction(action);
    }
}
