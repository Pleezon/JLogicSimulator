package de.techgamez.pleezon.backend.data.save;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class BlotterOutputStream extends BufferedOutputStream {
    public BlotterOutputStream(OutputStream out) {
        super(out);
    }

    public void writeInt(int num) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE / 8);
        buffer.putInt(num);
        write(buffer.array());
    }

    public void writeBoolean(boolean b) throws IOException {
        write(b ? 1 : 0);
    }

    public void writeString(String str) throws IOException {
        writeInt(str.length());
        write(str.getBytes());
    }

    public void writeLong(long l) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE / 8);
        buffer.putLong(l);
        write(buffer.array());
    }

    public void writeDouble(double d) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(Double.SIZE / 8);
        buffer.putDouble(d);
        write(buffer.array());
    }


}
