package ru.feryafox.FoxyBMPTools.utils;

import ru.feryafox.FoxyBMPTools.BMPWriter.BMPWriter;
import ru.feryafox.FoxyBMPTools.models.Image;

public class ImageBitPlaneSlicer {

    public static void sliceImageToBitPlanes(Image image, String baseFilename) {
        int width = image.getInfo().getBiWidth();
        int height = image.getInfo().getBiHeight();

        for (int plane = 0; plane < 8; plane++) {
            Image bitPlaneImage = new Image(width, height);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = image.getImage()[y][x];
                    // Извлекаем бит для текущей плоскости
                    int bit = (pixel >> plane) & 1;
                    // Устанавливаем пиксель белым или черным в зависимости от значения бита
                    bitPlaneImage.setPixel(x, y, bit * 255, bit * 255, bit * 255);
                }
            }

            // Сохраняем изображение битовой плоскости в отдельный файл

            String filename =baseFilename.replace(".bmp", "_plane_" + plane + ".bmp");
            BMPWriter writer = new BMPWriter(filename);
            writer.write(bitPlaneImage);
        }
    }
}