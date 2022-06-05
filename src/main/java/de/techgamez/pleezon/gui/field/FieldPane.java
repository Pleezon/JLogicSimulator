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
import java.util.HashSet;
import java.util.Objects;

public class FieldPane extends JPanel {
    private static final float[] SCALES = {0.1f, 0.5f, 1.0f, 2.0f};
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
     * The offset applied to camera movement due to a mouse drag that's currently in progress.
     */
    private Point cameraMoveDragOffset = null;


    /*
     * The offset applied to selection movement due to a mouse drag that's currently in progress.
     */
    private Point selectionMoveDragOffset = null;
    private int lastMouseButton = -1;
    private Point dragPos = null;

    private final HashSet<WorldComponent> selectedComponents = new HashSet<>();
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
                cameraMoveDragOffset = null;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                lastMouseButton = -1;
                dragPos = null;
                lastMouseClickPoint = null;
                // Apply the mouse drag, if any.
                if (cameraMoveDragOffset != null) {
                    offset.x -= cameraMoveDragOffset.x;
                    offset.y -= cameraMoveDragOffset.y;
                    cameraMoveDragOffset = null;
                }
                // Apply the component drag, if any.
                if (selectionMoveDragOffset != null) {

                    AffineTransform tf = getWorldTransform();
                    Point2D transformedDragPos = tf.transform(selectionMoveDragOffset, null);
                    Point2D transformedOrigin = tf.transform(new Point(0, 0), null);

                    for (WorldComponent component : selectedComponents) {
                        component.x = component.x - ((transformedDragPos.getX() - transformedOrigin.getX()));
                        component.y = component.y - ((transformedDragPos.getY() - transformedOrigin.getY()));
                    }
                    selectionMoveDragOffset = null;
                }
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
                if (lastMouseClickPoint == null) return;
                if (lastMouseButton == MouseEvent.BUTTON1 && e.isControlDown()) {
                    dragPos = e.getPoint();
                    repaint();
                } else if (lastMouseButton == MouseEvent.BUTTON3) {

                    cameraMoveDragOffset = new Point(lastMouseClickPoint.x - e.getX(), lastMouseClickPoint.y - e.getY());
                    repaint();

                } else if (lastMouseButton == MouseEvent.BUTTON1) {
                    selectionMoveDragOffset = new Point(lastMouseClickPoint.x - e.getX(), lastMouseClickPoint.y - e.getY());
                    repaint();
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
        this.cameraMoveDragOffset = null;

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
        if (cameraMoveDragOffset != null) {
            x -= cameraMoveDragOffset.x;
            y -= cameraMoveDragOffset.y;
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

    public static class CacheKey {

        public WorldComponent.ComponentState state;
        Class<? extends WorldComponent> clazz;

        public CacheKey(Class<? extends WorldComponent> clazz, WorldComponent.ComponentState state) {
            this.clazz = clazz;
            this.state = state;
        }

    }

    HashMap<CacheKey, BufferedImage> textureCache = new HashMap<>();

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform worldTx = getWorldTransform();
        g2d.setTransform(worldTx);


        int selectionStartX = 0;
        int selectionStartY = 0;
        int selectionEndX = 0;
        int selectionEndY = 0;

        // Draw the selection rectangle, if any
        if (dragPos != null) {
            try {
                // dragPos is the current drag's mouse position.
                Point2D start = worldTx.inverseTransform(new Point2D.Double(dragPos.x, dragPos.y), null);
                // lastMouseClickPoint is the position of the mouse when the drag started.
                Point2D end = worldTx.inverseTransform(new Point2D.Double(lastMouseClickPoint.x, lastMouseClickPoint.y), null);
                selectionStartX = (int) Math.min(start.getX(), end.getX());
                selectionStartY = (int) Math.min(start.getY(), end.getY());
                selectionEndX = (int) Math.max(start.getX(), end.getX());
                selectionEndY = (int) Math.max(start.getY(), end.getY());
                g2d.drawRect(selectionStartX, selectionStartY, (int) Math.abs(end.getX() - start.getX()), (int) Math.abs(end.getY() - start.getY()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int finalStartX = selectionStartX;
        int finalStartY = selectionStartY;
        int finalEndX = selectionEndX;
        int finalEndY = selectionEndY;
        // draw each component
        if (world != null) {
            world.components.forEach((k, v) -> {
                boolean selected = v.checkForSelect(finalStartX, finalStartY, finalEndX, finalEndY);
                int offX = 0;
                int offY = 0;
                if (selected) selectedComponents.add(v);

                if (selectedComponents.contains(v) && selectionMoveDragOffset != null) {
                    Point2D transformedOffset = worldTx.transform(selectionMoveDragOffset, null);
                    Point2D transformedOrigin = worldTx.transform(new Point2D.Double(0, 0), null);
                    offX = (int) ((transformedOffset.getX() - transformedOrigin.getX()));
                    offY = (int) ((transformedOffset.getY() - transformedOrigin.getY()));
                    System.out.println("selectOffset: " + selectionMoveDragOffset.x + ", " + selectionMoveDragOffset.y);
                    System.out.println("offset: " + offX + ", " + offY);
                }
                v.draw(g2d, textureCache, offX, offY);
            });
        }
    }

    public void unselectComponents() {
        for (WorldComponent c : selectedComponents) {
            c.unselect();
        }
        selectedComponents.clear();
        repaint();
    }
}
