package com.github.eyrekr.output;


import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;

public final class Out {

    private interface Ascii {
        String RESET = "\033[0m";
        String BOLD = "\033[1m";
        String UNDERLINE = "\033[4m";
        String ITALIC = "\033[3m";
        String BLACK = "\033[30m";
        String RED = "\033[31m";
        String GREEN = "\033[32m";
        String YELLOW = "\033[33m";
        String BLUE = "\033[34m";
        String MAGENTA = "\033[35m";
        String CYAN = "\033[36m";
        String WHITE = "\033[37m";
        String LIGHT_BLACK = "\033[90m";
        String LIGHT_RED = "\033[91m";
        String LIGHT_GREEN = "\033[92m";
        String LIGHT_YELLOW = "\033[93m";
        String LIGHT_BLUE = "\033[94m";
        String LIGHT_MAGENTA = "\033[95m";
        String LIGHT_CYAN = "\033[96m";
        String LIGHT_WHITE = "\033[97m";
        String TOP_LEFT = "\033[H";
        String ERASE_SCREEN = "\033[2J";
    }

    private static final String[] FORMAT_CONTROL = new String[]{
            "@@",
            "@k", "@r", "@g", "@y", "@b", "@m", "@c", "@w",
            "@K", "@R", "@G", "@Y", "@B", "@M", "@C", "@W",
            "@~", "@/"
    };
    private static final String[] FORMAT_ASCII = new String[]{
            Ascii.RESET,
            Ascii.BLACK, Ascii.RED, Ascii.GREEN, Ascii.YELLOW, Ascii.BLUE, Ascii.MAGENTA, Ascii.CYAN, Ascii.WHITE,
            Ascii.LIGHT_BLACK, Ascii.LIGHT_RED, Ascii.LIGHT_GREEN, Ascii.LIGHT_YELLOW, Ascii.LIGHT_BLUE, Ascii.LIGHT_MAGENTA, Ascii.LIGHT_CYAN, Ascii.LIGHT_WHITE,
            Ascii.TOP_LEFT, Ascii.ERASE_SCREEN
    };

    public static String mainResource(final String name) {
        try {
            return Files.readString(Path.of(String.format("src/main/resources/" + name)));
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String testResource(final String name) {
        try {
            return Files.readString(Path.of(String.format("src/test/resources/" + name)));
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void print(final String format, final Object... args) {
        final String str = StringUtils.replaceEachRepeatedly(String.format(format, args), FORMAT_CONTROL, FORMAT_ASCII);
        final StringBuilder builder = new StringBuilder();
        final int n = str.length();
        int i = 0;
        boolean bold = false, italic = false, underline = false;
        while (i < str.length()) {
            final char ch = str.charAt(i);
            final char la = (i + 1 < n) ? str.charAt(i + 1) : '\0';
            switch ("" + ch + la) {
                case "**" -> { // **bold**
                    builder.append(bold ? Ascii.RESET : Ascii.BOLD);
                    bold = !bold;
                    i++;
                }
                case "__" -> { // __underline__
                    builder.append(underline ? Ascii.RESET : Ascii.UNDERLINE);
                    underline = !underline;
                    i++;
                }
                case "//" -> { // //italic//
                    builder.append(italic ? Ascii.RESET : Ascii.ITALIC);
                    italic = !italic;
                    i++;
                }
                default -> builder.append(ch);
            }
            i++;
        }
        builder.append(Ascii.RESET);
        System.out.print(builder);
    }
}
