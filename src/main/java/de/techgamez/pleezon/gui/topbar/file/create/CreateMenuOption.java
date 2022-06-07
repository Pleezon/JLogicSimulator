package de.techgamez.pleezon.gui.topbar.file.create;

import de.techgamez.pleezon.backend.World;
import de.techgamez.pleezon.gui.JLogicSimulatorGUI;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class CreateMenuOption extends JMenuItem {
    JLogicSimulatorGUI gui;

    public CreateMenuOption(JLogicSimulatorGUI gui) {
        super("Create");
        this.gui = gui;
        this.addActionListener((e) -> {
            if (gui.fieldPane.getWorld() != null) {
                int result = JOptionPane.showConfirmDialog(gui, "Do you want to save the current world?", "Save current world?", JOptionPane.YES_NO_CANCEL_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        gui.fieldPane.getWorld().saveWorld();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            final JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Choose save location");
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setAcceptAllFileFilterUsed(false);
            if (fc.showSaveDialog(gui) == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                File f = null;
                while (f == null || f.exists()) {
                    String name = JOptionPane.showInputDialog("Enter world name", "World");
                    path += "/" + name;
                    if (!path.endsWith(".jls")) path += ".jls";
                    f = new File(path);
                    if (f.exists()) {
                        JOptionPane.showMessageDialog(gui, "File already exists", "Error", JOptionPane.ERROR_MESSAGE);
                        path = fc.getSelectedFile().getAbsolutePath();
                    } else {
                        boolean b = false;
                        try {
                            b = f.createNewFile();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        if (!b) {
                            JOptionPane.showMessageDialog(gui, "File location invalid", "Error", JOptionPane.ERROR_MESSAGE);
                            path = fc.getSelectedFile().getAbsolutePath();
                        } else {
                            break;
                        }
                    }
                }
                World w = new World(f);
                gui.fieldPane.setWorld(w);
                try {
                    w.saveWorld();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
