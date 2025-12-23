package com.github.eyrekr.immutable;

import java.util.Arrays;

/**
 * Interval.
 */
public final class Int {

    public final long a, b;

    private Int(long a, long b) {
        if (a <= b) {
            this.a = a;
            this.b = b;
        } else {
            this.a = b;
            this.b = a;
        }
    }

    public static Int closed(final long a, final long b) {
        return new Int(a, b);
    }

    public static Int fromString(final String string) {
        final Parser parser = Parser.of(string);
        final boolean leftOpen = parser.eatOneOf('(', '[') == '(';
        final long left = parser.eatNumber();
        final String delimiter = parser.eatOneOf("...", "..", ";", ",", "-");
        if (delimiter == null) throw new IllegalStateException("Invalid interval: " + string);
        final long right = parser.eatNumber();
        final boolean rightOpen = parser.eatOneOf(')', ']') == ')';
        return new Int(leftOpen ? left + 1 : left, rightOpen ? right - 1 : right);
    }

    public boolean overlaps(final Int that) {
        return that.a <= this.b && this.a <= that.b;
    }

    public boolean notOverlaps(final Int that) {
        return this.b < that.a || that.b < this.a;
    }

    public boolean contains(final Int that) {
        return this.a <= that.a && that.b <= this.b;
    }

    public boolean notContains(final Int that) {
        return that.a < this.a || this.b < that.b;
    }

    public boolean contains(final long x) {
        return a <= x && x <= b;
    }

    public boolean notContains(final long x) {
        return x < a || b < x;
    }

    @Override
    public String toString() {
        return String.format("[%d,%d]", a, b);
    }

    private static class Parser {
        private final char[] a;
        private int i;

        private Parser(final char[] a, final int i) {
            this.a = a;
            this.i = i;
        }

        static Parser of(final String string) {
            return new Parser(string.toCharArray(), 0);
        }

        boolean done() {
            return i >= a.length;
        }

        char la() {
            return i < a.length ? a[i] : 0;
        }

        boolean la(final char ch) {
            return i < a.length && a[i] == ch;
        }

        boolean la(final int k, final char ch) {
            return 0 <= i + k && i + k < a.length && a[i + k] == ch;
        }

        boolean la(final String string) {
            for (int k = 0; k < string.length(); k++)
                if (!la(k, string.charAt(k))) return false;
            return true;
        }

        char eat() {
            final char ch = i < a.length ? a[i] : 0;
            i++;
            return ch;
        }

        void jmp(final int k) {
            i += k;
        }

        char eatOneOf(final char... symbols) {
            for (final char symbol : symbols)
                if (la(symbol)) return eat();
            return 0;
        }

        String eatOneOf(final String... sequences) {
            for (final String sequence : sequences)
                if (la(sequence)) {
                    jmp(sequence.length());
                    return sequence;
                }
            return null;
        }

        long eatNumber() {
            final long signum = switch (eatOneOf('+', '-')) {
                case '-' -> -1L;
                default -> +1L;
            };
            long number = 0;
            while (Character.isDigit(la())) number = 10 * number + Character.digit(eat(), 10);
            return signum * number;
        }
    }
}
