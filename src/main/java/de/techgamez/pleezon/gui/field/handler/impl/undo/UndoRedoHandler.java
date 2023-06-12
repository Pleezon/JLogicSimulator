package de.techgamez.pleezon.gui.field.handler.impl.undo;

import de.techgamez.pleezon.Constants;
import de.techgamez.pleezon.gui.field.FieldPane;
import de.techgamez.pleezon.gui.field.handler.ActionHandler;
import de.techgamez.pleezon.misc.SizedStack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

public class UndoRedoHandler extends ActionHandler {
    SizedStack<UndoableRedoable> undoStack = new SizedStack<>(Constants.undoRedoCap);
    SizedStack<UndoableRedoable> redoStack = new SizedStack<>(Constants.undoRedoCap);

    public UndoRedoHandler(FieldPane gui) {
        super(gui);
    }

    @Override
    public void draw(Graphics2D g2d) {

    }

    @Override
    public MouseAdapter mouseAdapter() {
        return null;
    }

    @Override
    public KeyAdapter keyAdapter() {
        return null;
    }

    public void addAction(UndoableRedoable action) {
        undoStack.push(action);
    }

    @Override
    public void registerInputs(InputMap map) {
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK), "undo");
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK), "redo");
    }

    @Override
    public void registerActions(ActionMap map) {
        map.put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoStack.size() > 0) {
                    UndoableRedoable undoable = undoStack.pop();
                    undoable.undo();
                    redoStack.push(undoable);
                    gui.repaint();
                }
            }
        });
        map.put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (redoStack.size() > 0) {
                    UndoableRedoable redoable = redoStack.pop();
                    redoable.redo();
                    undoStack.push(redoable);
                    gui.repaint();
                }
            }
        });
    }
}
