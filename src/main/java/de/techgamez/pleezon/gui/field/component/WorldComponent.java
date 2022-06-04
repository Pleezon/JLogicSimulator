package de.techgamez.pleezon.gui.field.component;

import de.techgamez.pleezon.backend.data.LogicComponent;
import de.techgamez.pleezon.backend.data.save.Blottable;
import de.techgamez.pleezon.backend.data.save.BlotterInputStream;
import de.techgamez.pleezon.backend.data.save.BlotterOutputStream;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class WorldComponent implements Blottable {
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

    @Override
    public void blot(BlotterOutputStream out) throws IOException {
        out.writeString(component.getClass().getName());
        component.blot(out);
        out.writeDouble(x);
        out.writeDouble(y);
    }

    @Override
    public void unblot(BlotterInputStream in) throws IOException {
        try {
            component = (LogicComponent) Class.forName(in.readString()).getDeclaredConstructor().newInstance();
            component.unblot(in);
            x = in.readDouble();
            y = in.readDouble();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
