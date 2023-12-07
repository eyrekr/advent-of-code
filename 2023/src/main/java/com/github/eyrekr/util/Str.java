package com.github.eyrekr.util;


import java.util.regex.Pattern;

public final class Str {

    private static final Pattern NUMBERS = Pattern.compile("(\\d+)", Pattern.MULTILINE | Pattern.DOTALL);

    public static Seq<Long> longs(final String input) {
        final var matcher = NUMBERS.matcher(input);
        Seq<Long> seq = Seq.empty();
        while (matcher.find()) {
            seq = seq.prepend(Long.parseLong(matcher.group()));
        }
        return seq.reverse();
    }

}
