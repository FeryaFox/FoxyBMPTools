package ru.feryafox.FoxyBMPTools.BMPWriter;

import ru.feryafox.FoxyBMPTools.BMPReader.BMPReader;
import ru.feryafox.FoxyBMPTools.DataStreamWithLittleEndian.DataOutputStreamWithLittleEndian;
import ru.feryafox.FoxyBMPTools.models.BitmapFileHeader;
import ru.feryafox.FoxyBMPTools.models.BitmapInfoHeader;
import ru.feryafox.FoxyBMPTools.models.Image;
import ru.feryafox.FoxyBMPTools.utils.HeaderPrinter;
import ru.feryafox.FoxyBMPTools.utils.ImageBitPlaneSlicer;
import ru.feryafox.FoxyBMPTools.utils.ColorChannelSeparator;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BMPWriter {
    private String path;
    public BMPWriter(String path) {
        this.path = path;
    }

    public void write(Image image) {
        try (DataOutputStreamWithLittleEndian dos = new DataOutputStreamWithLittleEndian(new BufferedOutputStream(new FileOutputStream(path)))) {
            writeBitmapFileHeader(dos, image.getHeader());
            writeBitmapInfoHeader(dos, image.getInfo());
            writeImageData(dos, image);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeBitmapFileHeader(DataOutputStreamWithLittleEndian dos, BitmapFileHeader fileHeader) throws IOException {
        dos.writeShortLE(fileHeader.getBfType());
        dos.writeIntLE(fileHeader.getBfSize());
        dos.writeShortLE(fileHeader.getBfReserved1());
        dos.writeShortLE(fileHeader.getBfReserved2());
        dos.writeIntLE(fileHeader.getBfOffBits());
    }

    private void writeBitmapInfoHeader(DataOutputStreamWithLittleEndian dos, BitmapInfoHeader infoHeader) throws IOException {
        dos.writeIntLE(infoHeader.getBiSize());
        dos.writeIntLE(infoHeader.getBiWidth());
        dos.writeIntLE(infoHeader.getBiHeight());
        dos.writeShortLE(infoHeader.getBiPlanes());
        dos.writeShortLE(infoHeader.getBiBitCount());
        dos.writeIntLE(infoHeader.getBiCompression());
        dos.writeIntLE(infoHeader.getBiSizeImage());
        dos.writeIntLE(infoHeader.getBiXPelsPerMeter());
        dos.writeIntLE(infoHeader.getBiYPelsPerMeter());
        dos.writeIntLE(infoHeader.getBiClrUsed());
        dos.writeIntLE(infoHeader.getBiClrImportant());
    }

    private void writeImageData(DataOutputStreamWithLittleEndian dos, Image image) throws IOException {
        int width = image.getInfo().getBiWidth();
        int height = image.getInfo().getBiHeight();
        int bytesPerPixel = image.getInfo().getBiBitCount() / 8;
        int padding = (4 - (width * bytesPerPixel) % 4) % 4;

        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getImage()[y][x];
                dos.writeByte((pixel >> 0) & 0xFF);
                dos.writeByte((pixel >> 8) & 0xFF);
                dos.writeByte((pixel >> 16) & 0xFF);
            }

            // Запись padding байтов
            for (int p = 0; p < padding; p++) {
                dos.writeByte(0);
            }
        }
    }

    public static void fixHeaders(Image image) {
        image.getHeader().setBfOffBits(BitmapInfoHeader.HEADER_SIZE + BitmapFileHeader.HEADER_SIZE);
        image.getHeader().setBfSize(BitmapInfoHeader.HEADER_SIZE + BitmapFileHeader.HEADER_SIZE + image.getInfo().getBiWidth() * image.getInfo().getBiHeight() * 4);
        image.getInfo().setBiSize(BitmapInfoHeader.HEADER_SIZE);
    }

    public static void main(String[] args) {
        BMPReader bmpReader = new BMPReader("holo.bmp");
        Image image = bmpReader.read();

        ImageBitPlaneSlicer.sliceImageToBitPlanes(image, "holo.bmp");

        HeaderPrinter.printHeader(image);

        BMPWriter.fixHeaders(image);

        ColorChannelSeparator.decomposeImage(image, "holo.bmp");


        BMPWriter bmpWriter = new BMPWriter("holo_re.bmp");
        bmpWriter.write(image);
    }
}
