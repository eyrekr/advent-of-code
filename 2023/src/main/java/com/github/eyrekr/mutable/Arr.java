package com.github.eyrekr.mutable;

import com.github.eyrekr.annotation.ReturnsNewInstance;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.immutable.Seq;

import java.util.*;
import java.util.function.*;

/**
 * Auto-expandable mutable array of elements.
 * Implemented as a wrapper over {@link com.github.eyrekr.immutable.Arr}
 */
public final class Arr<E> implements Iterable<E> {

    private com.github.eyrekr.immutable.Arr<E> delegate;

    private Arr(final com.github.eyrekr.immutable.Arr delegate) {
        this.delegate = delegate;
    }

    public int length() {
        return delegate.length;
    }

    public static <T> Arr<T> empty() {
        return new Arr<>(com.github.eyrekr.immutable.Arr.empty());
    }

    public static <T> Arr<T> of(final T value, final T... values) {
        return new Arr<>(com.github.eyrekr.immutable.Arr.of(value, values));
    }

    public boolean isEmpty() {
        return delegate.isEmpty;
    }

    public boolean isNotEmpty() {
        return delegate.isNotEmpty;
    }

    public E getFirst() {
        return delegate.at(0);
    }

    public E getLast() {
        return delegate.at(-1);
    }

    public Arr<E> addLast(final E value) {
        delegate = delegate.addLast(value);
        return this;
    }

    public Arr<E> addLast(final Arr<? extends E> arr) {
        delegate = delegate.addLast(arr.delegate);
        return this;
    }

    public E removeLast() {
        final E value = delegate.at(-1);
        delegate = delegate.removeLast();
        return value;
    }

    public Arr<E> addFirst(final E value) {
        delegate = delegate.addFirst(value);
        return this;
    }

    public E removeFirst() {
        final E value = delegate.at(0);
        delegate = delegate.removeFirst();
        return value;
    }

    public E at(final int i) {
        return delegate.at(i);
    }

    public Arr<E> set(final int i, final E value) {
        delegate.set(i, value);
        return this;
    }

    public boolean has(final E value) {
        return delegate.has(value);
    }

    public boolean atLeastOneIs(final Predicate<? super E> predicate) {
        return delegate.atLeastOneIs(predicate);
    }

    public boolean atLeastOneIsNot(final Predicate<? super E> predicate) {
        return atLeastOneIs(predicate.negate());
    }

    public boolean allMatch(final E value) {
        return delegate.allMatch(value);
    }

    public boolean allAre(final Predicate<? super E> predicate) {
        return delegate.allAre(predicate);
    }

    public boolean noneMatch(final E value) {
        return delegate.noneMatch(value);
    }

    public boolean noneIs(final Predicate<? super E> predicate) {
        return delegate.noneIs(predicate);
    }

    public <R> R reduce(final R init, final BiFunction<? super R, ? super E, ? extends R> reducer) {
        return delegate.reduce(init, reducer);
    }

    public E reduce(final BiFunction<? super E, ? super E, ? extends E> reducer) {
        return delegate.reduce(reducer);
    }

    public E min(final Comparator<? super E> comparator) {
        return delegate.min(comparator);
    }

    public int argmin(final Comparator<? super E> comparator) {
        return delegate.argmin(comparator);
    }

    public E max(final Comparator<? super E> comparator) {
        return delegate.max(comparator);
    }

    public int argmax(final Comparator<? super E> comparator) {
        return delegate.argmax(comparator);
    }

    public Arr<E> where(final Predicate<? super E> predicate) {
        delegate = delegate.where(predicate);
        return this;
    }

    public Arr<E> reversed() {
        delegate = delegate.reversed();
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
