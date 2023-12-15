package com.github.eyrekr;

import com.github.eyrekr.util.Arr;
import com.github.eyrekr.util.Seq;
import com.google.common.collect.LinkedListMultimap;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * https://adventofcode.com/2023/day/15
 * 1) 515495
 * 2)
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
        return words.map(D15::hash).reduce(Integer::sum);
    }

    long star2() {
        final List<Lens>[] map = new List[256];
        for (int i = 0; i < 256; i++) map[i] = new LinkedList<>();
        for (final var lens : lenses) {
            switch (lens.operation) {
                case '=' -> {
                    final int i = map[lens.hash].indexOf(lens);
                    if (i < 0) {
                        map[lens.hash].add(lens);
                    } else {
                        map[lens.hash].get(i).focalLength = lens.focalLength;
                    }
                }
                case '-' -> map[lens.hash].remove(lens);
            }
        }

        long sum = 0L;
        for (int i = 0; i < 256; i++) {
            for (int l = 0; l < map[i].size(); l++) {
                sum += (i+1) * (l+1) * map[i].get(l).focalLength;
            }
        }
        return sum;
    }

    static int hash(final String word) {
        return Seq.ofCharactersFromString(word).reduce(0, (hash, ch) -> (hash + ch) * 17 % 256);
    }

    static class Lens {
        final String name;
        final int hash;
        final char operation;
        long focalLength;

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

        @Override
        public int hashCode() {
            return hash;
        }
    }
}