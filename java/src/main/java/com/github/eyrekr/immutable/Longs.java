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
public final class Longs implements Iterable<Long> {
    private static final Pattern NUMBERS = Pattern.compile("(-?\\d+)", Pattern.MULTILINE | Pattern.DOTALL);
    private static final int MIN_CAPACITY = 16;

    public final int length;
    public final boolean isEmpty;
    public final boolean isNotEmpty;
    private final long[] a;
    private final int start;
    private final boolean isFull;
    private final boolean safeToAdd; // copy-on-write safeguard: addLast(1).removeLast().addLast(2) => 2 must not rewrite the 1

    private Longs() {
        this.a = new long[MIN_CAPACITY];
        this.length = 0;
        this.isEmpty = true;
        this.isNotEmpty = false;
        this.start = 0;
        this.isFull = false;
        this.safeToAdd = true;
    }

    private Longs(final long[] a, final int start, final int length, final boolean safeToAdd) {
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
    public Longs clone(final double factor) {
        final int n = Math.max(MIN_CAPACITY, Math.max(a.length, (int) (a.length * factor)));
        final long[] b = new long[n];
        for (int i = 0; i < length; i++) b[i] = at(i);
        return new Longs(b, 0, length, true);
    }

    /**
     * @return New empty array.
     * @complexity O(1)
     */
    public static Longs empty() {
        return new Longs();
    }

    /**
     * @return New array of the given size where all values are initialized to the given value.
     * @complexity O(n)
     */
    public static Longs repeat(final long value, final int n) {
        final long[] b = new long[n];
        Arrays.fill(b, value);
        return new Longs(b, 0, b.length, true);
    }

    /**
     * @return New array from the given values.
     * @complexity O(n)
     */
    public static Longs of(final long value, final long... values) {
        Longs arr = new Longs();
        arr = arr.addLast(value);
        if (values != null) for (final long v : values) arr = arr.addLast(v);
        return arr;
    }

    /**
     * @return Deep clone of the supplied array. Operations with this array do not affect the supplied array and vice versa.
     * @complexity O(n)
     */
    public static Longs fromArray(final long[] array) {
        return new Longs(Arrays.copyOf(array, Math.max(MIN_CAPACITY, array.length)), 0, array.length, true);
    }

    /**
     * @return Deep clone of the supplied array. Convenience method for converting from int[].
     * @complexity O(n)
     */
    public static Longs fromIntArray(final int[] array) {
        final long[] b = new long[Math.max(MIN_CAPACITY, array.length)];
        for (int i = 0; i < array.length; i++) b[i] = array[i];
        return new Longs(b, 0, array.length, true);
    }

    /**
     * @return Longs parsed from values of the array.
     * @complexity O(n)
     */
    public static Longs fromStringArray(final String[] array) {
        final long[] b = new long[Math.max(MIN_CAPACITY, array.length)];
        for (int i = 0; i < array.length; i++) b[i] = Long.valueOf(array[i]);
        return new Longs(b, 0, array.length, true);
    }

    /**
     * @return Array from the supplied iterable collection.
     * @complexity O(n)
     */
    public static Longs fromIterable(final Iterable<Long> iterable) {
        return fromIterator(iterable.iterator());
    }

    /**
     * @return Array using the supplied iterator.
     * @complexity O(n)
     */
    public static Longs fromIterator(final Iterator<Long> iterator) {
        Longs array = new Longs();
        while (iterator.hasNext()) array = array.addLast(iterator.next());
        return array;
    }

    /**
     * @return Array of all long values extracted from the string.
     */
    public static Longs fromString(final String string) {
        final var matcher = NUMBERS.matcher(string);
        Longs arr = Longs.empty();
        while (matcher.find()) arr = arr.addLast(Long.parseLong(matcher.group()));
        return arr;
    }

    /**
     * @return New array with all values in the range.
     * @complexity O(n)
     */
    public static Longs range(final long startInclusive, final long endExclusive) {
        int n = (int) (endExclusive - startInclusive);
        Longs array = new Longs(new long[n], 0, 0, true);
        for (long value = startInclusive; value < endExclusive; value++) array = array.addLast(value);
        return array;
    }

    /**
     * @return New array with the value at the end of the array.
     * @complexity O(1)
     */
    public Longs addLast(final long value) {
        if (isFull) {
            return clone(2).addLast(value);
        } else if (safeToAdd) {
            final int i = (start + length) % a.length;
            a[i] = value;
            return new Longs(a, start, length + 1, true);
        } else {
            return clone(1).addLast(value);
        }
    }

    /**
     * @return New array with the values at the end of the array.
     * @complexity O(n)
     */
    public Longs addLast(final Longs values) {
        Longs tmp = this;
        for (int i = 0; i < values.length; i++) tmp = tmp.addLast(values.at(i));
        return tmp;
    }

    /**
     * @return New array without the last value. If the array is empty, the operation does not do anything.
     * @complexity O(1)
     */
    public Longs removeLast() {
        return isEmpty ? this : new Longs(a, start, length - 1, false);
    }

    /**
     * @return New array with the value at the beginning of the array.
     * @complexity O(1)
     */
    public Longs addFirst(final long value) {
        if (isFull) {
            return clone(2).addFirst(value);
        } else if (safeToAdd) {
            final int i = (a.length + start - 1) % a.length;
            a[i] = value;
            return new Longs(a, i, length + 1, true);
        } else {
            return clone(1).addFirst(value);
        }
    }

    /**
     * @return New array without the first value. If the array is empty, the operation does not do anything.
     * @complexity O(1)
     */
    public Longs removeFirst() {
        return isEmpty ? this : new Longs(a, start + 1, length - 1, false);
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
        for (int i = 0; i < length; i++) if (at(i) == value) return true;
        return false;
    }

    /**
     * @return True, if at least one value satisfies the predicate.
     * @complexity O(n)
     */
    public boolean atLeastOneIs(final LongToBool predicate) {
        for (int i = 0; i < length; i++) if (predicate.test(at(i))) return true;
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
        for (int i = 0; i < length; i++) if (at(i) != value) return false;
        return true;
    }

    /**
     * @return True, if all values satisfy the predicate.
     * @complexity O(n)
     */
    public boolean allAre(final LongToBool predicate) {
        for (int i = 0; i < length; i++) if (!predicate.test(at(i))) return false;
        return true;
    }

    /**
     * @return True, if no values matches the given value.
     * @complexity O(n)
     */
    public boolean noneMatch(final long value) {
        for (int i = 0; i < length; i++) if (at(i) == value) return false;
        return true;
    }

    /**
     * @return True, if no value satisfies the predicate.
     * @complexity O(n)
     */
    public boolean noneIs(final LongToBool predicate) {
        for (int i = 0; i < length; i++) if (predicate.test(at(i))) return false;
        return true;
    }

    /**
     * @param init    Initial value for the accumulator.
     * @param reducer Function combining the accumulator with the values of the array.
     * @return The accumulated value.
     * @complexity O(n)
     */
    public <R> R reduce(final R init, final AccumulatorLongToR<R> reducer) {
        R acc = init;
        for (int i = 0; i < length; i++) acc = reducer.reduce(acc, at(i));
        return acc;
    }

    /**
     * @param init    Initial value for the accumulator.
     * @param reducer Function combining the accumulator with the values of the array.
     * @return The accumulated value.
     * @complexity O(n)
     */
    public <R> R reduce(final R init, final AccumulatorContextToR<R> reducer) {
        R acc = init;
        for (int i = 0; i < length; i++)
            acc = reducer.reduce(acc, at(i), at(i + 1), at(i - 1), i, i == 0, i == length - 1);
        return acc;
    }

    /**
     * @return Sum of all values of the array. 0 for empty array.
     * @complexity O(n)
     */
    public long sum() {
        long sum = 0L;
        for (int i = 0; i < length; i++) sum += at(i);
        return sum;
    }

    /**
     * @return Product of all values of the array. 0 for empty array.
     * @complexity O(n)
     */
    public long prod() {
        if (isEmpty) return 0L;
        long prod = 1L;
        for (int i = 0; i < length; i++) prod *= at(i);
        return prod;
    }

    /**
     * @return Minimum value in the array. Long.MAX_VALUE for empty array.
     * @complexity O(n)
     */
    public long min() {
        long min = Long.MAX_VALUE;
        for (int i = 0; i < length; i++) min = Math.min(min, at(i));
        return min;
    }

    /**
     * @return Maximum value in the array. Long.MIN_VALUE for empty array.
     * @complexity O(n)
     */
    public long max() {
        long max = Long.MIN_VALUE;
        for (int i = 0; i < length; i++) max = Math.max(max, at(i));
        return max;
    }

    /**
     * @return New array with only those values that satisfy the predicate.
     * @complexity O(n)
     */
    public Longs where(final LongToBool predicate) {
        Longs array = new Longs();
        for (int i = 0; i < length; i++) {
            final long value = at(i);
            if (predicate.test(value)) array = array.addLast(value);
        }
        return array;
    }

    /**
     * @return New array with the positive values only.
     * @complexity O(n)
     */
    public Longs positiveValuesOnly() {
        Longs array = new Longs();
        for (int i = 0; i < length; i++) {
            final long value = at(i);
            if (value > 0) array = array.addLast(value);
        }
        return array;
    }

    /**
     * @return New array with the negative values only.
     * @complexity O(n)
     */
    public Longs negativeValuesOnly() {
        Longs array = new Longs();
        for (int i = 0; i < length; i++) {
            final long value = at(i);
            if (value < 0) array = array.addLast(value);
        }
        return array;
    }

    /**
     * @return Number of values that satisfy the predicate.
     * @complexity O(n)
     */
    public long countWhere(final LongToBool predicate) {
        long count = 0L;
        for (int i = 0; i < length; i++) if (predicate.test(at(i))) count++;
        return count;
    }

    /**
     * @return Two new arrays, one with the value that satisfy the predicate and the other with values that don't.
     * @complexity O(n)
     */
    public Partition partitionBy(final LongToBool predicate) {
        Longs matching = new Longs(), rest = new Longs();
        for (int i = 0; i < length; i++) {
            final long value = at(i);
            if (predicate.test(value)) matching = matching.addLast(value);
            else rest = rest.addLast(value);
        }
        return new Partition(matching, rest);
    }

    /**
     * @return New array with the values in reversed order.
     * @complexity O(n)
     */
    public Longs reversed() {
        final Longs arr = clone(1);
        for (int i = 0; i < length; i++) arr.a[i] = at(length - i - 1);
        return arr;
    }

    /**
     * @return Order of the values.
     * @complexity O(n)
     */
    public Order ordering() {
        Order order = Order.Constant;
        for (int i = 0; i < length - 1; i++) {
            final long l = at(i), r = at(i + 1);
            if (l < r) {
                order = switch (order) {
                    case Ascending, Constant -> Order.Ascending;
                    case Descending, Random -> Order.Random;
                };
            } else if (l > r) {
                order = switch (order) {
                    case Descending, Constant -> Order.Descending;
                    case Ascending, Random -> Order.Random;
                };
            }
            if (order == Order.Random) break;
        }
        return order;
    }

    /**
     * @return New array with the values sorted in the ascending order.
     * @complexity O(n log n)
     */
    public Longs sorted() {
        final Longs arr = clone(1);
        Arrays.sort(arr.a, 0, length);
        return arr;
    }

    /**
     * @return New array with the values sorted by the surrogate values.
     * @complexity O(n log n)
     */
    public Longs sortedBy(final LongToLong transform) {
        return sortedBy((a, b) -> Long.compare(transform.apply(a), transform.apply(b)));
    }

    /**
     * @return New array with the values sorted in the given order.
     * @complexity O(n log n)
     */
    public Longs sortedBy(final LongLongToInt comparator) {
        final Longs arr = clone(1);
        quicksort(arr.a, 0, length - 1, comparator);
        return arr;
    }

    private static void quicksort(final long[] a, final int begin, final int end, final LongLongToInt comparator) {
        final long pivot = a[(begin + end) / 2];
        int l = begin, r = end;
        do {
            while (comparator.apply(a[l], pivot) < 0 && l < end) l++;
            while (comparator.apply(a[r], pivot) > 0 && r > begin) r--;
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
    public Longs unique() {
        Longs arr = new Longs();
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
    public Longs first(final int n) {
        Longs arr = new Longs();
        for (int i = 0; i < length && i < n; i++) arr = arr.addLast(at(i));
        return arr;
    }

    /**
     * @return New array with the last N values.
     * @complexity O(n)
     */
    public Longs last(final int n) {
        Longs arr = new Longs();
        for (int i = Math.max(0, length - n); i < length; i++) arr = arr.addLast(at(i));
        return arr;
    }

    /**
     * @return New array without the first N values.
     * @complexity O(n)
     */
    public Longs skip(final int n) {
        Longs arr = new Longs();
        for (int i = n; i < length; i++) arr = arr.addLast(at(i));
        return arr;
    }

    /**
     * @return New array with the values before the predicate was first not satisfied.
     * @complexity O(n)
     */
    public Longs takeWhile(final LongToBool predicate) {
        Longs arr = new Longs();
        for (int i = 0; i < length; i++) {
            final long value = at(i);
            if (predicate.test(value)) arr = arr.addLast(value);
            else break;
        }
        return arr;
    }

    /**
     * @return New array with the values after the predicate was first not satisfied.
     * @complexity O(n)
     */
    public Longs skipWhile(final LongToBool predicate) {
        Longs arr = new Longs();
        boolean skip = true;
        for (int i = 0; i < length; i++) {
            final long value = at(i);
            if (skip && !predicate.test(value)) skip = false;
            if (!skip) arr = arr.addLast(value);
        }
        return arr;
    }


    /**
     * @return New array with values transformed using the supplied function.
     * @complexity O(n)
     */
    public Longs map(final LongToLong transform) {
        Longs arr = new Longs();
        for (int i = 0; i < length; i++) arr = arr.addLast(transform.apply(at(i)));
        return arr;
    }

    /**
     * @return New array with values zipped with the other array transformed using the given binary function.
     * @complexity O(n)
     */
    public Longs mapWith(final Longs other, final LongLongToLong transform) {
        Longs arr = new Longs();
        for (int i = 0; i < Math.min(length, other.length); i++) arr = arr.addLast(transform.apply(at(i), other.at(i)));
        return arr;
    }

    /**
     * @return New array with all the possible combinations of values transformed using the given binary function.
     * @complexity O(n ^ 2)
     */
    public Longs prodWith(final Longs other, final LongLongToLong transform) {
        Longs arr = new Longs();
        for (int i = 0; i < length; i++)
            for (int j = 0; j < other.length; j++)
                arr = arr.addLast(transform.apply(at(i), other.at(j)));
        return arr;
    }

    /**
     * @return New array with all possible unique combinations of values transformed using the given binary function.
     * @complexity O(n ^ 2)
     */
    public Longs prodUpperTriangleWith(final Longs other, final LongLongToLong transform) {
        Longs arr = new Longs();
        for (int i = 0; i < length; i++)
            for (int j = i + 1; j < other.length; j++)
                arr = arr.addLast(transform.apply(at(i), other.at(j)));
        return arr;
    }

    /**
     * @return New array with values transformed using the supplied function.
     * @complexity O(n ^ 2)
     */
    public Longs flatMap(final LongToArr transform) {
        Longs arr = new Longs();
        for (int i = 0; i < length; i++) {
            final Longs sub = transform.apply(at(i));
            for (int j = 0; j < sub.length; j++) arr = arr.addLast(sub.at(j));
        }
        return arr;
    }

    /**
     * Perform an operation with each value of this array.
     *
     * @complexity O(n)
     */
    public Longs each(final LongToVoid consumer) {
        for (int i = 0; i < length; i++) consumer.apply(at(i));
        return this;
    }

    /**
     * @return New array with differences between each two consecutive values.
     * @complexity O(n)
     */
    public Longs deltas() {
        return mapWith(removeFirst(), (value, next) -> next - value);
    }

    public Longs print() {
        return print(" ");
    }

    public Longs print(final String separator) {
        return print(separator, (value, i, first, last) -> String.valueOf(value));
    }

    public Longs print(final String separator, final RichLongToString formatter) {
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
            if (i > 0) builder.append(' ');
            builder.append(at(i));
        }
        return builder.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Longs that && that.length == this.length) {
            for (int i = 0; i < length; i++) if (this.at(i) != that.at(i)) return false;
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
        for (int i = 0; i < length; i++) b[i] = at(i);
        return b;
    }

    public int[] toIntArray() {
        final int[] b = new int[length];
        for (int i = 0; i < length; i++) b[i] = (int) at(i);
        return b;
    }

    public Set<Long> toSet() {
        final Set<Long> set = new HashSet<>();
        for (int i = 0; i < length; i++) set.add(at(i));
        return set;
    }

    public <R> Arr<R> toArr(final LongToR<? extends R> transform) {
        Arr<R> arr = Arr.empty();
        for (int i = 0; i < length; i++) arr.addLast(transform.apply(at(i)));
        return arr;
    }

    public Map<Long, Long> frequency() {
        final Map<Long, Long> frequencies = new HashMap<>();
        for (int i = 0; i < length; i++) {
            final long value = at(i);
            final long count = frequencies.getOrDefault(value, 0L) + 1;
            frequencies.put(value, count);
        }
        return frequencies;
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
    public interface LongToR<R> {
        R apply(long value);
    }

    @FunctionalInterface
    public interface AccumulatorLongToR<R> {
        R reduce(R accumulator, long value);
    }

    @FunctionalInterface
    public interface AccumulatorContextToR<R> {
        R reduce(R accumulator, long value, long nextValue, long previousValue, int i, boolean first, boolean last);
    }

    @FunctionalInterface
    public interface LongToLong {
        long apply(long value);
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
    public interface LongToArr {
        Longs apply(long value);
    }

    @FunctionalInterface
    public interface LongLongToInt {
        int apply(long a, long b);
    }

    public enum Order {
        Random, Ascending, Descending, Constant
    }

    public record Partition(Longs matching, Longs rest) {
    }

    private class It implements Iterator<Long> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < Longs.this.length;
        }

        @Override
        public Long next() {
            return Longs.this.at(i++);
        }
    }
}
