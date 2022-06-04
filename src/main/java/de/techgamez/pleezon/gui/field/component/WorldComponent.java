package de.techgamez.pleezon.gui.field.component;

import de.techgamez.pleezon.backend.data.LogicComponent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

public class WorldComponent {
    public LogicComponent component;
    public double x;
    public double y;


    public WorldComponent(LogicComponent component, int x, int y) {
        this.component = component;
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g, HashMap<Class<? extends WorldComponent>, BufferedImage> textureCache) {

        try {
            BufferedImage texture;
            if (textureCache.containsKey(this.getClass())) {
                texture = textureCache.get(this.getClass());
            } else {
                texture = component.texture();
                textureCache.put(this.getClass(), texture);
            }
            g.drawImage(texture, null, (int) x, (int) y);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
