package com.github.eyrekr;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

class Util {

    static long[] longs(final String input) {
        final String[] strings = StringUtils.split(input);
        final long[] longs = new long[strings.length];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = Long.parseLong(strings[i]);
        }
        return longs;
    }

    static <T> T fromNumbers(final String input, final Function<long[], T> constructor) {
        return constructor.apply(longs(input));
    }
}
