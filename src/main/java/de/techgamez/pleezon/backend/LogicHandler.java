package de.techgamez.pleezon.backend;

import de.techgamez.pleezon.backend.data.LogicComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LogicHandler {
    /*
    The logic handler is responsible for updating the state of the components.
     */


    /*
    The world that is being updated.
     */
    private final World world;
    /*
    The list of components that are currently in queue for being updated - with the amount of ticks in which they
    need to be updated, respectfully.
     */
    private final HashMap<Integer, ArrayList<Long>> updateQueue;

    public LogicHandler(World world) {
        this.world = world;
        updateQueue = new HashMap<>();
    }

    /*
    Updates a component and adds all it's children to the queue, with their respectful amount of ticks.
     */
    public void updateFrom(long id) {
        LogicComponent component = world.getComponents().get(id).component;
        if (component != null) {
            int ticks = component.triggerUpdate(this.world);
            if (ticks > 0) {
                ArrayList<Long> l = updateQueue.getOrDefault(ticks, new ArrayList<>());
                l.addAll(component.getOuputs());
                updateQueue.put(ticks, l);
            }
        }
    }

    /*
    Method that is called every tick.
    It updates all components that have to be updated (queue-ticks > 0)
     */
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
