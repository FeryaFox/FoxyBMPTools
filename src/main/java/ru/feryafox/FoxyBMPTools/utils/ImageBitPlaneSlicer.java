package ru.feryafox.FoxyBMPTools.utils;

import ru.feryafox.FoxyBMPTools.BMPReader.BMPReader;
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

    public static Image convertToGrayscale(Image image, String baseFilename) {
        int height = image.getInfo().getBiHeight();
        int width = image.getInfo().getBiWidth();
        Image gImage = new Image(width, height);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Image.Pixel pixel = image.getPixel(x, y);

                // Сначала получаем серое значение используя стандартные веса
                int gray = (int)Math.round(0.299 * pixel.getRed() + 0.587 * pixel.getGreen() + 0.114 * pixel.getBlue());


//                // Убеждаемся, что значение не выходит за пределы 0-255
//                gray = Math.min(255, Math.max(0, gray));

                Image.Pixel newPixel = new Image.Pixel(gray, gray, gray);
                gImage.setPixel(x, y, newPixel);
            }
        }

        BMPWriter writer = new BMPWriter(baseFilename.replace(".bmp", "_gray.bmp"));
        writer.write(gImage);
        return gImage;
    }

    public static void main(String[] args) {

        BMPReader reader1 = new BMPReader("example_gray.bmp");

        Image image1 = reader1.read();

        sliceImageToBitPlanes(image1, "example_gray.bmp");

    }
}