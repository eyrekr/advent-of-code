package com.github.eyrekr.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Array of numbers.
 * Optimized for better performance than @{@link Seq}.
 */
public final class Arr {

    private static final int MIN_CAPACITY = 16;
    private static final int GROW_FACTOR = 2;

    public final int length;
    public final boolean isEmpty;
    private final long[] a;
    private final int start;
    private final boolean isFull;
    private final boolean safeToGrow; // copy-on-write implementation

    private Arr() {
        this.a = new long[MIN_CAPACITY];
        this.length = 0;
        this.isEmpty = true;
        this.start = 0;
        this.isFull = false;
        this.safeToGrow = true;
    }

    private Arr(final long[] a, final int start, final int length, final boolean safeToGrow) {
        this.a = a;
        this.start = start % a.length;
        this.length = length;
        this.isEmpty = this.length == 0;
        this.isFull = this.length == a.length;
        this.safeToGrow = safeToGrow;
    }

    private Arr grow() {
        final long[] b = new long[a.length * GROW_FACTOR];
        for (int i = 0; i < length; i++) {
            b[i] = at(i);
        }
        return new Arr(b, 0, length, true);
    }

    @Override
    public Arr clone() {
        final long[] b = new long[a.length];
        for (int i = 0; i < length; i++) {
            b[i] = at(i);
        }
        return new Arr(b, 0, length, true);
    }

    public static Arr empty() {
        return new Arr();
    }

    public static Arr fromArray(final long[] array) {
        return new Arr(Arrays.copyOf(array, Math.max(MIN_CAPACITY, array.length)), 0, array.length, true);
    }

    public static Arr fromIntArray(final int[] array) {
        final long[] b = new long[Math.max(MIN_CAPACITY, array.length)];
        for (int i = 0; i < array.length; i++) {
            b[i] = array[i];
        }
        return new Arr(b, 0, array.length, true);
    }

    public static Arr fromIterable(final Iterable<Long> iterable) {
        return fromIterator(iterable.iterator());
    }

    public static Arr fromIterator(final Iterator<Long> iterator) {
        Arr array = new Arr();
        while (iterator.hasNext()) {
            final long l = iterator.next();
            array = array.addLast(l);
        }
        return array;
    }

    public static Arr range(final long startInclusive, final long endExclusive) {
        int n = (int) (endExclusive - startInclusive);
        final Arr array = new Arr(new long[n], 0, 0, true);
        for (long value = startInclusive; value < endExclusive; value++) {
            array.addLast(value);
        }
        return array;
    }

    public Arr addLast(final long value) {
        if (isFull) {
            return grow().addLast(value);
        } else if (safeToGrow) {
            final int i = (start + length) % a.length;
            a[i] = value;
            return new Arr(a, start, length + 1, true);
        } else {
            return clone().addLast(value);
        }
    }

    public Arr removeLast() {
        return isEmpty ? this : new Arr(a, start, length - 1, false);
    }

    public Arr addFirst(final long value) {
        if (isFull) {
            return grow().addFirst(value);
        } else if (safeToGrow) {
            final int i = (start - 1) % a.length;
            a[i] = value;
            return new Arr(a, i, length + 1, true);
        } else {
            return clone().addFirst(value);
        }
    }

    public Arr removeFirst() {
        return isEmpty ? this : new Arr(a, start + 1, length - 1, false);
    }

    public long at(final int i) {
        return a[(start + i) % a.length];
    }

    public boolean has(final long value) {
        for (int i = 0; i < length; i++) {
            if (at(i) == value) {
                return true;
            }
        }
        return false;
    }

