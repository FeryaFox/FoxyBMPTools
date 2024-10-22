package ru.feryafox.FoxyBMPTools.BMPReader;

import ru.feryafox.FoxyBMPTools.DataStreamWithLittleEndian.DataInputStreamWithLittleEndian;
import ru.feryafox.FoxyBMPTools.models.BitmapFileHeader;
import ru.feryafox.FoxyBMPTools.models.BitmapInfoHeader;
import ru.feryafox.FoxyBMPTools.models.Image;

import java.io.*;

public class BMPReader {
    private final String path;;

    public BMPReader(String path) {
        this.path = path;
    }

    private void readHeaders(DataInputStreamWithLittleEndian dis) throws IOException {
        readBitMapFileHeader(dis);
        readBitmapInfoHeader(dis);
    }

    public Image read() {
        try (DataInputStreamWithLittleEndian dis = new DataInputStreamWithLittleEndian(new BufferedInputStream(new FileInputStream(path)))) {
            dis.mark(Integer.MAX_VALUE);
            BitmapFileHeader fileHeader = readBitMapFileHeader(dis);
            BitmapInfoHeader infoHeader = readBitmapInfoHeader(dis);
            return new Image(fileHeader, infoHeader, readImage(dis, fileHeader, infoHeader));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private int[][] readImage(DataInputStreamWithLittleEndian dis, BitmapFileHeader fileHeader, BitmapInfoHeader infoHeader) throws IOException {
        dis.reset();
        int width = infoHeader.getBiWidth();
        int height = infoHeader.getBiHeight();
        int bytesPerPixel = infoHeader.getBiBitCount() / 8;
        int padding = (4 - (width * bytesPerPixel) % 4) % 4;
        int[][] imageData = new int[height][width];

        dis.skipBytes(fileHeader.getBfOffBits());

        for (int y = height - 1; y >= 0; y--) {
           for (int x = 0; x < width; x++) {
               int blue = dis.readUnsignedByte();
               int green = dis.readUnsignedByte();
               int red = dis.readUnsignedByte();

               imageData[y][x] = 0xFF000000 | (red << 16) | (green << 8) | blue;
           }
            dis.skipBytes(padding);
        }
        return imageData;
    }


    private BitmapFileHeader readBitMapFileHeader(DataInputStreamWithLittleEndian dis) throws IOException {
        BitmapFileHeader.BitmapFileHeaderBuilder builder = BitmapFileHeader.builder();

        while (true) {
            if (dis.available() == 0) {
                throw new EOFException("Достигнут конец файла. Файл поврежден или имеет неверный формат.");
            }
            int b1 = dis.readUnsignedByte();
            if (b1 == 66) {
                if (dis.available() == 0) {
                    throw new EOFException("Достигнут конец файла. Файл поврежден или имеет неверный формат.");
                }
                int b2 = dis.readUnsignedByte();
                if (b2 == 77) {
                    break; // Сигнатура найдена
                } else {
                    // Продолжаем поиск со следующего байта
                    dis.mark(1); // Помечаем текущую позицию
                    dis.reset();  // Возвращаемся на помеченную позицию
                }
            }
        }


        builder.bfType((short) 19778);

        builder.bfSize(dis.readIntLE());

        builder.bfReserved1(dis.readShortLE());

        builder.bfReserved2(dis.readShortLE());

        builder.bfOffBits(dis.readIntLE());

        return builder.build();
    }

    private BitmapInfoHeader readBitmapInfoHeader(DataInputStreamWithLittleEndian dis) throws IOException {
        BitmapInfoHeader.BitmapInfoHeaderBuilder builder = BitmapInfoHeader.builder();

        builder.biSize(dis.readIntLE());

        builder.biWidth(dis.readIntLE());

        builder.biHeight(dis.readIntLE());

        builder.biPlanes(dis.readShortLE());

        builder.biBitCount(dis.readShortLE());

        builder.biCompression(dis.readIntLE());

        builder.biSizeImage(dis.readIntLE());

        builder.biXPelsPerMeter(dis.readIntLE());

        builder.biYPelsPerMeter(dis.readIntLE());

        builder.biClrUsed(dis.readIntLE());

        builder.biClrImportant(dis.readIntLE());

        return builder.build();
    }
}
