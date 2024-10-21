package ru.feryafox.FoxyBMPTools.models;


import lombok.Getter;
import lombok.Setter;

public class Image {
    @Getter
    @Setter
    private BitmapFileHeader header = null;
    @Getter
    @Setter
    private BitmapInfoHeader info = null;
    @Getter
    @Setter
    private int[][] image = null;

    public Image(int width, int height) {
        image = new int[height][width];
        BitmapFileHeader.BitmapFileHeaderBuilder builder = new BitmapFileHeader.BitmapFileHeaderBuilder();

        builder.bfType((short) 19778);
        builder.bfSize(BitmapInfoHeader.HEADER_SIZE + BitmapFileHeader.HEADER_SIZE + width * height * 4);
        builder.bfOffBits(BitmapInfoHeader.HEADER_SIZE + BitmapFileHeader.HEADER_SIZE);
        header = builder.build();

        BitmapInfoHeader.BitmapInfoHeaderBuilder builder1 = new BitmapInfoHeader.BitmapInfoHeaderBuilder();

        builder1.biSize(BitmapInfoHeader.HEADER_SIZE);
        builder1.biWidth(width);
        builder1.biHeight(height);
        builder1.biPlanes((short) 1);
        builder1.biBitCount((short) 24);

        info = builder1.build();
    }

    public Image(BitmapFileHeader header, BitmapInfoHeader info) {
        this.header = header;
        this.info = info;
        image = new int[info.getBiWidth()][info.getBiHeight()];
    }

    public Image(BitmapFileHeader header, BitmapInfoHeader info, int[][] image) {
        this.header = header;
        this.info = info;
        this.image = image;
    }

    public void setPixel(int x, int y, int value) {
        image[y][x] = value;
    }

    public void setPixel(int x, int y, int red, int green, int blue) {
        image[y][x] = (red << 16) | (green << 8) | blue;
    }

    public void setPixel(int x, int y, Pixel p) {
        image[y][x] = p.RGB;
    }

    public Pixel getPixel(int x, int y) {
//        System.out.println();
        return new Pixel(image[y][x]);
    }

    public static class Pixel implements Cloneable{
        @Getter @Setter private int RGB;

        public Pixel(int red, int green, int blue) {
            RGB = 0xFF000000 | (red << 16) | (green << 8) | blue;
        }

        public Pixel(int RGB) {
            this.RGB = RGB;
        }

        public int getRed() {
            return (RGB >> 16) & 0xFF;
        }

        public int getGreen() {
            return (RGB >> 8) & 0xFF;
        }

        public int getBlue() {
            return (RGB) & 0xFF;
        }

        public int setRed(int red) {
            return (RGB & 0xFF00FFFF) | ((red & 0xFF) << 16);
        }

        public int setGreen(int green) {
            return (RGB & 0xFFFF00FF) | ((green & 0xFF) << 8);
        }

        public int setBlue(int blue) {
            return (RGB & 0xFFFFFF00) | (blue & 0xFF);
        }

        public static int convertRGB(int red, int green, int blue) {
            return 0xFF000000 & (red << 16) | (green << 8) | blue;
        }

        @Override
        public Pixel clone() {
            try {
                Pixel clone = (Pixel) super.clone();
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new AssertionError();
            }
        }
    }
}
