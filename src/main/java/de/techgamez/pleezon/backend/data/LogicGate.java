package de.techgamez.pleezon.backend.data;

import de.techgamez.pleezon.backend.World;

import java.util.ArrayList;

public abstract class LogicGate extends LogicComponent {

    public boolean hasInputs(){
        return true;
    }
    public boolean hasOutputs(){
        return true;
    }

    public abstract boolean state(int activeInputs, int totalInputs);
    public int triggerUpdate(World world){
        int amoActive = 0;
        for (long input : this.inputs) {
            if (world.components.get(input).component.getState()) {
                amoActive++;
            }
        }
        if (this.state(amoActive, this.inputs.size())) {
            if (this.getState()) return -1;
            setState(true);
        } else {
            if (!this.getState()) return -1;
            setState(false);
        }
        return 1;
    }
    /*
    logic gates don't have onClick behavior.
     */
    @Override
    public int triggerClick(World world) {
        return -1;
    }

}
