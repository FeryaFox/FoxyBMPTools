package ru.feryafox;

import com.beust.jcommander.JCommander;
import ru.feryafox.FoxyBMPTools.BMPReader.BMPReader;
import ru.feryafox.FoxyBMPTools.models.Image;
import ru.feryafox.FoxyBMPTools.utils.ColorChannelSeparator;
import ru.feryafox.FoxyBMPTools.utils.HeaderPrinter;
import ru.feryafox.FoxyBMPTools.utils.ImageBitPlaneSlicer;


public class Main {
    public static void main(String[] args) {
        FoxyBMPToolsArgs fargs = new FoxyBMPToolsArgs();

        JCommander jc = JCommander.newBuilder().addObject(fargs).build();

        try {
            jc.parse(args);

            if (fargs.isHelp()) {
                jc.usage();
                return;
            }

            String fileName = fargs.getInputFile();
            BMPReader reader = new BMPReader(fileName);
            Image image = reader.read();

            if (image == null) {
                System.out.println("Не удалось прочитать файл. Возможно его вообще нет...");
                return;
            }


            if (fargs.isPrintHeaders()) {
                HeaderPrinter.printHeader(image);
            }
            if (fargs.isSeperateColorChannel()) {
                ColorChannelSeparator.decomposeImage(image, fileName);
            }
            if (fargs.isSliceBitClane()) {
                Image gImage = ImageBitPlaneSlicer.convertToGrayscale(image, fileName);

                ImageBitPlaneSlicer.sliceImageToBitPlanes(gImage, fileName);
            }
        }
        catch (com.beust.jcommander.ParameterException e) {
            System.err.println("Error: " + e.getMessage());
            jc.usage();
        }
    }
}