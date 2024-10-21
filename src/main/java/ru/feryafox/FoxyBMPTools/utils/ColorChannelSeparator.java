package ru.feryafox.FoxyBMPTools.utils;

import ru.feryafox.FoxyBMPTools.BMPWriter.BMPWriter;
import ru.feryafox.FoxyBMPTools.models.Image;

public class ColorChannelSeparator {
    public static void decomposeImage(Image image, String baseFilename) {
        int width = image.getInfo().getBiWidth();
        int height = image.getInfo().getBiHeight();

        Image redImage = new Image(width, height);
        Image greenImage = new Image(width, height);
        Image blueImage = new Image(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Image.Pixel pixel = image.getPixel(x, y);

                redImage.setPixel(x, y, pixel.getRed(), 0, 0);
                greenImage.setPixel(x, y, 0, pixel.getGreen(), 0);
                blueImage.setPixel(x, y, 0, 0, pixel.getBlue());
            }
        }

        // Сохранение изображений цветовых каналов
        BMPWriter writerR = new BMPWriter(baseFilename.replace(".bmp", "_R.bmp"));
        writerR.write(redImage);

        BMPWriter writerG = new BMPWriter(baseFilename.replace(".bmp", "_G.bmp"));
        writerG.write(greenImage);

        BMPWriter writerB = new BMPWriter(baseFilename.replace(".bmp", "_B.bmp"));
        writerB.write(blueImage);
    }
}