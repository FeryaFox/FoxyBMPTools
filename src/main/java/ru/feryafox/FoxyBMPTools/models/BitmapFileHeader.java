package ru.feryafox.FoxyBMPTools.models;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BitmapFileHeader implements Cloneable {
    public static final int HEADER_SIZE = 14;

    private short bfType; // Код 4D42h - Буквы 'BM'
    private int bfSize; // Размер файла в байтах
    private short bfReserved1; // 0 (Резервное поле)
    private short bfReserved2; // 0 (Резервное поле)
    private int bfOffBits; // Смещение, с которого начинается само изображение

    @Override
    public BitmapFileHeader clone() {
        try {
            BitmapFileHeader clone = (BitmapFileHeader) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
