package com.github.eyrekr.mutable;

import com.github.eyrekr.annotation.ReturnsNewInstance;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.math.Algebra;

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

    private Arr<E> makeSureThereIsEnoughCapacity() {
        if (length < a.length) return this;
        final int n = a.length << 1;
        final Object[] b = new Object[n];
        for (int i = 0; i < length; i++) b[i] = at(i);
        start = 0;
        a = b;
        return this;
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

    public E removeLast() {
        if (length == 0) return null;
        final E value = at(-1);
        length--;
        return value;
    }

    public Arr<E> addFirst(final E value) {
        makeSureThereIsEnoughCapacity();
        start = (a.length + start - 1) % a.length;
        a[start] = value;
        length++;
        return this;
    }

    public E removeFirst() {
        final E value = at(0);
        start++;
        length--;
        return value;
    }

    public E at(final int i) {
        if (length == 0) return null;
        return (i >= 0)
                ? (E) a[(start + i % length) % a.length]
                : (E) a[(a.length + start + length + i % length) % a.length];
    }

    public Arr<E> set(final int i, final E value) {
        if (length == 0) throw new IndexOutOfBoundsException("Empty");
        if (i >= 0) a[(start + i % length) % a.length] = value;
        else a[(a.length + start + length + i % length) % a.length] = value;
        return this;
    }

    public Arr<E> swap(final int i, final int j) {
        final E a = at(i), b = at(j);
        return set(i, b).set(j, a);
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

    public E reduce(final BiFunction<? super E, ? super E, ? extends E> reducer) {
        E acc = at(0);
        for (int i = 1; i < length; i++) acc = reducer.apply(acc, at(i));
        return acc;
    }

    public E min(final Comparator<? super E> comparator) {
        E min = null;
        for (int i = 0; i < length; i++)
            if (min == null || comparator.compare(at(i), min) < 0) min = at(i);
        return min;
    }

    public <T extends Comparable<T>> E min(final Function<? super E, T> transform) {
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

    public E max(final Comparator<? super E> comparator) {
        E max = null;
        for (int i = 0; i < length; i++)
            if (max == null || comparator.compare(at(i), max) > 0) max = at(i);
        return max;
    }

    public <T extends Comparable<T>> E max(final Function<? super E, T> transform) {
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

        return this;
    }

    public Arr<E> reversed() {
        for (int i = 0; i < length / 2; i++) swap(i, length - i); // FIXME Reverse in O(1)
        return this;
    }

    public <T extends Comparable<T>> Arr<E> sortedBy(final Function<? super E, T> transform) {
        delegate = delegate.sortedBy(transform);
        return this;
    }

    public Arr<E> sortedBy(final Comparator<? super E> comparator) {
        delegate = delegate.sortedBy(comparator);
        return this;
    }

    public Arr<E> unique() {
        delegate = delegate.unique();
        return this;
    }

    public Arr<E> first(final int n) {
        delegate = delegate.first(n);
        return this;
    }

    public Arr<E> last(final int n) {
        delegate = delegate.last(n);
        return this;
    }

    public Arr<E> skip(final int n) {
        delegate = delegate.skip(n);
        return this;
    }

    public Arr<E> takeWhile(final Predicate<? super E> predicate) {
        delegate = delegate.takeWhile(predicate);
        return this;
    }

    public Arr<E> skipWhile(final Predicate<? super E> predicate) {
        delegate = delegate.skipWhile(predicate);
        return this;
    }

    @ReturnsNewInstance
    public <R> Arr<R> map(final Function<? super E, ? extends R> transform) {
        return new Arr<>(delegate.map(transform));
    }

    @ReturnsNewInstance
    public <F, R> Arr<R> mapWith(final Arr<F> other, final BiFunction<? super E, ? super F, ? extends R> transform) {
        return new Arr<>(delegate.mapWith(other.delegate, transform));
    }

    @ReturnsNewInstance
    public <F, R> Arr<R> prodWith(final Arr<F> other, final BiFunction<? super E, ? super F, ? extends R> transform) {
        return new Arr<>(delegate.prodWith(other.delegate, transform));
    }

    @ReturnsNewInstance
    public <F, R> Arr<R> prodUpperTriangleWith(final Arr<F> other, final BiFunction<? super E, ? super F, ? extends R> transform) {
        return new Arr<>(delegate.prodUpperTriangleWith(other.delegate, transform));
    }

    @ReturnsNewInstance
    public <R> Arr<R> flatMap(final Function<? super E, Arr<? extends R>> transform) {
        return new Arr<>(delegate.flatMap(value -> transform.apply(value).delegate));
    }

    public Arr<E> each(final Consumer<? super E> consumer) {
        delegate.each(consumer);
        return this;
    }

    public Arr<E> print() {
        delegate.print(" ");
        return this;
    }

    public Arr<E> print(final String separator) {
        delegate.print(separator);
        return this;
    }

    public Arr<E> print(final String separator,
                        final com.github.eyrekr.immutable.Arr.ContextFunction<? super E, String> formatter) {
        delegate.print(separator, formatter);
        return this;
    }

    public Longs toLongs(final Function<? super E, ? extends Number> transform) {
        return delegate.toLongs(transform);
    }

    public E[] toArray(final IntFunction<E[]> arrayGenerator) {
        return delegate.toArray(arrayGenerator);
    }

    public Set<E> toSet() {
        return delegate.toSet();
    }

    public List<E> toList() {
        return delegate.toList();
    }

    public Seq<E> toSeq() {
        return delegate.toSeq();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof final Arr that && Objects.equals(this.delegate, that.delegate);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return delegate.iterator();
    }

}
