package ru.feryafox.FoxyBMPTools.models;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BitmapInfoHeader implements Cloneable {
    public final static int HEADER_SIZE = 40;

    private int biSize; // Размер заголовка BITMAP (в байтах) равно 40
    private int biWidth; // Ширина изображения в пикселях
    private int biHeight; // Высота изображения в пикселях
    private short biPlanes; // Число плоскостей, должно быть 1
    private short biBitCount; // Бит/пиксел: 1, 4, 8 или 24
    private int biCompression; // Тип сжатия
    private int biSizeImage; // 0 или размер сжатого изображения в байтах
    private int biXPelsPerMeter; // Горизонтальное разрешение, пиксел/м
    private int biYPelsPerMeter; // Вертикальное разрешение, пиксел/м
    private int biClrUsed; // Количество используемых цветов
    private int biClrImportant; // Количество "важных" цветов.

    @Override
    public BitmapInfoHeader clone() {
        try {
            BitmapInfoHeader clone = (BitmapInfoHeader) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}