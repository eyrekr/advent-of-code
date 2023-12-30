package com.github.eyrekr.immutable;

import com.github.eyrekr.output.Out;

import java.util.*;
import java.util.regex.Pattern;

/**
 * Auto-expandable immutable array of long numbers.
 * Optimized for better performance than @{@link Seq}.
 * Combines the advantages of both {@link LinkedList} and {@link ArrayList}
 * <ol>
 * <li>fast adding and removing to both ends</li>
 * <li>fast access to all values by their index</li>
 * </ol>
 */
public final class LArr implements Iterable<Long> {
    private static final Pattern NUMBERS = Pattern.compile("(-?\\d+)", Pattern.MULTILINE | Pattern.DOTALL);
    private static final int MIN_CAPACITY = 16;

    public final int length;
    public final boolean isEmpty;
    public final boolean isNotEmpty;
    private final long[] a;
    private final int start;
    private final boolean isFull;
    private final boolean safeToAdd; // copy-on-write safeguard: addLast(1).removeLast().addLast(2) => 2 must not rewrite the 1

    private LArr() {
        this.a = new long[MIN_CAPACITY];
        this.length = 0;
        this.isEmpty = true;
        this.isNotEmpty = false;
        this.start = 0;
        this.isFull = false;
        this.safeToAdd = true;
    }

    private LArr(final long[] a, final int start, final int length, final boolean safeToAdd) {
        this.a = a;
        this.start = start % a.length;
        this.length = length;
        this.isEmpty = this.length == 0;
        this.isNotEmpty = !this.isEmpty;
        this.isFull = this.length == a.length;
        this.safeToAdd = safeToAdd;
    }

    /**
     * @param factor The growth factor for the capacity. Capacity shrinks for factor < 1, stays exactly the same for factor = 1 and grows for factor > 1.
     *               The capacity never drops below the MIN_CAPACITY.
     *               Also, the capacity never drops below the necessary capacity to hold the current elements.
     * @return Deep copy of the array with modified capacity.
     * @complexity O(n)
     */
    public LArr clone(final double factor) {
        final int n = Math.max(MIN_CAPACITY, Math.max(a.length, (int) (a.length * factor)));
        final long[] b = new long[n];
        for (int i = 0; i < length; i++) {
            b[i] = at(i);
        }
        return new LArr(b, 0, length, true);
    }

    /**
     * @return New empty array.
     * @complexity O(1)
     */
    public static LArr empty() {
        return new LArr();
    }

    /**
     * @return New array of the given size where all values are initialized to the given value.
     * @complexity O(n)
     */
    public static LArr repeat(final long value, final int n) {
        final long[] b = new long[n];
        Arrays.fill(b, value);
        return new LArr(b, 0, b.length, true);
    }

    /**
     * @return New array from the given values.
     * @complexity O(n)
     */
    public static LArr of(final long value, final long... values) {
        LArr arr = new LArr();
        arr = arr.addLast(value);
        if (values != null) {
            for (final long v : values) {
                arr = arr.addLast(v);
            }
        }
        return arr;
    }

    /**
     * @return Deep clone of the supplied array. Operations with this array do not affect the supplied array and vice versa.
     * @complexity O(n)
     */
    public static LArr fromArray(final long[] array) {
        return new LArr(Arrays.copyOf(array, Math.max(MIN_CAPACITY, array.length)), 0, array.length, true);
    }

    /**
     * @return Deep clone of the supplied array. Convenience method for converting from int[].
     * @complexity O(n)
     */
    public static LArr fromIntArray(final int[] array) {
        final long[] b = new long[Math.max(MIN_CAPACITY, array.length)];
        for (int i = 0; i < array.length; i++) {
            b[i] = array[i];
        }
        return new LArr(b, 0, array.length, true);
    }

    /**
     * @return Array from the supplied iterable collection.
     * @complexity O(n)
     */
    public static LArr fromIterable(final Iterable<Long> iterable) {
        return fromIterator(iterable.iterator());
    }

    /**
     * @return Array using the supplied iterator.
     * @complexity O(n)
     */
    public static LArr fromIterator(final Iterator<Long> iterator) {
        LArr array = new LArr();
        while (iterator.hasNext()) {
            final long l = iterator.next();
            array = array.addLast(l);
        }
        return array;
    }

