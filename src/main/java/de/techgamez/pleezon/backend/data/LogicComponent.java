package de.techgamez.pleezon.backend.data;

import de.techgamez.pleezon.backend.World;
import de.techgamez.pleezon.backend.data.save.Blottable;
import de.techgamez.pleezon.backend.data.save.BlotterInputStream;
import de.techgamez.pleezon.backend.data.save.BlotterOutputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public abstract class LogicComponent implements Blottable {
    private boolean state;
    protected ArrayList<Long> inputs;
    protected ArrayList<Long> outputs;

    private long id;

    public long getID() {
        return this.id;
    }

    public void setID(long id) {
        this.id = id;
    }

    public LogicComponent(ArrayList<Long> inputs, ArrayList<Long> outputs, boolean state, long id) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.state = state;
        this.id = id;
    }

    public LogicComponent() {
        this(new ArrayList<>(), new ArrayList<>(), false, 0);
    }

    public LogicComponent(long id) {
        this(new ArrayList<>(), new ArrayList<>(), false, id);
    }

    @Override
    public void blot(BlotterOutputStream out) throws IOException {
        out.writeBoolean(state);
        out.writeLong(id);
        out.writeInt(inputs.size());
        for (Long input : inputs) {
            out.writeLong(input);
        }
        out.writeInt(outputs.size());
        for (Long output : outputs) {
            out.writeLong(output);
        }
    }

    @Override
    public void unblot(BlotterInputStream in) throws IOException {
        state = in.readBoolean();
        id = in.readLong();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            inputs.add(in.readLong());
        }
        size = in.readInt();
        for (int i = 0; i < size; i++) {
            outputs.add(in.readLong());
        }
    }

    /*
     * Updates the component and returns -1 if the state hasn't changed, otherwise returns the amount of ticks in which to update it's kids
     */
    public abstract int triggerUpdate(boolean[] inputs);

    public abstract int triggerClick(World world);

    public abstract int maxInputs();

    public abstract int maxOutputs();

    public abstract String texturePath();


    public boolean getState() {
        return state;
    }

    protected void setState(boolean state) {
        this.state = state;
    }

    private void checkInputModification() throws IllegalStateModificationException {
        if (maxInputs() == 0) {
            throw new IllegalStateModificationException("This component has no inputs");
        }
    }

    private void checkOutputModification() throws IllegalStateModificationException {
        if (maxOutputs() == 0) {
            throw new IllegalStateModificationException("This component has no outputs");
        }
    }

    public void addInput(long input) throws IllegalStateModificationException {
        checkInputModification();
        this.inputs.add(input);
    }

    public boolean hasInput(long input) {
        return this.inputs.contains(input);
    }

    public boolean hasOutput(long input) {
        return this.outputs.contains(input);
    }

    public void removeInput(long input) throws IllegalStateModificationException {
        checkInputModification();
        this.inputs.remove(input);
    }

    public void addOutput(long output) throws IllegalStateModificationException {
        checkOutputModification();
        this.outputs.add(output);
    }

    public void removeOutput(long output) throws IllegalStateModificationException {
        checkOutputModification();
        this.outputs.remove(output);
    }

    public ArrayList<Long> getOuputs() {
        return this.outputs;
    }

    public ArrayList<Long> getInputs() {
        return this.outputs;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogicComponent component = (LogicComponent) o;
        return state == component.state && inputs.equals(component.inputs) && outputs.equals(component.outputs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, inputs, outputs);
    }
}
