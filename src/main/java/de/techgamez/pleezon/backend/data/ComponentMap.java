package de.techgamez.pleezon.backend.data;

import de.techgamez.pleezon.backend.data.save.Blottable;
import de.techgamez.pleezon.backend.data.save.BlotterInputStream;
import de.techgamez.pleezon.backend.data.save.BlotterOutputStream;
import de.techgamez.pleezon.gui.field.actions.impl.component.WorldComponent;

import java.io.IOException;
import java.util.HashMap;

public class ComponentMap extends HashMap<Long, WorldComponent> implements Blottable {
    public ComponentMap() {
        super();
    }

    @Override
    public void blot(BlotterOutputStream out) throws IOException {
        out.writeInt(size());
        for (Entry<Long, WorldComponent> entry : this.entrySet()) {
            out.writeLong(entry.getKey());
            entry.getValue().blot(out);
        }
    }

    @Override
    public void unblot(BlotterInputStream in) throws IOException {
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            long key = in.readLong();
            WorldComponent value = new WorldComponent(null, 0, 0);
            value.unblot(in);
            put(key, value);
        }
    }
}