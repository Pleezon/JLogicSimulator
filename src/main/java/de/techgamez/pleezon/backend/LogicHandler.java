package de.techgamez.pleezon.backend;
import de.techgamez.pleezon.backend.data.LogicComponent;
import java.util.*;

public class LogicHandler {
    private final World world;
    private final HashMap<Integer, ArrayList<Long>> updateQueue;

    public LogicHandler(World world) {
        this.world = world;
        updateQueue = new HashMap<>();
    }


    public void updateFrom(long id) {
        LogicComponent component = world.components.get(id).component;
        if (component != null) {
            int ticks = component.triggerUpdate(this.world);
            if (ticks > 0) {
                ArrayList<Long> l = updateQueue.getOrDefault(ticks, new ArrayList<>());
                l.addAll(component.getOuputs());
                updateQueue.put(ticks, l);
            }
        }
    }

    public void nextTick() {
        for (Map.Entry<Integer, ArrayList<Long>> e : updateQueue.entrySet()) {
            if (e.getKey() == 0) {
                e.getValue().forEach(this::updateFrom);
                updateQueue.remove(e.getKey());
                continue;
            }
            updateQueue.remove(e.getKey());
            updateQueue.put(e.getKey() - 1, e.getValue());
        }
    }
}
