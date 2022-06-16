package de.techgamez.pleezon.backend;

import de.techgamez.pleezon.backend.data.ComponentMap;
import de.techgamez.pleezon.backend.data.save.BlotterInputStream;
import de.techgamez.pleezon.backend.data.save.BlotterOutputStream;
import de.techgamez.pleezon.gui.field.actions.impl.component.WorldComponent;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.UUID;

public class World {
    /*
    the location the world is saved in
     */ File file;

    private final ComponentMap components;

    private final LogicHandler logicHandler;

    public World(File location) {
        this(new ComponentMap(), location);
    }

    public long addComponent(WorldComponent component) {
        long id = 0;
        while (components.containsKey(id)) {
            id = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        }
        components.put(id, component);
        component.component.setID(id);
        return id;
    }

    public void deleteComponent(WorldComponent component) {
        components.remove(component.component.getID());
    }

    public WorldComponent[] getComponentsAt(Point2D p) {
        LinkedList<WorldComponent> componentsToSelect = new LinkedList<>();
        for (WorldComponent component : getComponents().values()) {
            if (component.isInHitbox(p, false)) {
                componentsToSelect.add(component);
            }
        }
        return componentsToSelect.toArray(new WorldComponent[0]);
    }

    public World(ComponentMap components, File saveLocation) {
        this.components = components;
        this.logicHandler = new LogicHandler(this);
        this.file = saveLocation;
    }

    /*
    Saves the world and all it's states to a file (JSON).
     */
    @SuppressWarnings("")
    public void saveWorld() throws IOException {
        file.delete();
        file.createNewFile();
        try (BlotterOutputStream bos = new BlotterOutputStream(new FileOutputStream(file))) {
            components.blot(bos);
        }
    }

    /*
    reads a world from a file (JSON).
     */
    public static World fromFile(File file) throws IOException {
        try (BlotterInputStream bis = new BlotterInputStream(new FileInputStream(file))) {
            ComponentMap components = new ComponentMap();
            components.unblot(bis);
            return new World(components, file);
        }
    }

    public ComponentMap getComponents() {
        return components;
    }

}
