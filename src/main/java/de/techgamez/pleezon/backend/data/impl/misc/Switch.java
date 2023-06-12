package de.techgamez.pleezon.backend.data.impl.misc;

import de.techgamez.pleezon.backend.World;
import de.techgamez.pleezon.backend.data.LogicComponent;

public class Switch extends LogicComponent {

    public Switch() {
        super();
    }

    @Override
    public int triggerUpdate(boolean[] inputs) {
        return -1;
    }

    @Override
    public int triggerClick(World world) {
        setState(!getState());
        return 1;
    }

    @Override
    public int maxInputs() {
        return 0;
    }

    @Override
    public int maxOutputs() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String texturePath() {
        return "/textures/misc/Switch.png";

    }
}

