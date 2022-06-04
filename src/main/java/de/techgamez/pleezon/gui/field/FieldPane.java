package de.techgamez.pleezon.gui.field;

import de.techgamez.pleezon.backend.World;
import de.techgamez.pleezon.gui.JLogicSimulatorGUI;
import de.techgamez.pleezon.gui.field.component.WorldComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Objects;

public class FieldPane extends JPanel {
    private static final float[] SCALES = {0.1f, 0.5f, 1.0f, 2.0f, 5.0f};
    private static final int DEFAULT_SCALE_INDEX = 2;

    public World world;

    /**
     * The offset, in terms of screen coordinates, from the default view.
     */
    private final Point2D.Float offset = new Point2D.Float(0, 0);

    /**
     * The scale at which the world is being viewed.
     */
    private int scaleIndex = DEFAULT_SCALE_INDEX;

    /**
     * The last point at which the mouse button was pressed down. This is used
     * as a sort of "anchor" point to ensure that dragging is consistent and
     * not affected by lag.
     */
    private Point lastMouseClickPoint = null;

    /**
     * The offset applied due to a mouse drag that's currently in progress.
     */
    private Point mouseDragOffset = null;
    private int lastMouseButton = -1;
    private Point dragPos = null;

    JLogicSimulatorGUI gui;

    public void setWorld(World world) {
        this.world = world;
        Objects.requireNonNull(gui.topBar.fileButton.menu.saveMenuOption).setEnabled(world != null);
        repaint();
    }

    public FieldPane(JLogicSimulatorGUI gui) {
        this.gui = gui;
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMouseButton = e.getButton();
                lastMouseClickPoint = e.getPoint();
                mouseDragOffset = null;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lastMouseButton = -1;
                dragPos = null;
                lastMouseClickPoint = null;
                // Apply the mouse drag, if any.
                if (mouseDragOffset != null) {
                    offset.x -= mouseDragOffset.x;
                    offset.y -= mouseDragOffset.y;
                    mouseDragOffset = null;
                }
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
                if (lastMouseButton == MouseEvent.BUTTON1) {
                    dragPos = e.getPoint();
                    repaint();
                } else if (lastMouseButton == MouseEvent.BUTTON3) {
                    if (lastMouseClickPoint != null) {
                        mouseDragOffset = new Point(
                                lastMouseClickPoint.x - e.getX(),
                                lastMouseClickPoint.y - e.getY()
                        );
                        repaint();
                    }
                }

            }
        };
        addMouseListener(mouseAdapter);
        addMouseWheelListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    /**
     * Increases the scale, if possible.
     */
    private void zoomIn() {
        if (scaleIndex < SCALES.length - 1) {
            scaleIndex++;
            repaint();
        }
    }

    /**
     * Decreases the scale, if possible
     */
    private void zoomOut() {
        if (scaleIndex > 0) {
            scaleIndex--;
            repaint();
        }
    }

    /**
     * Gets the currently applied scale factor.
     *
     * @return The scale factor.
     */
    private float getScale() {
        return SCALES[scaleIndex];
    }

    /**
     * Resets the field pane's view to the default, which is a 1.0 scale,
     * centered at the origin (center) of the pane.
     */
    public void resetView() {
        this.offset.x = 0;
        this.offset.y = 0;
        this.mouseDragOffset = null;
        this.lastMouseClickPoint = null;
        this.scaleIndex = DEFAULT_SCALE_INDEX;
        repaint();
    }

    /**
     * Gets the current true screen offset that should be applied when
     * transforming world points to screen. This includes both the current
     * offset, and any mouse drag offset if the mouse is currently being
     * dragged.
     *
     * @return The true screen offset.
     */
    private Point2D.Float getScreenOffset() {
        float x = offset.x;
        float y = offset.y;
        if (mouseDragOffset != null) {
            x -= mouseDragOffset.x;
            y -= mouseDragOffset.y;
        }
        return new Point2D.Float(x, y);
    }

    /**
     * Gets a transform that transforms world coordinates into screen coordinates.
     *
     * @return The transformation.
     */
    private AffineTransform getWorldTransform() {
        AffineTransform tx = new AffineTransform();
        // First translate to the center of the panel, and do all operations from there.
        tx.translate(getWidth() / 2.0, getHeight() / 2.0);
        // Scale before our main translation.
        float s = getScale();
        tx.scale(s, s);
        // Translate according to the screen offset, and reverse the effect of scaling so that translation is still valid for screen space.
        Point2D.Float trueOffset = getScreenOffset();
        tx.translate(trueOffset.x / s, trueOffset.y / s);

        return tx;
    }

    HashMap<Class<? extends WorldComponent>, BufferedImage> textureCache = new HashMap<>();

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform worldTx = getWorldTransform();
        g2d.setTransform(worldTx);
        if (world != null) {
            g2d.drawRect(100, 100, 100, 100);
            world.components.forEach((k, v) -> {
                v.draw(g2d, textureCache);
            });
        }

    }

}
