package de.techgamez.pleezon.backend.data;

public class IllegalStateModificationException extends Exception {
    public IllegalStateModificationException(String errorMessage) {
        super(errorMessage);
    }
}
