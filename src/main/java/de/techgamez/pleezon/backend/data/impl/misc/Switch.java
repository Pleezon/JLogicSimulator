package de.techgamez.pleezon.backend.data.impl.misc;

import de.techgamez.pleezon.backend.World;
import de.techgamez.pleezon.backend.data.LogicComponent;

public class Switch extends LogicComponent {

    public Switch() {
        super();
    }

    @Override
    public int triggerUpdate(World world) {
        return -1;
    }

    @Override
    public int triggerClick(World world) {
        setState(!getState());
        return 1;
    }

    @Override
    public boolean hasInputs() {
        return false;
    }

    @Override
    public boolean hasOutputs() {
        return true;
    }

    @Override
    public String texturePath() {
        return null;
    }


}

