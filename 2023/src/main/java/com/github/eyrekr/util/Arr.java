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

    public final int length;
    public final boolean isEmpty;
    private final long[] a;
    private final int start;
    private final boolean isFull;

    private Arr() {
        this.a = new long[MIN_CAPACITY];
        this.length = 0;
        this.isEmpty = true;
        this.start = 0;
        this.isFull = false;
    }

    private Arr(final long[] a, final int start, final int length) {
        this.a = a;
        this.start = start;
        this.length = length;
        this.isEmpty = this.length == 0;
        this.isFull = this.length == a.length;
    }

    private Arr grow() {
        final long[] b = new long[a.length * 2];
        for (int i = 0; i < length; i++) {
            b[i] = a[(start + i) % a.length];
        }
        return new Arr(b, 0, length);
    }

    public Arr deepCopy() {
        final long[] b = new long[a.length];
        for (int i = 0; i < length; i++) {
            b[i] = at(i);
        }
        return new Arr(b, 0, length);
    }

    public static Arr empty() {
        return new Arr();
    }

    public static Arr fromArray(final long[] array) {
        return new Arr(Arrays.copyOf(array, Math.max(MIN_CAPACITY, array.length)), 0, array.length);
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
        final Arr array = new Arr(new long[n], 0, 0);
        for (long value = startInclusive; value < endExclusive; value++) {
            array.addLast(value);
        }
        return array;
    }

    public Arr addLast(final long value) {
        if (isFull) {
            return grow().addLast(value);
        }
        final int i = (start + length) % a.length;
        a[i] = value;
        return new Arr(a, start, length + 1);
    }

    public Arr addFirst(final long value) {
        if (isFull) {
            return grow().addFirst(value);
        }
        final int i = (start - 1) % a.length;
        a[i] = value;
        return new Arr(a, i, length + 1);
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

    public boolean atLeastOneIs(final Predicate predicate) {
        for (int i = 0; i < length; i++) {
            if (predicate.test(at(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean atLeastOneIsNot(final Predicate predicate) {
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

    public boolean allAre(final Predicate predicate) {
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

    public boolean noneIs(final Predicate predicate) {
        for (int i = 0; i < length; i++) {
            if (predicate.test(at(i))) {
                return false;
            }
        }
        return true;
    }

    public Arr each(final Consumer consumer) {
        for (int i = 0; i < length; i++) {
            consumer.accept(at(i), i, i == 0, i == length - 1);
        }
        return this;
    }

    public <R> R reduce(final R init, final Reducer<R> reducer) {
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

    public Arr where(final Predicate predicate) {
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
        final Arr arr = deepCopy();
        for (int i = 0; i < length / 2; i++) {
            arr.a[i] = arr.a[length - i];
        }
        return arr;
    }

    public Arr sorted() {
        final Arr arr = deepCopy();
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

    public Arr takeWhile(final Predicate predicate) {
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


    public Arr map(final Function transform) {
        Arr arr = new Arr();
        for (int i = 0; i < length; i++) {
            arr = arr.addLast(transform.apply(at(i)));
        }
        return arr;
    }

    public Arr flatMap(final Function2 transform) {
        Arr arr = new Arr();
        for (int i = 0; i < length; i++) {
            final Arr xxx = transform.apply(at(i));
            for(int j = 0; j < xxx.length; j++) {
                arr = arr.addLast(xxx.at(j));
            }
        }
        return arr;
    }

    public Arr print(final String separator, final Formatter formatter) {
        Str.print("[");
        each((value, i, first, last) -> Str.print("%s%s", formatter.format(value, i, first, last), last ? "" : separator));
        Str.print("]\n");
        return this;
    }

    @FunctionalInterface
    public interface Consumer {
        void accept(long value, int i, boolean first, boolean last);
    }

    @FunctionalInterface
    public interface Formatter {
        String format(long value, int i, boolean first, boolean last);
    }

    @FunctionalInterface
    public interface Predicate {
        boolean test(long value);

        default Predicate negate() {
            return value -> !test(value);
        }
    }

    @FunctionalInterface
    public interface Reducer<R> {
        R reduce(R accumulator, long value);
    }

    @FunctionalInterface
    public interface Function {
        long apply(long value);
    }

    @FunctionalInterface
    public interface Function2 {
        Arr apply(long value);
    }
}
