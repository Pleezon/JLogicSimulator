package de.techgamez.pleezon.backend;

import com.google.gson.Gson;
import de.techgamez.pleezon.backend.data.LogicComponent;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class World {
    /*
    * The world is a collection of components.
     */


    /*
    * Wrapper class for LogicComponent that also includes it's position relative to the world (for rendering)
     */
    public static class WorldComponent {
        public LogicComponent component;
        double x;
        double y;

        public WorldComponent(LogicComponent component, int x, int y) {
            this.component = component;
            this.x = x;
            this.y = y;
        }
    }

    /*
    a mapping of an ID to every component in the world.
     */
    public HashMap<Long, WorldComponent> components;

    /*
    Instance for handling any kind of logic.
     */
    private final LogicHandler logicHandler;

    public World() {
        this(new HashMap<>());
    }

    public World(HashMap<Long, WorldComponent> components) {
        this.components = components;
        this.logicHandler = new LogicHandler(this);
    }

    /*
    Saves the world and all it's states to a file (JSON).
     */
    @SuppressWarnings("")
    public void saveWorld(File file) throws IOException {
        file.delete();
        file.createNewFile();
        try (FileWriter fileWriter = new FileWriter(file)) {
            String fileContent = new Gson().toJson(this);
            fileWriter.write(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
    reads a world from a file (JSON).
     */
    public static World fromFile(File file) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            StringBuilder res = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                res.append(line);
            }
            return new Gson().fromJson(res.toString(), World.class);
        }
    }

}