    public boolean atLeastOneIs(final LongToBool predicate) {
        for (int i = 0; i < length; i++) {
            if (predicate.test(at(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean atLeastOneIsNot(final LongToBool predicate) {
        return atLeastOneIs(predicate.negate());
    }

    public boolean allMatch(final long value) {
        for (int i = 0; i < length; i++) {
            if (at(i) != value) {
                return false;
            }
        }
        return true;
    }

    public boolean allAre(final LongToBool predicate) {
        for (int i = 0; i < length; i++) {
            if (!predicate.test(at(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean noneMatch(final long value) {
        for (int i = 0; i < length; i++) {
            if (at(i) == value) {
                return false;
            }
        }
        return true;
    }

    public boolean noneIs(final LongToBool predicate) {
        for (int i = 0; i < length; i++) {
            if (predicate.test(at(i))) {
                return false;
            }
        }
        return true;
    }

    public <R> R reduce(final R init, final LongToR<R> reducer) {
        R acc = init;
        for (int i = 0; i < length; i++) {
            acc = reducer.reduce(acc, at(i));
        }
        return acc;
    }

    public long sum() {
        long sum = 0L;
        for (int i = 0; i < length; i++) {
            sum += at(i);
        }
        return sum;
    }

    public long prod() {
        long prod = 1L;
        for (int i = 0; i < length; i++) {
            prod *= at(i);
        }
        return prod;
    }

    public long min() {
        long min = Long.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            min = Math.min(min, at(i));
        }
        return min;
    }

    public long max() {
        long max = Long.MIN_VALUE;
        for (int i = 0; i < length; i++) {
            max = Math.max(max, at(i));
        }
        return max;
    }

    public Arr where(final LongToBool predicate) {
        Arr array = new Arr();
        for (int i = 0; i < length; i++) {
            final long value = at(i);
            if (predicate.test(value)) {
                array = array.addLast(value);
            }
        }
        return array;
    }

    public Arr reverse() {
        final Arr arr = clone();
        for (int i = 0; i < length / 2; i++) {
            arr.a[i] = arr.a[length - i];
        }
        return arr;
    }

    public Arr sorted() {
        final Arr arr = clone();
        Arrays.sort(arr.a, 0, length);
        return arr;
    }

    public Arr unique() {
        Arr arr = new Arr();
        final Set<Long> visited = new HashSet<>();
        for (int i = 0; i < length; i++) {
            final long value = at(i);
            if (!visited.contains(value)) {
                arr = arr.addLast(value);
                visited.add(value);
            }
        }
        return arr;
    }

    public Arr first(final int n) {
        Arr arr = new Arr();
        for (int i = 0; i < length && i < n; i++) {
            arr = arr.addLast(at(i));
        }
        return arr;
    }

    public Arr last(final int n) {
        Arr arr = new Arr();
        for (int i = Math.max(0, length - n); i < length; i++) {
            arr = arr.addLast(at(i));
        }
        return arr;
    }

    public Arr skip(final int n) {
        Arr arr = new Arr();
        for (int i = n; i < length; i++) {
            arr = arr.addLast(at(i));
        }
        return arr;
    }

    public Arr takeWhile(final LongToBool predicate) {
        Arr arr = new Arr();
        for (int i = 0; i < length; i++) {
            final long value = at(i);
            if (predicate.test(value)) {
                arr = arr.addLast(value);
            } else {
                break;
            }
        }
        return arr;
    }

//    public Arr skipWhile(final Predicate predicate){
//        Arr arr = new Arr();
//        boolean criteriaMet = false;
//        for(int i = 0; i < length; i++) {
//            final long value = at(i);
//            if (predicate.test(value)) {
//                criteriaMet = t
//            } else {
//                break;
//            }
//        }
//        return arr;
//    }


    public Arr map(final LongToLong transform) {
        Arr arr = new Arr();
        for (int i = 0; i < length; i++) {
            arr = arr.addLast(transform.apply(at(i)));
        }
        return arr;
    }

    public Arr mapWith(final Arr other, final LongLongToLong transform) {
        Arr arr = new Arr();
        for (int i = 0; i < Math.min(length, other.length); i++) {
            arr = arr.addLast(transform.apply(at(i), other.at(i)));
        }
        return arr;
    }

    public Arr prodWith(final Arr other, final LongLongToLong transform) {
        Arr arr = new Arr();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < other.length; j++) {
                arr = arr.addLast(transform.apply(at(i), other.at(j)));
            }
        }
        return arr;
    }

    public Arr prodUpperTriangleWith(final Arr other, final LongLongToLong transform) {
        Arr arr = new Arr();
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < other.length; j++) {
                arr = arr.addLast(transform.apply(at(i), other.at(j)));
            }
        }
        return arr;
    }

    public Arr flatMap(final LongToArr transform) {
        Arr arr = new Arr();
        for (int i = 0; i < length; i++) {
            final Arr sub = transform.apply(at(i));
            for (int j = 0; j < sub.length; j++) {
                arr = arr.addLast(sub.at(j));
            }
        }
        return arr;
    }

    public Arr print() {
        return print(" ");
    }

    public Arr print(final String separator) {
        return print(separator, (value, i, first, last) -> String.valueOf(value));
    }

    public Arr print(final String separator, final LongToString formatter) {
        for (int i = 0; i < length; i++) {
            final boolean last = i == length - 1;
            Str.print("%s%s", formatter.format(at(i), i, i == 0, last), last ? "\n" : separator);
        }
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                builder.append(' ');
            }
            builder.append(at(i));
        }
        return builder.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Arr that && that.length == this.length) {
            for (int i = 0; i < length; i++) {
                if (this.at(i) != that.at(i)) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        // FIXME probably very bad function with terrible distribution
        return reduce(13, (acc, value) -> (int) (value >> 32) * (int) value + acc);
    }

    public long[] toArray() {
        final long[] b = new long[length];
        for (int i = 0; i < length; i++) {
            b[i] = at(i);
        }
        return b;
    }

    public int[] toIntArray() {
        final int[] b = new int[length];
        for (int i = 0; i < length; i++) {
            b[i] = (int) at(i);
        }
        return b;
    }

    public Set<Long> toSet() {
        final Set<Long> set = new HashSet<>();
        for (int i = 0; i < length; i++) {
            set.add(at(i));
        }
        return set;
    }

    @FunctionalInterface
    public interface LongToVoid {
        void accept(long value, int i, boolean first, boolean last);
    }

    @FunctionalInterface
    public interface LongToString {
        String format(long value, int i, boolean first, boolean last);
    }

    @FunctionalInterface
    public interface LongToBool {
        boolean test(long value);

        default LongToBool negate() {
            return value -> !test(value);
        }
    }

    @FunctionalInterface
    public interface LongToR<R> {
        R reduce(R accumulator, long value);
    }

    @FunctionalInterface
    public interface LongToLong {
        long apply(long value);
    }

    @FunctionalInterface
    public interface LongLongToLong {
        long apply(long value, long other);
    }

    @FunctionalInterface
    public interface LongToArr {
        Arr apply(long value);
    }
}
