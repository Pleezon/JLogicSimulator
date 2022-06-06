package de.techgamez.pleezon.gui.field;

import de.techgamez.pleezon.backend.World;
import de.techgamez.pleezon.gui.JLogicSimulatorGUI;
import de.techgamez.pleezon.gui.field.actions.ActionHandler;
import de.techgamez.pleezon.gui.field.actions.impl.component.ComponentHandler;
import de.techgamez.pleezon.gui.field.actions.impl.select.SelectionDragHandler;
import de.techgamez.pleezon.gui.field.actions.impl.select.SelectionHandler;
import de.techgamez.pleezon.gui.field.actions.impl.undo.UndoRedoHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;

public class FieldPane extends JPanel {
    private static final float[] SCALES = {0.1f, 0.5f, 1.0f, 2.0f};
    private static final int DEFAULT_SCALE_INDEX = 2;
    private int scaleIndex = DEFAULT_SCALE_INDEX;
    private final Point2D.Float worldOffset = new Point2D.Float(0, 0);
    private Point2D.Float cameraMoveDragOffset = null;
    private Point mouseClickPoint = null;
    private Integer mouseClickButton = null;
    private World world;

    private AffineTransform worldTx = null;
    private AffineTransform screenTx = null;

    JLogicSimulatorGUI gui;

    ArrayList<ActionHandler> actions = new ArrayList<>();

    public void setWorld(World world) {
        this.world = world;
        Objects.requireNonNull(gui.topBar.fileButton.menu.saveMenuOption).setEnabled(world != null);
        resetView();
    }

    public World getWorld() {
        return this.world;
    }

    public Point getMouseClickPoint() {
        return mouseClickPoint;
    }

    public Integer getMouseClickButton() {
        return mouseClickButton;
    }

    public final SelectionHandler selectionHandler = new SelectionHandler(this);
    public final SelectionDragHandler selectionDragHandler = new SelectionDragHandler(this);

    public final UndoRedoHandler undoRedoHandler = new UndoRedoHandler(this);
    public final ComponentHandler componentHandler = new ComponentHandler(this);

    private void initActions() {
        actions.add(undoRedoHandler);
        actions.add(selectionHandler);
        actions.add(selectionDragHandler);
        actions.add(componentHandler);
    }

    private void resetView() {
        this.worldOffset.x = 0;
        this.worldOffset.y = 0;
        this.cameraMoveDragOffset = null;
        this.mouseClickButton = null;
        this.mouseClickPoint = null;
        this.scaleIndex = DEFAULT_SCALE_INDEX;
        updateTransform();
        repaint();
    }

    public float getScale() {
        return SCALES[scaleIndex];
    }

    private Point2D.Float getScreenOffset() {
        float x = worldOffset.x;
        float y = worldOffset.y;
        if (cameraMoveDragOffset != null) {
            x -= cameraMoveDragOffset.x;
            y -= cameraMoveDragOffset.y;
        }
        return new Point2D.Float(x, y);
    }

    public FieldPane(JLogicSimulatorGUI gui) {
        this.gui = gui;
        initActions();
        InputMap inputMap = this.getInputMap();
        ActionMap actionMap = this.getActionMap();

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseClickButton = e.getButton();
                mouseClickPoint = e.getPoint();
                cameraMoveDragOffset = null;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mouseClickButton = null;
                mouseClickPoint = null;
                applyMouseDrag();
                repaint();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (!e.isControlDown()) return;
                if (e.getWheelRotation() < 0) {
                    zoomIn();
                } else if (e.getWheelRotation() > 0) {
                    zoomOut();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (mouseClickPoint == null) return;
                if (mouseClickButton == MouseEvent.BUTTON3) {
                    cameraMoveDragOffset = new Point2D.Float(mouseClickPoint.x - e.getX(), mouseClickPoint.y - e.getY());
                    updateTransform();
                    repaint();
                }
            }
        };
        addMouseListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);

        for (ActionHandler a : actions) {
            if (a.mouseAdapter() != null) {
                addMouseListener(a.mouseAdapter());
                addMouseWheelListener(a.mouseAdapter());
                addMouseMotionListener(a.mouseAdapter());
            }
            if (a.keyAdapter() != null) {
                addKeyListener(a.keyAdapter());
            }
            a.registerInputs(inputMap);
            a.registerActions(actionMap);
        }
        updateTransform();
    }

    private void zoomIn() {
        if (scaleIndex < SCALES.length - 1) {
            scaleIndex++;
            updateTransform();
            repaint();
        }
    }

    private void zoomOut() {
        if (scaleIndex > 0) {
            scaleIndex--;
            updateTransform();
            repaint();
        }
    }

    private void applyMouseDrag() {
        if (cameraMoveDragOffset != null) {
            worldOffset.x -= cameraMoveDragOffset.x;
            worldOffset.y -= cameraMoveDragOffset.y;
            cameraMoveDragOffset = null;
            updateTransform();
        }
    }

    private void updateTransform() {
        AffineTransform tx = new AffineTransform();
        tx.translate(getWidth() / 2.0, getHeight() / 2.0);
        float s = getScale();
        tx.scale(s, s);
        Point2D.Float trueOffset = getScreenOffset();
        tx.translate(trueOffset.x / s, trueOffset.y / s);
        try {
            this.worldTx = tx;
            this.screenTx = tx.createInverse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Point2D worldToScreen(Point2D.Float worldPoint) {
        return worldTx.transform(worldPoint, null);
    }

    public Point2D screenToWorld(Point2D.Float worldPoint) {
        return screenTx.transform(worldPoint, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setTransform(worldTx);
        for (ActionHandler a : actions) {
            a.draw(g2d);
        }
    }
}