    /**
     * @return Array of all long values extracted from the string.
     */
    public static LArr fromString(final String string) {
        final var matcher = NUMBERS.matcher(string);
        LArr arr = LArr.empty();
        while (matcher.find()) {
            arr = arr.addLast(Long.parseLong(matcher.group()));
        }
        return arr;
    }

    /**
     * @return New array with all values in the range.
     * @complexity O(n)
     */
    public static LArr range(final long startInclusive, final long endExclusive) {
        int n = (int) (endExclusive - startInclusive);
        LArr array = new LArr(new long[n], 0, 0, true);
        for (long value = startInclusive; value < endExclusive; value++) {
            array = array.addLast(value);
        }
        return array;
    }

    /**
     * @return New array with the value at the end of the array.
     * @complexity O(1)
     */
    public LArr addLast(final long value) {
        if (isFull) {
            return clone(2).addLast(value);
        } else if (safeToAdd) {
            final int i = (start + length) % a.length;
            a[i] = value;
            return new LArr(a, start, length + 1, true);
        } else {
            return clone(1).addLast(value);
        }
    }

    /**
     * @return New array with the values at the end of the array.
     * @complexity O(n)
     */
    public LArr addLast(final LArr values) {
        LArr tmp = this;
        for (int i = 0; i < values.length; i++) {
            tmp = tmp.addLast(values.at(i));
        }
        return tmp;
    }

    /**
     * @return New array without the last value. If the array is empty, the operation does not do anything.
     * @complexity O(1)
     */
    public LArr removeLast() {
        return isEmpty ? this : new LArr(a, start, length - 1, false);
    }

    /**
     * @return New array with the value at the beginning of the array.
     * @complexity O(1)
     */
    public LArr addFirst(final long value) {
        if (isFull) {
            return clone(2).addFirst(value);
        } else if (safeToAdd) {
            final int i = (a.length + start - 1) % a.length;
            a[i] = value;
            return new LArr(a, i, length + 1, true);
        } else {
            return clone(1).addFirst(value);
        }
    }

    /**
     * @return New array without the first value. If the array is empty, the operation does not do anything.
     * @complexity O(1)
     */
    public LArr removeFirst() {
        return isEmpty ? this : new LArr(a, start + 1, length - 1, false);
    }

    /**
     * @return The first value in the array. Equivalent of {@code at(0)}.
     * @complexity O(1)
     */
    public long peek() {
        return at(0);
    }

    /**
     * @return Value at the given index. The index neither overflows nor underflows; it goes around.
     * Negative indexes work too: -1 is the last element, -2 is the second last, and so on.
     * If the array is empty, 0 is returned.
     * @complexity O(1)
     */
    public long at(final int i) {
        if (isEmpty) return 0L;
        if (i >= 0) return a[(start + i % length) % a.length];
        else return a[(a.length + start + length + i % length) % a.length];
    }

