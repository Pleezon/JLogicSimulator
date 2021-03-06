package de.techgamez.pleezon.backend.data;

import de.techgamez.pleezon.backend.World;


public abstract class LogicGate extends LogicComponent {


    public int maxInputs() {
        return Integer.MAX_VALUE;
    }

    public int maxOutputs() {
        return Integer.MAX_VALUE;
    }

    public boolean hasOutputs() {
        return true;
    }

    public abstract boolean state(int activeInputs, int totalInputs);

    /*
    handling of a logic-gate's update-method; Made easier because of logic gates
    only needing the amount of active and total inputs to compute their state
     */
    public int triggerUpdate(World world) {
        int amoActive = 0;
        for (long input : this.inputs) {
            if (world.getComponents().get(input).component.getState()) {
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
