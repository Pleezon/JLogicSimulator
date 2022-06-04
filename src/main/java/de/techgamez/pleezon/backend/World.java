package de.techgamez.pleezon.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.techgamez.pleezon.backend.data.ComponentMap;
import de.techgamez.pleezon.backend.data.impl.gates.ANDGate;
import de.techgamez.pleezon.gui.field.component.WorldComponent;

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
        components.put(0L, new WorldComponent(new ANDGate(), 100, 100));
    }

    /*
    Saves the world and all it's states to a file (JSON).
     */
    @SuppressWarnings("")
    public void saveWorld() throws IOException {
        file.delete();
        file.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(fos, components);
        }
    }

    /*
    reads a world from a file (JSON).
     */
    public static World fromFile(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ObjectMapper objectMapper = new ObjectMapper();
            String str = new String(fis.readAllBytes());
            return new World(objectMapper.readValue(str, ComponentMap.class), file);
        }
    }

}
