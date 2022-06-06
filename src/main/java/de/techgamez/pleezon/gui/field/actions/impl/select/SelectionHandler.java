package de.techgamez.pleezon.gui.field.actions.impl.select;

import de.techgamez.pleezon.gui.field.FieldPane;
import de.techgamez.pleezon.gui.field.actions.ActionHandler;
import de.techgamez.pleezon.gui.field.actions.impl.component.WorldComponent;
import de.techgamez.pleezon.gui.field.actions.impl.undo.UndoableRedoable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.HashSet;

public class SelectionHandler extends ActionHandler {
    private final HashSet<WorldComponent> selectedComponents = new HashSet<>();
    private Point selectionMousePosition = new Point();
    private Rectangle currentSelection = new Rectangle();
    private final HashSet<WorldComponent> newSelectedComponents = new HashSet<>();

    public SelectionHandler(FieldPane mainField) {
        super(mainField);
    }

    public WorldComponent[] getSelectedComponents() {
        return selectedComponents.toArray(new WorldComponent[0]);
    }

    private enum SelectionType {
        NONE,
        SELECT,
        UNSELECT
    }

    private SelectionType selectionType = SelectionType.NONE;


    @Override
    public void draw(Graphics2D g2d) {
        if (currentSelection != null) {
            Color c = g2d.getColor();
            if (selectionType == SelectionType.SELECT) {
                g2d.setColor(javax.swing.UIManager.getDefaults().getColor("Component.accentColor"));
            }
            if (selectionType == SelectionType.UNSELECT) {
                g2d.setColor(javax.swing.UIManager.getDefaults().getColor("Component.error.focusedBorderColor"));
            }
            g2d.drawRect(currentSelection.x, currentSelection.y, currentSelection.width, currentSelection.height);
            g2d.setColor(c);
        }
    }

    private void clearSelectionRect() {
        if (selectionMousePosition != null && gui.getWorld() != null) {
            if (!newSelectedComponents.isEmpty()) {
                WorldComponent[] components = newSelectedComponents.toArray(new WorldComponent[0]);
                final SelectionType type = selectionType;
                UndoableRedoable action = new UndoableRedoable() {
                    @Override
                    public void redo() {
                        for (WorldComponent component : components) {
                            if (type == SelectionType.SELECT) {
                                component.setState(WorldComponent.ComponentState.SELECTED);
                                selectedComponents.add(component);
                            } else if (type == SelectionType.UNSELECT) {
                                selectedComponents.remove(component);
                                component.setState(WorldComponent.ComponentState.NORMAL);
                            }
                        }
                        gui.repaint();
                    }

                    @Override
                    public void undo() {
                        for (WorldComponent component : components) {
                            if (type == SelectionType.UNSELECT) {
                                component.setState(WorldComponent.ComponentState.SELECTED);
                                selectedComponents.add(component);
                            } else if (type == SelectionType.SELECT) {
                                component.setState(WorldComponent.ComponentState.NORMAL);
                                selectedComponents.remove(component);
                            }
                        }
                        gui.repaint();
                    }
                };
                action.redo();
                gui.undoRedoHandler.addAction(action);
                newSelectedComponents.clear();
            }
            selectionMousePosition = null;
            currentSelection = null;
            selectionType = SelectionType.NONE;
            gui.repaint();
        }
    }

    private void updateSelection(Point mousePos) {
        selectionMousePosition = mousePos;
        Point2D start = gui.screenToWorld(new Point2D.Float(gui.getMouseClickPoint().x, gui.getMouseClickPoint().y));
        Point2D end = gui.screenToWorld(new Point2D.Float(selectionMousePosition.x, selectionMousePosition.y));
        Point selectionStart = new Point((int) Math.min(start.getX(), end.getX()), (int) Math.min(start.getY(), end.getY()));
        int width = (int) Math.abs(end.getX() - start.getX());
        int height = (int) Math.abs(end.getY() - start.getY());
        currentSelection = new Rectangle(selectionStart.x, selectionStart.y, width, height);
        for (WorldComponent component : gui.getWorld().getComponents().values()) {
            if (component.isIn(currentSelection)) {
                if (!component.getState().equals(selectionType == SelectionType.SELECT ? WorldComponent.ComponentState.SELECTED : WorldComponent.ComponentState.NORMAL)) {
                    newSelectedComponents.add(component);
                }
            } else {
                newSelectedComponents.remove(component);
            }
        }
        gui.repaint();
    }

