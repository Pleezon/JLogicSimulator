package de.techgamez.pleezon.backend.data.impl.gates;

import de.techgamez.pleezon.backend.data.LogicGate;

public class NORGate extends LogicGate {

    @Override
    public String texturePath() {
        return "/textures/gates/NOR.png";
    }

    /*
    active if all the inputs are inactive
     */
    @Override
    public boolean state(int activeInputs, int totalInputs) {
        return activeInputs == 0;
    }
}
