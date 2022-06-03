package de.techgamez.pleezon.gui;

import de.techgamez.pleezon.Constants;
import de.techgamez.pleezon.backend.World;
import de.techgamez.pleezon.gui.field.FieldPane;
import de.techgamez.pleezon.gui.topbar.TopBar;

import javax.swing.*;
import java.awt.*;

public class JLogicSimulatorGUI extends JFrame {

    TopBar topBar;
    FieldPane fieldPane;
    public JLogicSimulatorGUI() {
        super(Constants.APP_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920/2, 1080/2);
        this.topBar = new TopBar();
        this.fieldPane = new FieldPane();
        //this.add(topBar, BorderLayout.NORTH);
        this.add(fieldPane, BorderLayout.CENTER);


    }
}
