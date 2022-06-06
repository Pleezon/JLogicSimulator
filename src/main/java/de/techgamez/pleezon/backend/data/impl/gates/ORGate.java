package de.techgamez.pleezon.backend.data.impl.gates;

import de.techgamez.pleezon.backend.data.LogicGate;

public class ORGate extends LogicGate {


    @Override
    public String texturePath() {
        return "/textures/gates/OR.png";

    }

    /*
    active if any input is active
     */
    @Override
    public boolean state(int activeInputs, int totalInputs) {
        return activeInputs > 0;
    }
}
