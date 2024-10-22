package ru.feryafox;

import com.beust.jcommander.Parameter;
import lombok.Getter;

public class FoxyBMPToolsArgs {
    @Parameter(names = {"-i", "--input"}, description = "Входной файл", required = true)
    @Getter
    private String inputFile;

    @Parameter(names = {"-p", "-ph", "--print-headers"}, description = "Напечатать заголовки файла")
    @Getter
    private boolean printHeaders = false;

    @Parameter(names = {"-c", "-scc", "--seperate-color-channel"}, description = "Выделить в отдельные каналы")
    @Getter
    private boolean seperateColorChannel = false;

    @Parameter(names = {"-s", "-sbp", "--slice-bit-clane"}, description = "Нарезать на битовые плоскости")
    @Getter
    private boolean sliceBitClane = false;


    @Parameter(names = {"-h", "--help"}, description = "Показывает помощь", help = true)
    @Getter
    private boolean help = false;

    @Parameter(names = {"-l", "--use-library"}, description = "Использовать библиотечные методы обработки")
    @Getter
    private boolean useLibrary = false;
}
