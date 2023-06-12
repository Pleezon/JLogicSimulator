package de.techgamez.pleezon.gui.field.handler;

import de.techgamez.pleezon.gui.field.FieldPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;

public abstract class ActionHandler {

    public abstract void draw(Graphics2D g2d);

    public abstract MouseAdapter mouseAdapter();

    public abstract KeyAdapter keyAdapter();

    public void registerInputs(InputMap map) {

    }

    public void registerActions(ActionMap map) {

    }

    protected final FieldPane gui;

    public ActionHandler(FieldPane gui) {
        this.gui = gui;
    }

}
