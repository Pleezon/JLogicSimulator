package de.techgamez.pleezon.gui.topbar.file.open;

import de.techgamez.pleezon.backend.World;
import de.techgamez.pleezon.gui.JLogicSimulatorGUI;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class OpenMenuOption extends JMenuItem {
    JLogicSimulatorGUI gui;

    public OpenMenuOption(JLogicSimulatorGUI gui) {
        super("Open");
        this.gui = gui;
        this.addActionListener((e) -> {
            if(gui.fieldPane.world != null) {
                int result = JOptionPane.showConfirmDialog(gui, "Do you want to save the current world?", "Save current world?", JOptionPane.YES_NO_CANCEL_OPTION);
                if(result == JOptionPane.YES_OPTION) {
                    try {
                        gui.fieldPane.world.saveWorld();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            final JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Choose save location");
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setAcceptAllFileFilterUsed(false);
            fc.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(java.io.File f) {
                    return f.getName().endsWith(".jls");
                }

                @Override
                public String getDescription() {
                    return "Logic save files (*.jls)";
                }
            });
            if (fc.showOpenDialog(gui) == JFileChooser.APPROVE_OPTION) {
                try {
                    String path = fc.getSelectedFile().getAbsolutePath();
                    if(!path.endsWith(".jls")) path += ".jls";
                    World w = World.fromFile(new File(path));
                    gui.fieldPane.setWorld(w);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
