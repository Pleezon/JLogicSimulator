package de.techgamez.pleezon.gui.field.component;

import de.techgamez.pleezon.backend.data.LogicComponent;
import de.techgamez.pleezon.backend.data.save.Blottable;
import de.techgamez.pleezon.backend.data.save.BlotterInputStream;
import de.techgamez.pleezon.backend.data.save.BlotterOutputStream;
import de.techgamez.pleezon.gui.field.FieldPane;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Objects;

public class WorldComponent implements Blottable {
    public LogicComponent component;
    public double x;
    public double y;

    public enum ComponentState {
        NORMAL,
        SELECTED
    }

    public ComponentState state;

    public WorldComponent(LogicComponent component, int x, int y) {
        this.component = component;
        this.x = x;
        this.y = y;
        this.state = ComponentState.NORMAL;
    }

    public boolean checkForSelect(int selectionStartX, int selectionStartY, int selectionEndX, int selectionEndY) {
        if (selectionStartX <= x && x <= selectionEndX && selectionStartY <= y && y <= selectionEndY) {
            state = ComponentState.SELECTED;
            return true;
        }
        return false;
    }

    public void unselect() {
        state = ComponentState.NORMAL;
    }


    public BufferedImage texture() throws IOException {
        BufferedImage texture = ImageIO.read(Objects.requireNonNull(getClass().getResource(component.texturePath())));
        Color c = this.state == ComponentState.SELECTED ? javax.swing.UIManager.getDefaults().getColor("Component.focusColor") :
                javax.swing.UIManager.getDefaults().getColor("Component.linkColor");
        for (int x = 0; x < texture.getWidth(); x++) {
            for (int y = 0; y < texture.getHeight(); y++) {
                int argb = texture.getRGB(x, y);
                double medium = ((((argb & 0xFF) + ((argb >> 8) & 0xFF) + ((argb >> 16) & 0xFF)) / 3.0) / 255.0);
                int newARGB = 0;
                newARGB |= (int) (medium * c.getRed());
                newARGB |= (int) (medium * c.getGreen()) << 8;
                newARGB |= (int) (medium * c.getBlue()) << 16;
                newARGB |= (((argb >> 24) & 0xff) << 24);
                texture.setRGB(x, y, newARGB);
            }
        }
        return texture;
    }


    public void draw(Graphics2D g, HashMap<FieldPane.CacheKey, BufferedImage> textureCache, int offX, int offY) {
        try {
            BufferedImage texture;
            FieldPane.CacheKey key = new FieldPane.CacheKey(this.getClass(), state);
            if (textureCache.containsKey(key)) {
                texture = textureCache.get(key);
            } else {
                texture = texture();
                textureCache.put(key, texture);
            }
            g.drawImage(texture, null, (int) x - offX, (int) y - offY);
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
