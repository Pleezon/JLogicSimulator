package de.techgamez.pleezon.backend;

import de.techgamez.pleezon.backend.data.LogicComponent;
import de.techgamez.pleezon.backend.data.save.Blottable;
import de.techgamez.pleezon.backend.data.save.BlotterInputStream;
import de.techgamez.pleezon.backend.data.save.BlotterOutputStream;
import de.techgamez.pleezon.gui.field.handler.impl.component.WorldComponent;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LogicHandler implements Blottable {
    /*
    The logic handler is responsible for updating the state of the components.
     */


    /*
    The world that is being updated.
     */
    private World world;
    /*
    The list of components that are currently in queue for being updated - with the amount of ticks in which they
    need to be updated, respectfully.
     */
    private final ConcurrentHashMap<Integer, HashSet<Long>> updateQueue;

    public LogicHandler(World world) {
        this.world = world;
        updateQueue = new ConcurrentHashMap<>();
    }

    public LogicHandler() {
        updateQueue = new ConcurrentHashMap<>();
    }

    public void setWorld(World world) {
        this.world = world;
    }

    /*
    Updates a component and adds all it's children to the queue, with their respectful amount of ticks.
     */
    public void updateFrom(LogicComponent component, boolean[] ins) {
        if (component != null) {
            int ticks = component.triggerUpdate(ins);
            if (ticks >= 0) {
                updateChildren(component, ticks);
            }
        }
    }

    /*
    Adds a component's children to the updateQueue
     */
    public void updateChildren(LogicComponent component, int inTicks) {
        HashSet<Long> l = updateQueue.getOrDefault(inTicks, new HashSet<>());
        l.addAll(component.getOuputs());
        updateQueue.put(inTicks, l);
    }

    public void updateFrom(long id, boolean[] ins) {
        updateFrom(world.getComponents().get(id).component, ins);
        System.out.println("got component to tick.");
    }

    /*
    Method that is called every tick.
    It updates all components that have to be updated (queue-ticks > 0)
     */
    public void nextTick() {
        System.out.println("TICKING!");
        HashMap<Long, boolean[]> inMapping = new HashMap<>();
        for (WorldComponent w : this.world.getComponents().values()) {
            boolean[] ins = new boolean[w.component.getInputs().size()];
            for (int i = 0; i < w.component.getInputs().size(); i++) {
                long inputID = w.component.getInputs().get(i);
                ins[i] = world.getComponents().get(inputID).component.getState();
            }
            inMapping.put(w.component.getID(), ins);
        }

        for (Map.Entry<Integer, HashSet<Long>> e : updateQueue.entrySet()) {
            if (e.getKey() == 0) {
                e.getValue().forEach((c) -> updateFrom(c, inMapping.get(c)));
                updateQueue.remove(e.getKey());
                continue;
            }
            updateQueue.remove(e.getKey());
            updateQueue.put(e.getKey() - 1, e.getValue());
        }
    }

    public void blot(BlotterOutputStream bos) throws IOException {
        bos.writeLong(updateQueue.size());
        for (Map.Entry<Integer, HashSet<Long>> entry : updateQueue.entrySet()) {
            bos.writeInt(entry.getKey());
            bos.writeLong(entry.getValue().size());
            for (Long elem : entry.getValue().toArray(new Long[0])) {
                bos.writeLong(elem);
            }
        }
    }

    public void unblot(BlotterInputStream bis) throws IOException {
        long queueLen = bis.readLong();
        for (int i = 0; i < queueLen; i++) {
            int ticks = bis.readInt();
            long setSize = bis.readLong();
            HashSet<Long> set = new HashSet<>();
            for (int i_ = 0; i_ < setSize; i_++) {
                Long setEntry = bis.readLong();
                set.add(setEntry);
            }
            updateQueue.put(ticks, set);
        }
    }
}
