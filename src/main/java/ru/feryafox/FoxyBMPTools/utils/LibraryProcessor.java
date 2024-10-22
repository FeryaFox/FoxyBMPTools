package ru.feryafox.FoxyBMPTools.utils;

import de.vandermeer.asciitable.AsciiTable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LibraryProcessor {
    public static void processWithLibrary(String fileName, boolean printHeaders,
                                          boolean separateColors, boolean sliceBitPlanes) {
        try {
            File file = new File(fileName);
            BufferedImage original = ImageIO.read(file);

            if (printHeaders) {
                printLibraryHeaders(original);
            }

            if (separateColors) {
                separateColorsWithLibrary(original, fileName);
            }

            if (sliceBitPlanes) {
                sliceBitPlanesWithLibrary(original, fileName);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при обработке файла: " + e.getMessage());
        }
    }

    private static void printLibraryHeaders(BufferedImage image) {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Поле", "Значение");
        at.addRule();

        // Информация об изображении
        at.addRow("Ширина изображения", image.getWidth() + " пикселей");
        at.addRow("Высота изображения", image.getHeight() + " пикселей");
        at.addRow("Тип изображения", image.getType());
        at.addRow("Количество цветовых компонент", image.getColorModel().getNumComponents());
        at.addRow("Бит на пиксель", image.getColorModel().getPixelSize());
        at.addRow("Цветовое пространство", image.getColorModel().getColorSpace().getType() == 5 ? "RGB" : "Другое");
        at.addRow("Имеет альфа-канал", image.getColorModel().hasAlpha() ? "Да" : "Нет");
        at.addRow("Размер изображения", (image.getWidth() * image.getHeight() * image.getColorModel().getPixelSize() / 8) + " байт");

        // Информация о цветовой модели
        at.addRow("Прозрачность", image.getTransparency() == Transparency.OPAQUE ? "Нет" :
                image.getTransparency() == Transparency.BITMASK ? "Маска" : "Альфа-канал");
        at.addRow("Индексированные цвета", image.getColorModel().getNumColorComponents() < image.getColorModel().getNumComponents() ? "Да" : "Нет");

        at.addRule();

        System.out.println("\nИнформация об изображении (через библиотеку):");
        System.out.println(at.render());
    }

    private static void separateColorsWithLibrary(BufferedImage original, String fileName)
            throws IOException {
        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage redChannel = new BufferedImage(width, height,
                BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage greenChannel = new BufferedImage(width, height,
                BufferedImage.TYPE_BYTE_GRAY);
        BufferedImage blueChannel = new BufferedImage(width, height,
                BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(original.getRGB(x, y));

                redChannel.getRaster().setSample(x, y, 0, color.getRed());
                greenChannel.getRaster().setSample(x, y, 0, color.getGreen());
                blueChannel.getRaster().setSample(x, y, 0, color.getBlue());
            }
        }

        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        ImageIO.write(redChannel, "BMP", new File(baseName + "_lib_R.bmp"));
        ImageIO.write(greenChannel, "BMP", new File(baseName + "_lib_G.bmp"));
        ImageIO.write(blueChannel, "BMP", new File(baseName + "_lib_B.bmp"));
    }

    private static void sliceBitPlanesWithLibrary(BufferedImage original, String fileName)
            throws IOException {
        int width = original.getWidth();
        int height = original.getHeight();

        for (int bit = 0; bit < 8; bit++) {
            BufferedImage bitPlane = new BufferedImage(width, height,
                    BufferedImage.TYPE_BYTE_BINARY);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color color = new Color(original.getRGB(x, y));
                    int brightness = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                    int bitValue = (brightness >> bit) & 1;

                    bitPlane.getRaster().setSample(x, y, 0, bitValue);
                }
            }

            String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
            ImageIO.write(bitPlane, "BMP",
                    new File(baseName + "_lib_plane_" + bit + ".bmp"));
        }
    }
}