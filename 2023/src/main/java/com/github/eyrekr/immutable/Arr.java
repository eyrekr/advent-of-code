package com.github.eyrekr.immutable;

import com.github.eyrekr.output.Out;

import java.util.*;
import java.util.function.*;
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
public final class Arr<E> implements Iterable<E> {
    private static final Pattern NUMBERS = Pattern.compile("(-?\\d+)", Pattern.MULTILINE | Pattern.DOTALL);
    private static final int MIN_CAPACITY = 16;

    public final int length;
    public final boolean isEmpty;
    public final boolean isNotEmpty;
    private final Object[] a;
    private final int start;
    private final boolean isFull;
    private final boolean safeToAdd; // copy-on-write safeguard: addLast(1).removeLast().addLast(2) => 2 must not rewrite the 1

    private Arr() {
        this.a = new Object[MIN_CAPACITY];
        this.length = 0;
        this.isEmpty = true;
        this.isNotEmpty = false;
        this.start = 0;
        this.isFull = false;
        this.safeToAdd = true;
    }

    private Arr(final Object[] a, final int start, final int length, final boolean safeToAdd) {
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
    public Arr<E> clone(final double factor) {
        final int n = Math.max(MIN_CAPACITY, Math.max(a.length, (int) (a.length * factor)));
        final Object[] b = new Object[n];
        for (int i = 0; i < length; i++) {
            b[i] = at(i);
        }
        return new Arr<>(b, 0, length, true);
    }

    /**
     * @return New empty array.
     * @complexity O(1)
     */
    public static <T> Arr<T> empty() {
        return new Arr<>();
    }

    /**
     * @return New array of the given size where all values are initialized to the given value.
     * @complexity O(n)
     */
    public static <T> Arr<T> repeat(final T value, final int n) {
        final Object[] b = new Object[n];
        Arrays.fill(b, value);
        return new Arr<>(b, 0, b.length, true);
    }

    /**
     * @return New array from the given values.
     * @complexity O(n)
     */
    public static <T> Arr<T> of(final T value, final T... values) {
        Arr<T> arr = new Arr<>();
        arr = arr.addLast(value);
        if (values != null)
            for (final T v : values)
                arr = arr.addLast(v);
        return arr;
    }

    /**
     * @return Deep clone of the supplied array. Operations with this array do not affect the supplied array and vice versa.
     * @complexity O(n)
     */
    public static <T> Arr<T> fromArray(final T[] array) {
        return new Arr<>(Arrays.copyOf(array, Math.max(MIN_CAPACITY, array.length)), 0, array.length, true);
    }

    /**
     * @return Array from the supplied iterable collection.
     * @complexity O(n)
     */
    public static <T> Arr<T> fromIterable(final Iterable<T> iterable) {
        return fromIterator(iterable.iterator());
    }

    /**
     * @return Array using the supplied iterator.
     * @complexity O(n)
     */
    public static <T> Arr<T> fromIterator(final Iterator<T> iterator) {
        Arr<T> array = new Arr<>();
        while (iterator.hasNext()) array = array.addLast(iterator.next());
        return array;
    }

    /**
     * @return New array with the value at the end of the array.
     * @complexity O(1)
     */
    public Arr<E> addLast(final E value) {
        if (isFull) {
            return clone(2).addLast(value);
        } else if (safeToAdd) {
            final int i = (start + length) % a.length;
            a[i] = value;
            return new Arr<>(a, start, length + 1, true);
        } else {
            return clone(1).addLast(value);
        }
    }

    /**
     * @return New array with the values at the end of the array.
     * @complexity O(n)
     */
    public Arr<E> addLast(final Arr<? extends E> values) {
        Arr<E> tmp = this;
        for (int i = 0; i < values.length; i++) tmp = tmp.addLast(values.at(i));
        return tmp;
    }

    /**
     * @return New array without the last value. If the array is empty, the operation does not do anything.
     * @complexity O(1)
     */
    public Arr<E> removeLast() {
        return isEmpty ? this : new Arr<>(a, start, length - 1, false);
    }

    /**
     * @return New array with the value at the beginning of the array.
     * @complexity O(1)
     */
    public Arr<E> addFirst(final E value) {
        if (isFull) {
            return clone(2).addFirst(value);
        } else if (safeToAdd) {
            final int i = (a.length + start - 1) % a.length;
            a[i] = value;
            return new Arr<>(a, i, length + 1, true);
        } else {
            return clone(1).addFirst(value);
        }
    }

    /**
     * @return New array without the first value. If the array is empty, the operation does not do anything.
     * @complexity O(1)
     */
    public Arr<E> removeFirst() {
        return isEmpty ? this : new Arr<>(a, start + 1, length - 1, false);
    }

    /**
     * @return The first value in the array. Equivalent of {@code at(0)}.
     * @complexity O(1)
     */
    public E peek() {
        return at(0);
    }

    /**
     * @return Value at the given index. The index neither overflows nor underflows; it goes around.
     * Negative indexes work too: -1 is the last element, -2 is the second last, and so on.
     * If the array is empty, 0 is returned.
     * @complexity O(1)
     */
    public E at(final int i) {
        if (isEmpty) return null;
        return (i >= 0)
                ? (E) a[(start + i % length) % a.length]
                : (E) a[(a.length + start + length + i % length) % a.length];
    }

    /**
     * @return True, if the value is in the array at least once.
     * @complexity O(n)
     */
    public boolean has(final E value) {
        for (int i = 0; i < length; i++) if (Objects.equals(at(i), value)) return true;
        return false;
    }

    /**
     * @return True, if at least one value satisfies the predicate.
     * @complexity O(n)
     */
    public boolean atLeastOneIs(final Predicate<? super E> predicate) {
        for (int i = 0; i < length; i++) if (predicate.test(at(i))) return true;
        return false;
    }

    /**
     * @return True, if at least one value does not satisfy the predicate.
     * @complexity O(n)
     */
    public boolean atLeastOneIsNot(final Predicate<? super E> predicate) {
        return atLeastOneIs(predicate.negate());
    }

    /**
     * @return True, if all values match the given value.
     * @complexity O(n)
     */
    public boolean allMatch(final E value) {
        for (int i = 0; i < length; i++) if (!Objects.equals(at(i), value)) return false;
        return true;
    }

    /**
     * @return True, if all values satisfy the predicate.
     * @complexity O(n)
     */
    public boolean allAre(final Predicate<? super E> predicate) {
        for (int i = 0; i < length; i++) if (!predicate.test(at(i))) return false;
        return true;
    }

    /**
     * @return True, if no values matches the given value.
     * @complexity O(n)
     */
    public boolean noneMatch(final E value) {
        for (int i = 0; i < length; i++) if (Objects.equals(at(i), value)) return false;
        return true;
    }

    /**
     * @return True, if no value satisfies the predicate.
     * @complexity O(n)
     */
    public boolean noneIs(final Predicate<? super E> predicate) {
        for (int i = 0; i < length; i++)
            if (predicate.test(at(i))) return false;
        return true;
    }

    /**
     * @param init    Initial value for the accumulator.
     * @param reducer Function combining the accumulator with the values of the array.
     * @return The accumulated value.
     * @complexity O(n)
     */
    public <R> R reduce(final R init, final BiFunction<? super R, ? super E, ? extends R> reducer) {
        R acc = init;
        for (int i = 0; i < length; i++) acc = reducer.apply(acc, at(i));
        return acc;
    }

    /**
     * @return The accumulated value.
     * @complexity O(n)
     */
    public E reduce(final BiFunction<? super E, ? super E, ? extends E> reducer) {
        if (isEmpty) return null;
        E acc = at(0);
        for (int i = 1; i < length; i++) acc = reducer.apply(acc, at(i));
        return acc;
    }

    /**
     * @return Minimum value in the array.
     * @complexity O(n)
     */
    public E min(final Comparator<? super E> comparator) {
        E min = null;
        for (int i = 0; i < length; i++)
            if (min == null || comparator.compare(at(i), min) < 0) min = at(i);
        return min;
    }

    /**
     * @return Index of the minimum value in the array.
     * @complexity O(n)
     */
    public int argmin(final Comparator<? super E> comparator) {
        int index = -1;
        for (int i = 0; i < length; i++)
            if (index < 0 || comparator.compare(at(i), at(index)) < 0) index = i;
        return index;
    }

    /**
     * @return Maximum value in the array.
     * @complexity O(n)
     */
    public E max(final Comparator<? super E> comparator) {
        E max = null;
        for (int i = 0; i < length; i++)
            if (max == null || comparator.compare(max, at(i)) < 0) max = at(i);
        return max;
    }

    /**
     * @return Index of the maximum value in the array.
     * @complexity O(n)
     */
    public int argmax(final Comparator<? super E> comparator) {
        int index = -1;
        for (int i = 0; i < length; i++)
            if (index < 0 || comparator.compare(at(index), at(i)) < 0) index = i;
        return index;
    }

    /**
     * @return New array with only those values that satisfy the predicate.
     * @complexity O(n)
     */
    public Arr<E> where(final Predicate<? super E> predicate) {
        Arr<E> array = new Arr<>();
        for (int i = 0; i < length; i++) {
            final E value = at(i);
            if (predicate.test(value)) array = array.addLast(value);
        }
        return array;
    }

    /**
     * @return New array with the values in reversed order.
     * @complexity O(n)
     */
    public Arr<E> reverse() {
        final Arr<E> arr = clone(1);
        for (int i = 0; i < length; i++)
            arr.a[i] = at(length - i - 1);
        return arr;
    }

    /**
     * @return New array with the values sorted in the given order.
     * @complexity O(n log n)
     */
    public Arr<E> sortBy(final Comparator<? super E> comparator) {
        final Arr<E> arr = clone(1);
        quicksort((E[]) arr.a, 0, length - 1, comparator);
        return arr;
    }

    public static <T> void quicksort(final T[] a, final int begin, final int end, final Comparator<? super T> comparator) {
        final T pivot = a[begin + end / 2];
        int l = begin, r = end;
        do {
            while (comparator.compare(a[l], pivot) < 0 && l < end) l++;
            while (comparator.compare(a[r], pivot) > 0 && r > begin) r--;
            if (l <= r) {
                final T t = a[l];
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
    public Arr<E> unique() {
        Arr<E> arr = new Arr<>();
        final Set<E> visited = new HashSet<>();
        for (int i = 0; i < length; i++) {
            final E value = at(i);
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
    public Arr<E> first(final int n) {
        Arr<E> arr = new Arr<>();
        for (int i = 0; i < length && i < n; i++)
            arr = arr.addLast(at(i));
        return arr;
    }

    /**
     * @return New array with the last N values.
     * @complexity O(n)
     */
    public Arr<E> last(final int n) {
        Arr<E> arr = new Arr<>();
        for (int i = Math.max(0, length - n); i < length; i++)
            arr = arr.addLast(at(i));
        return arr;
    }

    /**
     * @return New array without the first N values.
     * @complexity O(n)
     */
    public Arr<E> skip(final int n) {
        Arr<E> arr = new Arr<>();
        for (int i = n; i < length; i++)
            arr = arr.addLast(at(i));
        return arr;
    }

    /**
     * @return New array with the values before the predicate was first not satisfied.
     * @complexity O(n)
     */
    public Arr<E> takeWhile(final Predicate<? super E> predicate) {
        Arr<E> arr = new Arr<>();
        for (int i = 0; i < length; i++) {
            final E value = at(i);
            if (predicate.test(value)) arr = arr.addLast(value);
            else break;
        }
        return arr;
    }

    /**
     * @return New array with the values after the predicate was first not satisfied.
     * @complexity O(n)
     */
    public Arr<E> skipWhile(final Predicate<? super E> predicate) {
        Arr<E> arr = new Arr<>();
        boolean skip = true;
        for (int i = 0; i < length; i++) {
            final E value = at(i);
            if (skip && !predicate.test(value)) skip = false;
            if (!skip) arr = arr.addLast(value);
        }
        return arr;
    }


    /**
     * @return New array with values transformed using the supplied function.
     * @complexity O(n)
     */
    public <R> Arr<R> map(final Function<? super E, ? extends R> transform) {
        Arr<R> arr = new Arr<>();
        for (int i = 0; i < length; i++)
            arr = arr.addLast(transform.apply(at(i)));
        return arr;
    }

    /**
     * @return New array with values zipped with the other array transformed using the given binary function.
     * @complexity O(n)
     */
    public <F, R> Arr<R> mapWith(final Arr<F> other, final BiFunction<? super E, ? super F, ? extends R> transform) {
        Arr<R> arr = new Arr<>();
        for (int i = 0; i < Math.min(length, other.length); i++)
            arr = arr.addLast(transform.apply(at(i), other.at(i)));
        return arr;
    }

    /**
     * @return New array with all the possible combinations of values transformed using the given binary function.
     * @complexity O(n ^ 2)
     */
    public <F, R> Arr<R> prodWith(final Arr<F> other, final BiFunction<? super E, ? super F, ? extends R> transform) {
        Arr<R> arr = new Arr<>();
        for (int i = 0; i < length; i++)
            for (int j = 0; j < other.length; j++)
                arr = arr.addLast(transform.apply(at(i), other.at(j)));
        return arr;
    }

    /**
     * @return New array with all possible unique combinations of values transformed using the given binary function.
     * @complexity O(n ^ 2)
     */
    public <F, R> Arr<R> prodUpperTriangleWith(final Arr<F> other, final BiFunction<? super E, ? super F, ? extends R> transform) {
        Arr<R> arr = new Arr<>();
        for (int i = 0; i < length; i++)
            for (int j = i + 1; j < other.length; j++)
                arr = arr.addLast(transform.apply(at(i), other.at(j)));
        return arr;
    }

    /**
     * @return New array with values transformed using the supplied function.
     * @complexity O(n ^ 2)
     */
    public <R> Arr<R> flatMap(final Function<? super E, Arr<? extends R>> transform) {
        Arr<R> arr = new Arr<>();
        for (int i = 0; i < length; i++) {
            final Arr<? extends R> sub = transform.apply(at(i));
            for (int j = 0; j < sub.length; j++) arr = arr.addLast(sub.at(j));
        }
        return arr;
    }

    /**
     * @complexity O(n)
     * Perform an operation with each value of this array.
     */
    public Arr<E> each(final Consumer<? super E> consumer) {
        for (int i = 0; i < length; i++) consumer.accept(at(i));
        return this;
    }

    public Arr<E> print() {
        return print(" ");
    }

    public Arr<E> print(final String separator) {
        return print(separator, (value, i, first, last) -> String.valueOf(value));
    }

    public Arr<E> print(final String separator, final ContextFunction<? super E, String> formatter) {
        for (int i = 0; i < length; i++) {
            final boolean last = i == length - 1;
            Out.print("%s%s", formatter.apply(at(i), i, i == 0, last), last ? "\n" : separator);
        }
        return this;
    }

    public LArr toLongArr(final Function<? super E, ? extends Number> transform) {
        LArr arr = LArr.empty();
        for (int i = 0; i < length; i++) arr = arr.addLast(transform.apply(at(i)).longValue());
        return arr;
    }

    public E[] toArray(final IntFunction<E[]> arrayGenerator) {
        final E[] b = arrayGenerator.apply(length);
        for (int i = 0; i < length; i++) b[i] = at(i);
        return b;
    }

    public Set<E> toSet() {
        final Set<E> set = new HashSet<>();
        for (int i = 0; i < length; i++) set.add(at(i));
        return set;
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
        if (obj instanceof Arr that && that.length == this.length) {
            for (int i = 0; i < length; i++) if (this.at(i) != that.at(i)) return false;
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return new It();
    }

    private class It implements Iterator<E> {
        private int i = 0;

        @Override
        public boolean hasNext() {
            return i < Arr.this.length;
        }

        @Override
        public E next() {
            return Arr.this.at(i++);
        }
    }

    @FunctionalInterface
    public interface ContextFunction<E, R> {
        R apply(E e, int i, boolean first, boolean last);
    }
}
