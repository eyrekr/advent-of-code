package com.github.eyrekr.mutable;

import com.github.eyrekr.annotation.ReturnsNewInstance;
import com.github.eyrekr.common.Indexed;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.immutable.Opt;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.math.Algebra;
import com.github.eyrekr.output.Out;

import java.util.*;
import java.util.function.*;

/**
 * Auto-expandable mutable array of elements.
 * Not thread-safe!
 */
public final class Arr<E> implements Iterable<E> {

    private static final int MIN_CAPACITY = 16;

    private int length;
    private Object[] a;
    private int start;

    private Arr(final int capacity) {
        final int n = 1 << Algebra.log2(capacity);
        this.a = new Object[Math.max(MIN_CAPACITY, n)];
        this.length = 0;
        this.start = 0;
    }

    private void makeSureThereIsEnoughCapacity() {
        if (length < a.length) return;
        final int n = a.length << 1;
        final Object[] b = new Object[n];
        for (int i = 0; i < length; i++) b[i] = at(i);
        start = 0;
        a = b;
    }

    public static <T> Arr<T> empty() {
        return new Arr<>(MIN_CAPACITY);
    }

    public static <T> Arr<T> of(final T value, final T... values) {
        final Arr<T> arr = new Arr<>(values != null ? values.length + 1 : 1);
        arr.addLast(value);
        if (values != null) for (final T t : values) arr.addLast(t);
        return arr;
    }

    public static <T> Arr<T> repeat(final T value, final int n) {
        final Arr<T> arr = new Arr<>(n);
        for (int i = 0; i < n; i++) arr.addLast(value);
        return arr;
    }

