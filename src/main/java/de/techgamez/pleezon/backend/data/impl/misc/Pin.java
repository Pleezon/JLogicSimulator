package de.techgamez.pleezon.backend.data.impl.misc;

import de.techgamez.pleezon.backend.World;
import de.techgamez.pleezon.backend.data.LogicComponent;

public class Pin extends LogicComponent {
    @Override
    public int triggerUpdate(World world) {
        for (Long input : this.inputs) {
            LogicComponent c = world.getComponents().get(input).component;
            if (c.getState()) {
                if (!this.getState()) {
                    setState(true);
                    return 0;
                }
            } else {
                if (this.getState()) {
                    setState(false);
                    return 0;
                }
            }
        }
        return -1;
    }

    @Override
    public int triggerClick(World world) {
        return 0;
    }

    @Override
    public int maxInputs() {
        return 1;
    }

    @Override
    public int maxOutputs() {
        return 1;
    }

    @Override
    public String texturePath() {
        return "/textures/misc/Pin.png";
    }
}
