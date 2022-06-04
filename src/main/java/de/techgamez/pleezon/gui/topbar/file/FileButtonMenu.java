package de.techgamez.pleezon.gui.topbar.file;

import de.techgamez.pleezon.gui.JLogicSimulatorGUI;
import de.techgamez.pleezon.gui.topbar.file.create.CreateMenuOption;
import de.techgamez.pleezon.gui.topbar.file.open.OpenMenuOption;
import de.techgamez.pleezon.gui.topbar.file.save.SaveMenuOption;

import javax.swing.*;
import java.sql.Savepoint;

public class FileButtonMenu extends JPopupMenu {

    public CreateMenuOption createMenuOption;
    public SaveMenuOption saveMenuOption;
    public OpenMenuOption openMenuOption;
    JLogicSimulatorGUI gui;
    public FileButtonMenu(JLogicSimulatorGUI gui) {
        super();
        this.gui = gui;
        createMenuOption = new CreateMenuOption(gui);
        saveMenuOption = new SaveMenuOption(gui);
        openMenuOption = new OpenMenuOption(gui);


        this.add(createMenuOption);
        this.add(openMenuOption);
        this.add(saveMenuOption);

    }
}