    public int length() {
        return length;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public boolean isNotEmpty() {
        return length != 0;
    }

    public Arr<E> copy() {
        final Arr<E> arr = new Arr<>(a.length);
        for (int i = 0; i < length; i++) arr.addLast(at(i));
        return arr;
    }

    public E getFirst() {
        return at(0);
    }

    public E getLast() {
        return at(-1);
    }

    public Arr<E> addLast(final E value) {
        makeSureThereIsEnoughCapacity();
        final int i = (start + length) % a.length;
        a[i] = value;
        length++;
        return this;
    }

    public Arr<E> addAllLast(final Arr<? extends E> values) {
        values.each(this::addLast);
        return this;
    }

    public E removeLast() {
        if (length == 0) return null;
        final E value = at(-1);
        length--;
        return value;
    }

    public Arr<E> removeLast(final int n) {
        final Arr<E> tail = Arr.empty();
        for (int i = 0; i < n; i++) tail.addFirst(removeLast());
        return tail;
    }

    public Arr<E> addFirst(final E value) {
        makeSureThereIsEnoughCapacity();
        start = (a.length + start - 1) % a.length;
        a[start] = value;
        length++;
        return this;
    }

    public Arr<E> addAllFirst(final Arr<? extends E> values) {
        values.eachReverse(this::addFirst);
        return this;
    }

    public E removeFirst() {
        final E value = at(0);
        start++;
        length--;
        return value;
    }

    public Arr<E> removeFirst(final int n) {
        final Arr<E> head = Arr.empty();
        for (int i = 0; i < n; i++) head.addLast(removeFirst());
        return head;
    }

    public E at(final int i) {
        if (length == 0) throw new IndexOutOfBoundsException();
        return (i >= 0)
                ? (E) a[(start + i % length) % a.length]
                : (E) a[(a.length + start + length + i % length) % a.length];
    }

    public E at(final Indexed indexed) {
        return at(indexed.i());
    }

    public Arr<E> set(final int i, final E value) {
        if (length == 0) throw new IndexOutOfBoundsException("Empty");
        if (i >= 0) a[(start + i % length) % a.length] = value;
        else a[(a.length + start + length + i % length) % a.length] = value;
        return this;
    }

    public Arr<E> set(final Indexed indexed, final E value) {
        return set(indexed.i(), value);
    }

    public Arr<E> swap(final int i, final int j) {
        if (i == j) return this;
        final E a = at(i), b = at(j);
        return set(i, b).set(j, a);
    }

    public Arr<E> swap(final Indexed a, final Indexed b) {
        return swap(a.i(), b.i());
    }

    public boolean has(final E value) {
        for (int i = 0; i < length; i++)
            if (Objects.equals(at(i), value)) return true;
        return false;
    }

    public int indexOf(final E value) {
        for (int i = 0; i < length; i++)
            if (Objects.equals(at(i), value)) return i;
        return -1;
    }

    public Opt<E> findFirst(final E value) {
        for (int i = 0; i < length; i++)
            if (Objects.equals(at(i), value)) return Opt.of(value);
        return Opt.empty();
    }

    public Opt<E> findFirst(final Predicate<? super E> predicate) {
        for (int i = 0; i < length; i++)
            if (predicate.test(at(i))) return Opt.of(at(i));
        return Opt.empty();
    }

    public Opt<E> findLast(final E value) {
        for (int i = length - 1; i >= 0; i--)
            if (Objects.equals(at(i), value)) return Opt.of(value);
        return Opt.empty();
    }

    public Opt<E> findLast(final Predicate<? super E> predicate) {
        for (int i = length - 1; i >= 0; i--)
            if (predicate.test(at(i))) return Opt.of(at(i));
        return Opt.empty();
    }

    public Arr<E> replaceAll(final E value, final E replacement) {
        for (int i = length - 1; i >= 0; i--)
            if (Objects.equals(at(i), value)) set(i, replacement);
        return this;
    }

    public boolean atLeastOneIs(final Predicate<? super E> predicate) {
        for (int i = 0; i < length; i++)
            if (predicate.test(at(i))) return true;
        return false;
    }

    public boolean atLeastOneIsNot(final Predicate<? super E> predicate) {
        for (int i = 0; i < length; i++)
            if (!predicate.test(at(i))) return true;
        return false;
    }

    public boolean allMatch(final E value) {
        for (int i = 0; i < length; i++)
            if (!Objects.equals(at(i), value)) return false;
        return true;
    }

    public boolean allAre(final Predicate<? super E> predicate) {
        for (int i = 0; i < length; i++)
            if (!predicate.test(at(i))) return false;
        return true;
    }

    public boolean noneMatch(final E value) {
        for (int i = 0; i < length; i++)
            if (Objects.equals(at(i), value)) return false;
        return true;
    }

    public boolean noneIs(final Predicate<? super E> predicate) {
        for (int i = 0; i < length; i++)
            if (predicate.test(at(i))) return false;
        return true;
    }

    public <R> R reduce(final R init, final BiFunction<? super R, ? super E, ? extends R> reducer) {
        R acc = init;
        for (int i = 0; i < length; i++) acc = reducer.apply(acc, at(i));
        return acc;
    }

    public Opt<E> reduce(final BiFunction<? super E, ? super E, ? extends E> reducer) {
        if (isEmpty()) return Opt.empty();
        E acc = at(0);
        for (int i = 1; i < length; i++) acc = reducer.apply(acc, at(i));
        return Opt.of(acc);
    }

    public Opt<E> min(final Comparator<? super E> comparator) {
        if (isEmpty()) return Opt.empty();
        E min = null;
        for (int i = 0; i < length; i++)
            if (min == null || comparator.compare(at(i), min) < 0) min = at(i);
        return Opt.of(min);
    }

    public <T extends Comparable<T>> Opt<E> min(final Function<? super E, T> transform) {
        return min(Comparator.comparing(transform));
    }

    public int argmin(final Comparator<? super E> comparator) {
        int index = -1;
        for (int i = 0; i < length; i++)
            if (index < 0 || comparator.compare(at(i), at(index)) < 0) index = i;
        return index;
    }

    public <T extends Comparable<T>> int argmin(final Function<? super E, T> transform) {
        return argmin(Comparator.comparing(transform));
    }

    public Opt<E> max(final Comparator<? super E> comparator) {
        if (isEmpty()) return Opt.empty();
        E max = null;
        for (int i = 0; i < length; i++)
            if (max == null || comparator.compare(at(i), max) > 0) max = at(i);
        return Opt.of(max);
    }

    public <T extends Comparable<T>> Opt<E> max(final Function<? super E, T> transform) {
        return min(Comparator.comparing(transform));
    }

    public int argmax(final Comparator<? super E> comparator) {
        int index = -1;
        for (int i = 0; i < length; i++)
            if (index < 0 || comparator.compare(at(i), at(index)) > 0) index = i;
        return index;
    }

    public <T extends Comparable<T>> int argmax(final Function<? super E, T> transform) {
        return argmin(Comparator.comparing(transform));
    }

    public Arr<E> where(final Predicate<? super E> predicate) {
        int n = 0;
        for (int i = 0; i < length; i++)
            if (predicate.test(at(i))) swap(i, n++);
        length = n;
        return this;
    }

    public Arr<E> reversed() {
        for (int i = 0; i < length / 2; i++) swap(i, length - i); // FIXME Implement reverse in O(1)
        return this;
    }

    public <T extends Comparable<T>> Arr<E> sortedBy(final Function<? super E, T> transform) {
        return sortedBy(Comparator.comparing(transform));
    }

    public Arr<E> sortedBy(final Comparator<? super E> comparator) {
        quicksort(0, length - 1, comparator);
        return this;
    }

    private void quicksort(final int begin, final int end, final Comparator<? super E> comparator) {
        final E pivot = at(begin + end / 2);
        int l = begin, r = end;
        do {
            while (comparator.compare(at(l), pivot) < 0 && l < end) l++;
            while (comparator.compare(at(r), pivot) > 0 && r > begin) r--;
            if (l <= r) swap(l++, r--);
        } while (l < r);
        if (r > begin) quicksort(begin, r, comparator);
        if (l < end) quicksort(l, end, comparator);
    }

    public Arr<E> unique() {
        final Set<E> visited = new HashSet<>();
        int n = 0;
        for (int i = 0; i < length; i++) {
            final E value = at(i);
            if (!visited.contains(value)) {
                swap(i, n++);
                visited.add(value);
            }
        }
        length = n;
        return this;
    }

    public Arr<E> first(final int n) {
        length = Math.min(n, length);
        return this;
    }

    public Arr<E> last(final int n) {
        if (n >= length) return this;
        start = (start + length - n) % a.length;
        length = n;
        return this;
    }

    public Arr<E> skip(final int n) {
        if (n < length) start = (start + n) % a.length;
        length = Math.max(0, length - n);
        return this;
    }

    public Arr<E> takeWhile(final Predicate<? super E> predicate) {
        int n = 0;
        while (n < length && predicate.test(at(n))) n++;
        return first(n);
    }

    public Arr<E> skipWhile(final Predicate<? super E> predicate) {
        int n = 0;
        while (n < length && predicate.test(at(n))) n++;
        return skip(n);
    }

    @ReturnsNewInstance
    public <R> Arr<R> map(final Function<? super E, ? extends R> transform) {
        final Arr<R> arr = new Arr<>(length);
        for (int i = 0; i < length; i++) arr.addLast(transform.apply(at(i)));
        return arr;
    }

    @ReturnsNewInstance
    public <F, R> Arr<R> mapWith(final Arr<F> other, final BiFunction<? super E, ? super F, ? extends R> transform) {
        final Arr<R> arr = new Arr<>(length);
        for (int i = 0; i < length; i++) arr.addLast(transform.apply(at(i), other.at(i)));
        return arr;
    }

    @ReturnsNewInstance
    public <F, R> Arr<R> prodWith(final Arr<F> other, final BiFunction<? super E, ? super F, ? extends R> transform) {
        final Arr<R> arr = new Arr<>(length * other.length);
        for (int i = 0; i < length; i++)
            for (int j = 0; j < other.length; j++)
                arr.addLast(transform.apply(at(i), other.at(j)));
        return arr;
    }

    @ReturnsNewInstance
    public <F, R> Arr<R> prodUpperTriangleWith(final Arr<F> other, final BiFunction<? super E, ? super F, ? extends R> transform) {
        final Arr<R> arr = new Arr<>(length * other.length / 2);
        for (int i = 0; i < length; i++)
            for (int j = i + 1; j < other.length; j++)
                arr.addLast(transform.apply(at(i), other.at(j)));
        return arr;
    }

    @ReturnsNewInstance
    public <R> Arr<R> flatMap(final Function<? super E, Arr<? extends R>> transform) {
        final Arr<R> arr = new Arr<>(length);
        for (int i = 0; i < length; i++) {
            final Arr<? extends R> sub = transform.apply(at(i));
            for (int j = 0; j < sub.length; j++) arr.addLast(sub.at(j));
        }
        return arr;
    }

    public Arr<E> each(final Consumer<? super E> consumer) {
        for (int i = 0; i < length; i++) consumer.accept(at(i));
        return this;
    }

    public Arr<E> transform(final Function<? super E, ? extends E> transform) {
        for (int i = 0; i < length; i++) set(i, transform.apply(at(i)));
        return this;
    }

    public Arr<E> eachReverse(final Consumer<? super E> consumer) {
        for (int i = length - 1; i >= 0; i--) consumer.accept(at(i));
        return this;
    }

    public Arr<E> transformReverse(final Function<? super E, ? extends E> transform) {
        for (int i = length - 1; i >= 0; i--) set(i, transform.apply(at(i)));
        return this;
    }

    public Arr<E> print() {
        print(" ");
        return this;
    }

    public Arr<E> print(final String separator) {
        return print(separator, (e, i, first, last) -> String.valueOf(e));
    }

    public Arr<E> print(final String separator, final ContextFunction<? super E, String> formatter) {
        for (int i = 0; i < length; i++) {
            final boolean last = i == length - 1;
            Out.print("%s%s", formatter.apply(at(i), i, i == 0, last), last ? "\n" : separator);
        }
        return this;
    }

    public Longs toLongs(final Function<? super E, ? extends Number> transform) {
        Longs arr = Longs.empty();
        for (int i = 0; i < length; i++) arr = arr.addLast(transform.apply(at(i)).longValue());
        return arr;
    }

    public E[] toArray(final IntFunction<E[]> arrayGenerator) {
        final E[] array = arrayGenerator.apply(length);
        for (int i = 0; i < length; i++) array[i] = at(i);
        return array;
    }

    public Set<E> toSet() {
        final Set<E> set = new HashSet<E>(length);
        for (int i = 0; i < length; i++) set.add(at(i));
        return set;
    }

    public List<E> toList() {
        final List<E> list = new ArrayList<E>(length);
        for (int i = 0; i < length; i++) list.add(at(i));
        return list;
    }

    public Seq<E> toSeq() {
        Seq<E> seq = Seq.empty();
        for (int i = length - 1; i >= 0; i--) seq = seq.addFirst(at(i));
        return seq;
    }

    public Map<E, Long> frequencies() {
        final Map<E, Long> frequencies = new HashMap<>();
        for (int i = 0; i < length; i++) {
            final E value = at(i);
            final long count = frequencies.getOrDefault(value, 0L) + 1;
            frequencies.put(value, count);
        }
        return frequencies;
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
        if (obj instanceof final Arr that && this.length == that.length) {
            for (int i = 0; i < length; i++) if (!Objects.equals(this.at(i), that.at(i))) return false;
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
            return i < length;
        }

        @Override
        public E next() {
            return at(i++);
        }
    }

    @FunctionalInterface
    public interface ContextFunction<E, R> {
        R apply(E e, int i, boolean first, boolean last);
    }
}
