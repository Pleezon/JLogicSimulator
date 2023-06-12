package de.techgamez.pleezon.gui.topbar.run;

import de.techgamez.pleezon.gui.JLogicSimulatorGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunButton extends JButton {
    JLogicSimulatorGUI gui;
    boolean running = false;

    public RunButton(JLogicSimulatorGUI gui) {
        this.gui = gui;
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                running = !running;
                if (running) {
                    gui.fieldPane.getWorld().logicHandler.nextTick();
                    gui.fieldPane.repaint();
                    setStopIcon();
                } else {
                    setStartIcon();
                }
            }
        });
        setStartIcon();
    }

    private void setStartIcon() {
        this.setText("▷");
        this.setForeground(javax.swing.UIManager.getDefaults().getColor("Actions.Green"));
    }

    private void setStopIcon() {
        this.setText("□");
        this.setForeground(javax.swing.UIManager.getDefaults().getColor("Component.custom.borderColor"));
    }
}
