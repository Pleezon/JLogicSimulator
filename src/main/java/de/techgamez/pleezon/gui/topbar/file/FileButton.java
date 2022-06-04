package de.techgamez.pleezon.gui.topbar.file;

import de.techgamez.pleezon.gui.JLogicSimulatorGUI;

import javax.swing.*;

public class FileButton extends JButton {

    public FileButtonMenu menu;
    JLogicSimulatorGUI gui;
    public FileButton(JLogicSimulatorGUI gui) {
        super();
        this.gui = gui;
        menu = new FileButtonMenu(gui);
        this.setText("File");
        this.addActionListener(evt -> {
            menu.show(this, -20, this.getHeight());
        });
    }
}
