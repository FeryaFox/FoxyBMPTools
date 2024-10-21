package ru.feryafox.FoxyBMPTools.DataStreamWithLittleEndian;

import java.io.DataOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DataOutputStreamWithLittleEndian extends DataOutputStream {

    /**
     * Creates a new data output stream to write data to the specified
     * underlying output stream. The counter {@code written} is
     * set to zero.
     *
     * @param out the underlying output stream, to be saved for later
     *            use.
     * @see FilterOutputStream#out
     */
    public DataOutputStreamWithLittleEndian(OutputStream out) {
        super(out);
    }

    public void writeShortLE(short value) throws IOException {
        writeShort((((value & 0xFF) << 8) | ((value >> 8) & 0xFF)));
    }

    public void writeIntLE(int value) throws IOException {
        writeInt(((value & 0xFF) << 24) |
                ((value & 0xFF00) << 8) |
                ((value >> 8) & 0xFF00) |
                ((value >> 24) & 0xFF));
    }

    public void writeLongLE(long value) throws IOException {
        writeLong((((value & 0xFFL) << 56) |
                        ((value & 0xFF00L) << 40) |
                        ((value & 0xFF0000L) << 24) |
                        ((value & 0xFF000000L) << 8) |
                        ((value >> 8) & 0xFF000000L) |
                        ((value >> 24) & 0xFF0000L) |
                        ((value >> 40) & 0xFF00L) |
                        ((value >> 56) & 0xFFL)));
    }
}
