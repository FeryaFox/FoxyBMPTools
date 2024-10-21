package ru.feryafox.FoxyBMPTools.DataStreamWithLittleEndian;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataInputStreamWithLittleEndian extends DataInputStream {
    private String path;
    private DataInputStream dis;

    /**
     * Creates a DataInputStream that uses the specified
     * underlying InputStream.
     *
     * @param in the specified input stream
     */
    public DataInputStreamWithLittleEndian(InputStream in) {
        super(in);
    }

    public int readIntLE() throws IOException {
        int b1 = readUnsignedByte();
        int b2 = readUnsignedByte();
        int b3 = readUnsignedByte();
        int b4 = readUnsignedByte();
        return (b1) | (b2 << 8) | (b3 << 16) | (b4 << 24);
    }

    public short readShortLE() throws IOException {
        int b1 = readUnsignedByte();
        int b2 = readUnsignedByte();
        return (short) ((b1) | (b2 << 8));
    }

    public long readLongLE() throws IOException {
        long b1 = readUnsignedByte();
        long b2 = readUnsignedByte();
        long b3 = readUnsignedByte();
        long b4 = readUnsignedByte();
        long b5 = readUnsignedByte();
        long b6 = readUnsignedByte();
        long b7 = readUnsignedByte();
        long b8 = readUnsignedByte();
        return (b1) | (b2 << 8) | (b3 << 16) | (b4 << 24) |
                (b5 << 32) | (b6 << 40) | (b7 << 48) | (b8 << 56);
    }
}
