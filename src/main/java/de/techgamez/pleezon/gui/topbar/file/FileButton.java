package de.techgamez.pleezon.gui.topbar.file;

import javax.swing.*;

public class FileButton extends JButton {

    FileButtonMenu menu;
    public FileButton() {
        super();
        menu = new FileButtonMenu();
        this.setText("File");
        this.addActionListener(evt -> {
            menu.show(this, -20, this.getHeight());
        });
    }
}
