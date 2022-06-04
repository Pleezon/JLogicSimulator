package de.techgamez.pleezon.backend;

import de.techgamez.pleezon.backend.data.ComponentMap;
import de.techgamez.pleezon.backend.data.save.BlotterInputStream;
import de.techgamez.pleezon.backend.data.save.BlotterOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class World {
    /*
     * The world is a collection of components.
     */

    /*
    the location the world is saved in
     */ File file;

    /*
     * Wrapper class for LogicComponent that also includes it's position relative to the world (for rendering)
     */

    /*
    a mapping of an ID to every component in the world.
     */

    public ComponentMap components;

    /*
    Instance for handling any kind of logic.
     */
    private final LogicHandler logicHandler;

    public World(File location) {
        this(new ComponentMap(), location);
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

}
