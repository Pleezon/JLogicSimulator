package de.techgamez.pleezon.backend;

import com.google.gson.Gson;
import de.techgamez.pleezon.backend.data.LogicComponent;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class World {

    public static class WorldComponent {
        public LogicComponent component;
        int x;
        int y;

        public WorldComponent(LogicComponent component, int x, int y) {
            this.component = component;
            this.x = x;
            this.y = y;
        }
    }

    public HashMap<Long, WorldComponent> components;


    private LogicHandler logicHandler;

    public World() {
        this(new HashMap<>());
    }

    public World(HashMap<Long, WorldComponent> components) {
        this.components = components;
        this.logicHandler = new LogicHandler(this);
    }



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
