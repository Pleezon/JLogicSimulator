package de.techgamez.pleezon.backend.data.impl.gates;

import de.techgamez.pleezon.backend.data.LogicGate;

public class NANDGate extends LogicGate {


    @Override
    public String texturePath() {
        return "/textures/gates/NAND.png";
    }

    /*
    active if any input is inactive
     */
    @Override
    public boolean state(int activeInputs, int totalInputs) {
        return activeInputs != totalInputs;
    }
}
