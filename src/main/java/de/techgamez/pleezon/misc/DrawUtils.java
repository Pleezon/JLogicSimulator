package de.techgamez.pleezon.misc;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class DrawUtils {
    public static Polygon BASIC_ARROW_HEAD = new Polygon();

    static {
        BASIC_ARROW_HEAD.addPoint(0, 0);
        BASIC_ARROW_HEAD.addPoint(-5, -10);
        BASIC_ARROW_HEAD.addPoint(5, -10);
    }

    public static void drawConnection(Graphics2D graphics2D, int x0, int x1, int y0, int y1, Polygon arrowHead, Color color, float thickness, int arrowDist) {
        double angle = Math.atan2(y1 - y0, x1 - x0);
        double steepness = Math.tan(angle);
        graphics2D.setColor(color);
        graphics2D.setStroke(new BasicStroke(thickness));
        graphics2D.drawLine(x0, y0, (int) (x1 - 10 * Math.cos(angle)), (int) (y1 - 10 * Math.sin(angle)));
        /*double addend = y0 - steepness * (startX);
        double inversedSteepness = -1 * steepness;
        double inversedAddend = -1 * addend;*/
        double incrementX = arrowDist / Math.sqrt(Math.pow(steepness, 2) + 1);
        double incrementY = steepness * incrementX;
        int cnt = 1;
        // x0 < x1 true when on right
        for (double x_ = Math.min(x0, x1) + incrementX; x_ < Math.max(x0, x1) - incrementX; x_ += incrementX) {
            double y_ = (x0 < x1 ? y0 : y1) + cnt * incrementY;
            cnt++;
            drawArrowHead(graphics2D, (int) x_, (int) y_, angle, arrowHead);
        }

        drawArrowHead(graphics2D, x1, y1, angle, arrowHead);
    }

    public static void drawArrowHead(Graphics2D graphics2D, int x, int y, double angle, Polygon head) {
        AffineTransform tx1 = graphics2D.getTransform();
        AffineTransform tx2 = (AffineTransform) tx1.clone();
        tx2.translate(x, y);
        tx2.rotate(angle - Math.PI / 2);
        graphics2D.setTransform(tx2);
        graphics2D.fill(head);
        graphics2D.setTransform(tx1);
    }
}
