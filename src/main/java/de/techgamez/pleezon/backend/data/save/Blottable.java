package de.techgamez.pleezon.backend.data.save;

import java.io.IOException;

public interface Blottable {
    void blot(BlotterOutputStream out) throws IOException;

    void unblot(BlotterInputStream in) throws IOException;
}
