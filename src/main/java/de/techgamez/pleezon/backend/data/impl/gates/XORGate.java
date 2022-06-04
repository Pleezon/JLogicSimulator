package de.techgamez.pleezon.backend.data.impl.gates;

import de.techgamez.pleezon.backend.data.LogicGate;

public class XORGate extends LogicGate {

    @Override
    public String texturePath() {
        return "/textures/gates/XOR.png";
    }

    /*
     * active if an odd number of inputs are active
     */

    @Override
    public boolean state(int activeInputs, int totalInputs) {
        return (activeInputs & 1) == 1;
    }
}
