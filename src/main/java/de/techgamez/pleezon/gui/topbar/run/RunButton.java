package de.techgamez.pleezon.gui.topbar.run;

import de.techgamez.pleezon.gui.JLogicSimulatorGUI;

import javax.swing.*;

public class RunButton extends JButton {
    JLogicSimulatorGUI gui;
    public RunButton(JLogicSimulatorGUI gui) {
        super("▷");
        this.gui = gui;
        this.setForeground(javax.swing.UIManager.getDefaults().getColor("Actions.Green"));
    }
}
