package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;
import org.apache.commons.lang3.StringUtils;

/**
 * https://adventofcode.com/2023/day/15
 * 1) 515495
 * 2) 229349
 */
class D15 extends AoC {

    final Seq<String> words;
    final Seq<Lens> lenses;

    D15(final String input) {
        super(input);
        this.words = Seq.fromArray(StringUtils.split(input, ','));
        this.lenses = this.words.map(Lens::from);
    }

    long star1() {
        return words.toArr(D15::hash).sum();
    }

    long star2() {
        final Seq<Lens>[] map = new Seq[256];
        for (int i = 0; i < 256; i++) map[i] = Seq.empty();
        lenses.each(lens -> {
            final int i = lens.hash;
            switch (lens.operation) {
                case '=' -> {
                    if (map[i].has(lens)) map[i] = map[i].replaceFirst(lens, lens);
                    else map[i] = map[i].addLast(lens);
                }
                case '-' -> map[i] = map[i].removeFirst(lens);
            }
        });

        return Seq.range(0, 256)
                .flatMap(box -> map[box].mapWith(Seq.range(0, map[box].length), (lens, position) ->  (box + 1) * (position + 1) * lens.focalLength))
                .reduce(Long::sum);
    }

    static int hash(final String word) {
        return Seq.ofCharactersFromString(word).reduce(0, (hash, ch) -> (hash + ch) * 17 % 256);
    }

    static class Lens {
        final String name;
        final int hash;
        final char operation;
        final long focalLength;

        private Lens(String name, char operation, long focalLength) {
            this.name = name;
            this.hash = D15.hash(name);
            this.operation = operation;
            this.focalLength = focalLength;
        }

        static Lens from(final String input) {
            if (input.endsWith("-")) {
                final String name = StringUtils.substringBefore(input, "-");
                return new Lens(name, '-', -1L);
            }
            final String name = StringUtils.substringBefore(input, "=");
            final long focalLength = Long.parseLong(StringUtils.substringAfter(input, "="));
            return new Lens(name, '=', focalLength);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof final Lens that) {
                return this.name.equalsIgnoreCase(that.name);
            }
            return false;
        }
    }
}