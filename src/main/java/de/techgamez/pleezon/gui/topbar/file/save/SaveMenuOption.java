package de.techgamez.pleezon.gui.topbar.file.save;

import de.techgamez.pleezon.gui.JLogicSimulatorGUI;

import javax.swing.*;
import java.io.IOException;

public class SaveMenuOption extends JMenuItem {
    JLogicSimulatorGUI gui;

    public SaveMenuOption(JLogicSimulatorGUI gui) {
        super("Save");
        if (!(this.gui != null && this.gui.fieldPane != null && this.gui.fieldPane.getWorld() != null))
            this.setEnabled(false);
        this.gui = gui;
        this.addActionListener((e) -> {
            try {
                gui.fieldPane.getWorld().saveWorld();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
