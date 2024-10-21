package ru.feryafox.FoxyBMPTools.utils;

import de.vandermeer.asciitable.AsciiTable;
import ru.feryafox.FoxyBMPTools.models.BitmapFileHeader;
import ru.feryafox.FoxyBMPTools.models.BitmapInfoHeader;
import ru.feryafox.FoxyBMPTools.models.Image;

public class HeaderPrinter {
    public static void printHeader(Image image) {
        BitmapInfoHeader infoHeader = image.getInfo();
        BitmapFileHeader fileHeader = image.getHeader();

        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRow("Поле", "Значение");
        at.addRule();
        at.addRow("Тип файла", fileHeader.getBfType());
        at.addRow("Размер файла в байтах", fileHeader.getBfSize());
        at.addRow("Резервное поле", fileHeader.getBfReserved1());
        at.addRow("Резервное поле", fileHeader.getBfReserved2());
        at.addRow("Смещение, с которого начинается само изображение", fileHeader.getBfOffBits());
        at.addRule();
        System.out.println("Заголовок файла: ");
        System.out.println(at.render());

        AsciiTable at2 = new AsciiTable();
        at2.addRule();
        at2.addRow("Поле", "Значение");
        at2.addRule();
        at2.addRow("Размер заголовка BITMAP", infoHeader.getBiSize());
        at2.addRow("Ширина изображения в пикселях", infoHeader.getBiWidth());
        at2.addRow("Высота изображения в пикселях", infoHeader.getBiHeight());
        at2.addRow("Число плоскостей", infoHeader.getBiPlanes());
        at2.addRow("Бит/пиксел", infoHeader.getBiBitCount());
        at2.addRow("Тип сжатия", infoHeader.getBiCompression());
        at2.addRow("размер изображения", infoHeader.getBiSizeImage());
        at2.addRow("Горизонтальное разрешение", infoHeader.getBiXPelsPerMeter());
        at2.addRow("Вертикальное разрешение", infoHeader.getBiYPelsPerMeter());
        at2.addRow("Количество используемых цветов", infoHeader.getBiClrUsed());
        at2.addRow("Количество \"важных\" цветов", infoHeader.getBiClrImportant());
        at2.addRule();

        System.out.println("Заголовок изображения");
        System.out.println(at2.render());
    }

    public static void printHeader(Image image, String fileName) {
        System.out.printf("Заголовки %s\n", fileName );
        printHeader(image);
    }
}
