package de.techgamez.pleezon;

import com.formdev.flatlaf.FlatDarkLaf;
import de.techgamez.pleezon.gui.JLogicSimulatorGUI;

public class JLogicSimulator {

    /*
       Entry Point.
     */
    public static void main(String[] args) {
        new JLogicSimulator();
    }

    JLogicSimulatorGUI gui;
    public JLogicSimulator(){
        FlatDarkLaf.setup();
        gui = new JLogicSimulatorGUI();
        gui.setVisible(true);
    }
}