    /**
     * @return True, if the value is in the array at least once.
     * @complexity O(n)
     */
    public boolean has(final long value) {
        for (int i = 0; i < length; i++) {
            if (at(i) == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return True, if at least one value satisfies the predicate.
     * @complexity O(n)
     */
    public boolean atLeastOneIs(final LongToBool predicate) {
        for (int i = 0; i < length; i++) {
            if (predicate.test(at(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return True, if at least one value does not satisfy the predicate.
     * @complexity O(n)
     */
    public boolean atLeastOneIsNot(final LongToBool predicate) {
        return atLeastOneIs(predicate.negate());
    }

    /**
     * @return True, if all values match the given value.
     * @complexity O(n)
     */
    public boolean allMatch(final long value) {
        for (int i = 0; i < length; i++) {
            if (at(i) != value) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return True, if all values satisfy the predicate.
     * @complexity O(n)
     */
    public boolean allAre(final LongToBool predicate) {
        for (int i = 0; i < length; i++) {
            if (!predicate.test(at(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return True, if no values matches the given value.
     * @complexity O(n)
     */
    public boolean noneMatch(final long value) {
        for (int i = 0; i < length; i++) {
            if (at(i) == value) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return True, if no value satisfies the predicate.
     * @complexity O(n)
     */
    public boolean noneIs(final LongToBool predicate) {
        for (int i = 0; i < length; i++) {
            if (predicate.test(at(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param init    Initial value for the accumulator.
     * @param reducer Function combining the accumulator with the values of the array.
     * @return The accumulated value.
     * @complexity O(n)
     */
    public <R> R reduce(final R init, final LongToR<R> reducer) {
        R acc = init;
        for (int i = 0; i < length; i++) {
            acc = reducer.reduce(acc, at(i));
        }
        return acc;
    }

    /**
     * @return Sum of all values of the array. 0 for empty array.
     * @complexity O(n)
     */
    public long sum() {
        long sum = 0L;
        for (int i = 0; i < length; i++) {
            sum += at(i);
        }
        return sum;
    }

    /**
     * @return Product of all values of the array. 0 for empty array.
     * @complexity O(n)
     */
    public long prod() {
        if (isEmpty) return 0L;
        long prod = 1L;
        for (int i = 0; i < length; i++) {
            prod *= at(i);
        }
        return prod;
    }

    /**
     * @return Minimum value in the array. Long.MAX_VALUE for empty array.
     * @complexity O(n)
     */
    public long min() {
        long min = Long.MAX_VALUE;
        for (int i = 0; i < length; i++) {
            min = Math.min(min, at(i));
        }
        return min;
    }

    /**
     * @return Maximum value in the array. Long.MIN_VALUE for empty array.
     * @complexity O(n)
     */
    public long max() {
        long max = Long.MIN_VALUE;
        for (int i = 0; i < length; i++) {
            max = Math.max(max, at(i));
        }
        return max;
    }

    /**
     * @return New array with only those values that satisfy the predicate.
     * @complexity O(n)
     */
    public LArr where(final LongToBool predicate) {
        LArr array = new LArr();
        for (int i = 0; i < length; i++) {
            final long value = at(i);
            if (predicate.test(value)) {
                array = array.addLast(value);
            }
        }
        return array;
    }

    /**
     * @return New array with the values in reversed order.
     * @complexity O(n)
     */
    public LArr reverse() {
        final LArr arr = clone(1);
        for (int i = 0; i < length; i++) {
            arr.a[i] = at(length - i - 1);
        }
        return arr;
    }

    /**
     * @return New array with the values sorted in the ascending order.
     * @complexity O(n log n)
     */
    public LArr sort() {
        final LArr arr = clone(1);
        arr.print();
        Arrays.sort(arr.a, 0, length);
        return arr;
    }

    /**
     * @return New array with the values sorted in the given order.
     * @complexity O(n log n)
     */
    public LArr sortBy(final Comparator<Long> comparator) {
        final LArr arr = clone(1);
        quicksort(arr.a, 0, length - 1, comparator);
        return arr;
    }

    public static void quicksort(final long[] a, final int begin, final int end, final Comparator<Long> comparator) {
        final long pivot = a[begin + end / 2];
        int l = begin, r = end;
        do {
            while (comparator.compare(a[l], pivot) < 0 && l < end) l++;
            while (comparator.compare(a[r], pivot) > 0 && r > begin) r--;
            if (l <= r) {
                final long t = a[l];
                a[l++] = a[r];
                a[r--] = t;
            }
        } while (l < r);
        if (r > begin) quicksort(a, begin, r, comparator);
        if (l < end) quicksort(a, l, end, comparator);
    }

    /**
     * @return New array with all duplicate values removed duplicates. In other words, only the first occurrence of the value is kept.
     * @complexity O(n)
     */
    public LArr unique() {
        LArr arr = new LArr();
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

    /**
     * @return New array with the first N values.
     * @complexity O(n)
     */
    public LArr first(final int n) {
        LArr arr = new LArr();
        for (int i = 0; i < length && i < n; i++) {
            arr = arr.addLast(at(i));
        }
        return arr;
    }

    /**
     * @return New array with the last N values.
     * @complexity O(n)
     */
    public LArr last(final int n) {
        LArr arr = new LArr();
        for (int i = Math.max(0, length - n); i < length; i++) {
            arr = arr.addLast(at(i));
        }
        return arr;
    }

    /**
     * @return New array without the first N values.
     * @complexity O(n)
     */
    public LArr skip(final int n) {
        LArr arr = new LArr();
        for (int i = n; i < length; i++) {
            arr = arr.addLast(at(i));
        }
        return arr;
    }

    /**
     * @return New array with the values before the predicate was first not satisfied.
     * @complexity O(n)
     */
    public LArr takeWhile(final LongToBool predicate) {
        LArr arr = new LArr();
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

    /**
     * @return New array with the values after the predicate was first not satisfied.
     * @complexity O(n)
     */
    public LArr skipWhile(final LongToBool predicate) {
        LArr arr = new LArr();
        boolean skip = true;
        for (int i = 0; i < length; i++) {
            final long value = at(i);
            if (skip && !predicate.test(value)) {
                skip = false;
            }
            if (!skip) {
                arr = arr.addLast(value);
            }
        }
        return arr;
    }


    /**
     * @return New array with values transformed using the supplied function.
     * @complexity O(n)
     */
    public LArr map(final LongToLong transform) {
        LArr arr = new LArr();
        for (int i = 0; i < length; i++) {
            arr = arr.addLast(transform.apply(at(i)));
        }
        return arr;
    }

    /**
     * @return New array with values zipped with the other array transformed using the given binary function.
     * @complexity O(n)
     */
    public LArr mapWith(final LArr other, final LongLongToLong transform) {
        LArr arr = new LArr();
        for (int i = 0; i < Math.min(length, other.length); i++) {
            arr = arr.addLast(transform.apply(at(i), other.at(i)));
        }
        return arr;
    }

    /**
     * @return New array with all the possible combinations of values transformed using the given binary function.
     * @complexity O(n ^ 2)
     */
    public LArr prodWith(final LArr other, final LongLongToLong transform) {
        LArr arr = new LArr();
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < other.length; j++) {
                arr = arr.addLast(transform.apply(at(i), other.at(j)));
            }
        }
        return arr;
    }

    /**
     * @return New array with all possible unique combinations of values transformed using the given binary function.
     * @complexity O(n ^ 2)
     */
    public LArr prodUpperTriangleWith(final LArr other, final LongLongToLong transform) {
        LArr arr = new LArr();
        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < other.length; j++) {
                arr = arr.addLast(transform.apply(at(i), other.at(j)));
            }
        }
        return arr;
    }

    /**
     * @return New array with values transformed using the supplied function.
     * @complexity O(n ^ 2)
     */
    public LArr flatMap(final LongToArr transform) {
        LArr arr = new LArr();
        for (int i = 0; i < length; i++) {
            final LArr sub = transform.apply(at(i));
            for (int j = 0; j < sub.length; j++) {
                arr = arr.addLast(sub.at(j));
            }
        }
        return arr;
    }

    /**
     * @complexity O(n)
     * Perform an operation with each value of this array.
     */
    public LArr each(final LongToVoid consumer) {
        for (int i = 0; i < length; i++) {
            final long value = at(i);
            consumer.apply(value);
        }
        return this;
    }

    public LArr print() {
        return print(" ");
    }

    public LArr print(final String separator) {
        return print(separator, (value, i, first, last) -> String.valueOf(value));
    }

    public LArr print(final String separator, final RichLongToString formatter) {
        for (int i = 0; i < length; i++) {
            final boolean last = i == length - 1;
            Out.print("%s%s", formatter.format(at(i), i, i == 0, last), last ? "\n" : separator);
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
        if (obj instanceof LArr that && that.length == this.length) {
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

    @Override
    public Iterator<Long> iterator() {
        return new It();
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
    public interface RichLongToString {
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
    public interface RichLongToBool {
        boolean test(long value, int i, boolean first, boolean last);

        default RichLongToBool negate() {
            return (value, i, first, last) -> !test(value, i, first, last);
        }
    }

    @FunctionalInterface
    public interface LongToR<R> {
        R reduce(R accumulator, long value);
    }

    @FunctionalInterface
    public interface RichLongToR<R> {
        R reduce(R accumulator, long value, int i, boolean first, boolean last);
    }

    @FunctionalInterface
    public interface LongToLong {
        long apply(long value);
    }

    @FunctionalInterface
    public interface RichLongToLong {
        long apply(long value, int i, boolean first, boolean last);
    }

    @FunctionalInterface
    public interface LongToVoid {
        void apply(long value);
    }


    @FunctionalInterface
    public interface LongLongToLong {
        long apply(long value, long other);
    }

    @FunctionalInterface
    public interface RichLongLongToLong {
        long apply(long value, long other, int i, boolean first, boolean last);
    }

    @FunctionalInterface
    public interface LongToArr {
        LArr apply(long value);
    }

    @FunctionalInterface
    public interface RichLongToArr {
        LArr apply(long value, int i, boolean first, boolean last);
    }

    private class It implements Iterator<Long> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < LArr.this.length;
        }

        @Override
        public Long next() {
            return LArr.this.at(i++);
        }
    }
}