    @Override
    public MouseAdapter mouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                clearSelectionRect();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                clearSelectionRect();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                if (gui.getWorld() == null) return;
                if (gui.getMouseClickButton() == MouseEvent.BUTTON1 && e.isAltDown() && !e.isControlDown()) {

                    if (selectionType == SelectionType.NONE) {
                        selectionType = SelectionType.SELECT;
                    }
                    updateSelection(e.getPoint());
                }
                if (gui.getMouseClickButton() == MouseEvent.BUTTON1 && !e.isAltDown() && e.isControlDown()) {
                    if (selectionType == SelectionType.NONE) {
                        selectionType = SelectionType.UNSELECT;
                    }
                    updateSelection(e.getPoint());
                }
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
                if (gui.getMouseClickButton() != null && gui.getMouseClickButton() == MouseEvent.BUTTON1 && (e.isControlDown() || e.isAltDown())) {
                    updateSelection(e.getPoint());
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (gui.getWorld() == null) return;
                boolean isUnselect = !e.isAltDown() && e.isControlDown();
                boolean isSelect = e.isAltDown() && !e.isControlDown();
                if (e.getButton() == MouseEvent.BUTTON1 && (isSelect || isUnselect)) {
                    Point2D worldPos = gui.screenToWorld(new Point2D.Float(e.getX(), e.getY()));
                    WorldComponent[] components = gui.getWorld().getComponentsAt(worldPos);
                    final SelectionType type = isSelect ? SelectionType.SELECT : SelectionType.UNSELECT;
                    UndoableRedoable action = new UndoableRedoable() {
                        @Override
                        public void redo() {
                            for (WorldComponent component : components) {
                                if (type == SelectionType.SELECT) {
                                    component.setState(WorldComponent.ComponentState.SELECTED);
                                    selectedComponents.add(component);
                                } else {
                                    selectedComponents.remove(component);
                                    component.setState(WorldComponent.ComponentState.NORMAL);
                                }
                            }
                            gui.repaint();
                        }

                        @Override
                        public void undo() {
                            for (WorldComponent component : components) {
                                if (type != SelectionType.SELECT) {
                                    component.setState(WorldComponent.ComponentState.SELECTED);
                                    selectedComponents.add(component);
                                } else {
                                    selectedComponents.remove(component);
                                    component.setState(WorldComponent.ComponentState.NORMAL);
                                }
                            }
                            gui.repaint();
                        }
                    };
                    action.redo();
                    gui.undoRedoHandler.addAction(action);
                    gui.repaint();
                }


            }
        };
    }

    @Override
    public KeyAdapter keyAdapter() {
        return new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if (e.getKeyCode() == KeyEvent.VK_ALT) clearSelectionRect();
            }
        };
    }

    public void unselectComponents() {
        for (WorldComponent c : selectedComponents) {
            c.unselect();
        }
        selectedComponents.clear();
        gui.repaint();
    }

    @Override
    public void registerInputs(InputMap map) {
        super.registerInputs(map);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "deleteSelection");
    }

    @Override
    public void registerActions(ActionMap map) {
        super.registerActions(map);
        map.put("deleteSelection", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gui.getWorld() != null) {
                    WorldComponent[] components = getSelectedComponents();
                    UndoableRedoable action = new UndoableRedoable() {
                        @Override
                        public void redo() {
                            for (WorldComponent component : components) {
                                gui.getWorld().deleteComponent(component);
                            }
                            gui.repaint();
                        }

                        @Override
                        public void undo() {
                            for (WorldComponent component : components) {
                                gui.getWorld().addComponent(component);
                            }
                            gui.repaint();
                        }
                    };
                    action.redo();
                    gui.undoRedoHandler.addAction(action);
                }
            }
        });
    }
}
