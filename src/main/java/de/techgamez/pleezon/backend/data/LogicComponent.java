package de.techgamez.pleezon.backend.data;

import de.techgamez.pleezon.backend.World;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public abstract class LogicComponent {
    private boolean state;

    protected ArrayList<Long> inputs;
    protected ArrayList<Long> outputs;

    public LogicComponent(ArrayList<Long> inputs, ArrayList<Long> outputs, boolean state) {
        this.inputs = inputs;
        this.outputs = outputs;
        this.state = state;
    }

    public LogicComponent() {
        this(new ArrayList<>(), new ArrayList<>(), false);
    }

    /*
        * Updates the component and returns -1 if the state hasn't changed, otherwise returns the amount of ticks in which to update it's kids
     */
    public abstract int triggerUpdate(World world);

    public abstract int triggerClick(World world);

    public abstract boolean hasInputs();

    public abstract boolean hasOutputs();

    protected abstract String texturePath();

    public BufferedImage texture() throws IOException {
        return ImageIO.read(Objects.requireNonNull(getClass().getResource(texturePath())));
    }

    public boolean getState() {
        return state;
    }

    protected void setState(boolean state) {
        this.state = state;
    }

    private void checkInputModification() throws IllegalStateModificationException {
        if (!hasInputs()) {
            throw new IllegalStateModificationException("This component has no inputs");
        }
    }

    private void checkOutputModification() throws IllegalStateModificationException {
        if (!hasOutputs()) {
            throw new IllegalStateModificationException("This component has no outputs");
        }
    }

    public void addInput(long input) throws IllegalStateModificationException {
        checkInputModification();
        this.inputs.add(input);
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



}