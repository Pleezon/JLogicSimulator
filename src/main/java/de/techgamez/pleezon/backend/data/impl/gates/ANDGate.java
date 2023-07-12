package de.techgamez.pleezon.backend.data.impl.gates;

import de.techgamez.pleezon.backend.data.LogicGate;

public class ANDGate extends LogicGate
{


	@Override
	public String texturePath()
	{
		return "/textures/gates/AND.png";
	}

	/*
	active if all inputs are active
	 */
	@Override
	public boolean state(int activeInputs, int totalInputs)
	{
		return activeInputs == this.inputs.size();
	}
}
