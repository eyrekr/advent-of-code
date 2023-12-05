package com.github.eyrekr;

import java.util.regex.Pattern;

final class Str {

    private static final Pattern NUMBERS = Pattern.compile("(\\d+)", Pattern.MULTILINE | Pattern.DOTALL);

    static Seq<Long> longs(final String input) {
        final var matcher = NUMBERS.matcher(input);
        Seq<Long> seq = Seq.empty();
        while (matcher.find()) {
            seq = seq.add(Long.parseLong(matcher.group()));
        }
        return seq.reverse();
    }

}
