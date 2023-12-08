package com.github.eyrekr.util;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public final class Str {

    private static final Pattern NUMBERS = Pattern.compile("(\\d+)", Pattern.MULTILINE | Pattern.DOTALL);
    private static final String[][] FORMATTING = new String[][] {
      new String[]{"@@", "\033[0m"},
      new String[]{"@k", "\033[30m"},
      new String[]{"@r", "\033[31m"},
      new String[]{"@g", "\033[32m"},
      new String[]{"@y", "\033[33m"},
      new String[]{"@b", "\033[34m"},
      new String[]{"@m", "\033[35m"},
      new String[]{"@c", "\033[36m"},
      new String[]{"@w", "\033[37m"},
      new String[]{"@K", "\033[90m"},
      new String[]{"@R", "\033[91m"},
      new String[]{"@G", "\033[92m"},
      new String[]{"@Y", "\033[93m"},
      new String[]{"@B", "\033[94m"},
      new String[]{"@M", "\033[95m"},
      new String[]{"@C", "\033[96m"},
      new String[]{"@W", "\033[97m"},
      new String[]{"**", "\033[1m", "\033[0m"},
      new String[]{"__", "\033[4m", "\033[0m"},
      new String[]{"//", "\033[3m", "\033[0m"},
    };

    public static Seq<Long> longs(final String input) {
        final var matcher = NUMBERS.matcher(input);
        Seq<Long> seq = Seq.empty();
        while (matcher.find()) {
            seq = seq.prepend(Long.parseLong(matcher.group()));
        }
        return seq.reverse();
    }

    static void print(final String format, final Object... args) {
        final StringBuilder b = new StringBuilder();
        int i = 0;
        while(i < format.length()) {
            final char ch = format.charAt(i);
            switch()
        }
        System.out.printf(tmp, args);
    }

    public static void main(String[] args) {
        print("**bold**__underline__//italic//##frame##\n");
        print("@rRED@gGREEN@bBLUE@cCYAN@mMAGENTA@yYELLOW@kBLACK@wGRAY@@\n");
        print("@RRED@GGREEN@BBLUE@CCYAN@MMAGENTA@YYELLOW@KBLACK@WGRAY@@\n");
    }
}
