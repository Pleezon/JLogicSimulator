package de.techgamez.pleezon.gui.field.handler.impl.select;

import de.techgamez.pleezon.gui.field.FieldPane;
import de.techgamez.pleezon.gui.field.handler.ActionHandler;
import de.techgamez.pleezon.gui.field.handler.impl.component.WorldComponent;
import de.techgamez.pleezon.gui.field.handler.impl.undo.UndoableRedoable;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class SelectionDragHandler extends ActionHandler {

    private Point totalDragOffset = null;
    private Point lastCursorPosition = null;

    public SelectionDragHandler(FieldPane mainField) {
        super(mainField);
    }

    @Override
    public void draw(Graphics2D g2d) {

    }

    private void finalizeDrag() {
        final Point2D transformedDragPos = gui.screenToWorld(new Point2D.Float(totalDragOffset.x, totalDragOffset.y));
        final Point2D transformedOrigin = gui.screenToWorld(new Point2D.Float(0, 0));
        final WorldComponent[] components = gui.selectionHandler.getSelectedComponents();
        UndoableRedoable action = new UndoableRedoable() {
            @Override
            public void redo() {
                for (WorldComponent component : components) {
                    component.x = component.x - ((transformedDragPos.getX() - transformedOrigin.getX()));
                    component.y = component.y - ((transformedDragPos.getY() - transformedOrigin.getY()));
                }
                gui.repaint();
            }

            @Override
            public void undo() {
                for (WorldComponent component : components) {
                    component.x = component.x + ((transformedDragPos.getX() - transformedOrigin.getX()));
                    component.y = component.y + ((transformedDragPos.getY() - transformedOrigin.getY()));
                }
                gui.repaint();
            }
        };
        gui.undoRedoHandler.addAction(action);
    }

    private void applyDrag(int dx, int dy) {
        final Point2D transformedDragPos = gui.screenToWorld(new Point2D.Float(dx, dy));
        final Point2D transformedOrigin = gui.screenToWorld(new Point2D.Float(0, 0));
        final WorldComponent[] components = gui.selectionHandler.getSelectedComponents();
        for (WorldComponent component : components) {
            component.x = component.x - ((transformedDragPos.getX() - transformedOrigin.getX()));
            component.y = component.y - ((transformedDragPos.getY() - transformedOrigin.getY()));
        }
    }

    @Override
    public MouseAdapter mouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gui.wireDragHandler.startComponent != null) return;
                super.mousePressed(e);
                totalDragOffset = null;
                lastCursorPosition = null;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (gui.wireDragHandler.startComponent != null) return;
                if (totalDragOffset != null) {
                    finalizeDrag();
                    totalDragOffset = null;
                    lastCursorPosition = null;
                    gui.repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (gui.wireDragHandler.startComponent != null) return;
                if (gui.getMouseClickButton() == MouseEvent.BUTTON1 && !e.isAltDown() && !e.isControlDown()) {
                    totalDragOffset = new Point((gui.getMouseClickPoint().x - e.getX()), (gui.getMouseClickPoint().y - e.getY()));
                    if (lastCursorPosition == null) {
                        lastCursorPosition = gui.getMouseClickPoint();
                    }
                    applyDrag(lastCursorPosition.x - e.getX(), lastCursorPosition.y - e.getY());
                    lastCursorPosition = e.getPoint();
                    gui.repaint();
                }
            }
        };
    }


    @Override
    public KeyAdapter keyAdapter() {
        return null;
    }
}
