package com.github.eyrekr.util;


import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public final class Str {

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

    }

    private static final Pattern NUMBERS = Pattern.compile("(\\d+)", Pattern.MULTILINE | Pattern.DOTALL);
    private static final String[] FORMAT_CONTROL = new String[]{
            "@@",
            "@k", "@r", "@g", "@y", "@b", "@m", "@c", "@w",
            "@K", "@R", "@G", "@Y", "@B", "@M", "@C", "@W",
    };
    private static final String[] FORMAT_ASCII = new String[]{
            Ascii.RESET,
            Ascii.BLACK, Ascii.RED, Ascii.GREEN, Ascii.YELLOW, Ascii.BLUE, Ascii.MAGENTA, Ascii.CYAN, Ascii.WHITE,
            Ascii.LIGHT_BLACK, Ascii.LIGHT_RED, Ascii.LIGHT_GREEN, Ascii.LIGHT_YELLOW, Ascii.LIGHT_BLUE, Ascii.LIGHT_MAGENTA, Ascii.LIGHT_CYAN, Ascii.LIGHT_WHITE,
    };


    public static Seq<Long> longs(final String input) {
        final var matcher = NUMBERS.matcher(input);
        Seq<Long> seq = Seq.empty();
        while (matcher.find()) {
            seq = seq.prepend(Long.parseLong(matcher.group()));
        }
        return seq.reverse();
    }

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
        final String fmt = StringUtils.replaceEachRepeatedly(format, FORMAT_CONTROL, FORMAT_ASCII);
        final StringBuilder builder = new StringBuilder();
        final int n = fmt.length();
        int i = 0;
        boolean bold = false, italic = false, underline = false;
        while (i < fmt.length()) {
            final char ch = fmt.charAt(i);
            final char la = (i + 1 < n) ? fmt.charAt(i + 1) : '\0';
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
        System.out.printf(builder.toString(), args);
    }

    public static void main(String[] args) {
        print("**bold**__underline__//italic//\n");
        print("@rRED@gGREEN@bBLUE@cCYAN@mMAGENTA@yYELLOW@kBLACK@wGRAY@@\n");
        print("@RRED@GGREEN@BBLUE@CCYAN@MMAGENTA@YYELLOW@KBLACK@WGRAY@@\n");
    }
}
