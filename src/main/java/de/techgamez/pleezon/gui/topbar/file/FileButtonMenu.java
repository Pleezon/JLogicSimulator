package de.techgamez.pleezon.gui.topbar.file;

import de.techgamez.pleezon.gui.topbar.file.create.CreateMenuOption;
import de.techgamez.pleezon.gui.topbar.file.open.OpenMenuOption;
import de.techgamez.pleezon.gui.topbar.file.save.SaveMenuOption;

import javax.swing.*;
import java.sql.Savepoint;

public class FileButtonMenu extends JPopupMenu {

    CreateMenuOption createMenuOption;
    SaveMenuOption saveMenuOption;
    OpenMenuOption openMenuOption;

    public FileButtonMenu() {
        super();
        createMenuOption = new CreateMenuOption();
        saveMenuOption = new SaveMenuOption();
        openMenuOption = new OpenMenuOption();


        this.add(createMenuOption);
        this.add(openMenuOption);
        this.add(saveMenuOption);

    }
}
