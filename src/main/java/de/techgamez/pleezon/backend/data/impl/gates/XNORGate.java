package de.techgamez.pleezon.backend.data.impl.gates;

import de.techgamez.pleezon.backend.data.LogicGate;

public class XNORGate extends LogicGate {


    @Override
    public String texturePath() {
        return "/textures/gates/XNOR.png";
    }

    /*
    active if an even number of inputs are active
     */
    @Override
    public boolean state(int activeInputs, int totalInputs) {
        return (activeInputs & 1) == 0;
    }
}
