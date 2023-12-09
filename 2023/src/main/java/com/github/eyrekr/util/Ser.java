package com.github.eyrekr.util;

/**
 * Series of numbers.
 * Optimized {@link Seq} for working with numbers.
 * The optimization is about replacing the tail recursion with cycles => cleaner stack trace.
 */
public final class Ser {
    public static final Ser EMPTY = new Ser(0, null);

    public final long value;
    public final long lastValue;
    public final int length;
    public final Ser tail;
    public final boolean isEmpty;

    private Ser(final long value, final Ser tail) {
        this.value = value;
        this.lastValue = (tail == null || tail.length == 0) ? value : tail.lastValue;
        this.length = (tail == null) ? 0 : tail.length + 1;
        this.tail = tail;
        this.isEmpty = (tail == null);
    }

    public static Ser empty() {
        return EMPTY;
    }

    //    public static Sr of(final long first, final long... rest) {
//    }
//
    public static Ser fromArray(final long[] array) {
        Ser ser = EMPTY;
        if (array != null) {
            for (int i = array.length - 1; i >= 0; i--) {
                ser = new Ser(array[i], ser);
            }
        }
        return ser;
    }

    public static Ser range(final long startInclusive, final long endExclusive) {
        Ser ser = EMPTY;
        for (long i = endExclusive - 1; i >= startInclusive; i--) {
            ser = new Ser(i, ser);
        }
        return ser;
    }

    public Ser prepend(final long value) {
        return new Ser(value, this);
    }

}
