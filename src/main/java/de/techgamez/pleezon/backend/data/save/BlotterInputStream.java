package de.techgamez.pleezon.backend.data.save;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class BlotterInputStream extends BufferedInputStream {
    public BlotterInputStream(InputStream in) {
        super(in);
    }

    public String readString() throws IOException {
        int length = readInt();
        byte[] bytes = new byte[length];
        read(bytes);
        return new String(bytes);
    }

    public int readInt() throws IOException {
        int bytesAmount = Integer.SIZE / 8;
        byte[] bytes = new byte[bytesAmount];
        read(bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getInt();
    }

    public boolean readBoolean() throws IOException {
        byte[] bytes = new byte[1];
        read(bytes);
        return bytes[0] == 1;
    }

    public long readLong() throws IOException {
        int bytesAmount = Long.SIZE / 8;
        byte[] bytes = new byte[bytesAmount];
        read(bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getLong();

    }

    public double readDouble() throws IOException {
        int bytesAmount = Double.SIZE / 8;
        byte[] bytes = new byte[bytesAmount];
        read(bytes);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return buffer.getDouble();
    }

}

