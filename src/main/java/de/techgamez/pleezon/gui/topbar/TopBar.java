package de.techgamez.pleezon.gui.topbar;

import de.techgamez.pleezon.gui.topbar.file.FileButton;
import de.techgamez.pleezon.gui.topbar.file.FileButtonMenu;
import de.techgamez.pleezon.gui.topbar.run.RunButton;

import javax.swing.*;
import java.awt.*;

public class TopBar extends JToolBar {
    FileButton fileButton;
    RunButton runButton;
    public TopBar() {
        super();
        this.setBorderPainted(true);
        this.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0,
                        javax.swing.UIManager.getDefaults().getColor("Separator.background")),
                javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0,
                        javax.swing.UIManager.getDefaults().getColor("Separator.foreground"))));
        fileButton = new FileButton();
        runButton = new RunButton();
        this.add(new JToolBar.Separator(new Dimension(20, 0)));
        this.add(runButton);
        this.add(new JToolBar.Separator(new Dimension(40, 20)));
        this.add(fileButton);
        this.add(new JToolBar.Separator(new Dimension(40, 20)));

    }
}
