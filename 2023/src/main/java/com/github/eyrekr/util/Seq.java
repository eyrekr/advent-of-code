package com.github.eyrekr.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Seq<E> implements Iterable<E> {
    public final E value;
    public final int length;
    public final Seq<E> tail;
    public final boolean isEmpty;

    private Seq(final E value, final Seq<E> tail) {
        this.value = value;
        this.length = (tail == null) ? 0 : tail.length + 1;
        this.tail = tail;
        this.isEmpty = (tail == null);
    }

    public static <T> Seq<T> empty() {
        return new Seq<>(null, null);
    }

    @SafeVarargs
    public static <T> Seq<T> of(final T first, final T... rest) {
        return rest != null ? fromArray(rest).add(first) : new Seq<>(first, empty());
    }

    public static <T> Seq<T> fromArray(final T[] array) {
        return array == null ? empty() : range(0, array.length).carryMap(array, (arr, i) -> arr[i.intValue()]);
    }

    public static <T> Seq<T> fromIterable(final Iterable<T> collection) {
        return fromIterator(collection.iterator());
    }

    public static <T> Seq<T> fromIterator(final Iterator<T> iterator) {
        return iterator.hasNext() ? new Seq<>(iterator.next(), fromIterator(iterator)) : empty();
    }

    public static Seq<Long> range(final long startInclusive, final long endExclusive) {
        return startInclusive >= endExclusive ? empty() : new Seq<>(startInclusive, range(startInclusive + 1, endExclusive));
    }

    public Seq<E> add(final E value) {
        return new Seq<>(value, this);
    }

    public Seq<E> addSeq(final Seq<E> seq) {
        return seq.reduceR(this, (acc, element) -> new Seq<>(element, acc));
    }

    public E at(final int i) {
        return (i % length == 0) ? value : i > 0 ? tail.at((i % length) - 1) : tail.at(i % length);
    }

    public boolean has(final E value) {
        return !isEmpty && (this.value == value || tail.has(value));
    }

    public <R> Seq<R> map(final Function<? super E, R> translate) {
        return isEmpty ? empty() : new Seq<>(translate.apply(value), tail.map(translate));
    }

    public <C, R> Seq<R> carryMap(final C carry, final BiFunction<? super C, ? super E, R> translate) {
        return isEmpty ? empty() : new Seq<>(translate.apply(carry, value), tail.carryMap(carry, translate));
    }

    public <F, R> Seq<R> mapWith(final Seq<F> seq, final BiFunction<? super E, ? super F, R> translate) {
        return isEmpty || seq.isEmpty ? empty() : new Seq<>(translate.apply(value, seq.value), tail.mapWith(seq.tail, translate));
    }

    public <R> Seq<R> flatMap(final Function<? super E, Seq<R>> translate) {
        return reduceR(empty(), (acc, element) -> acc.addSeq(translate.apply(element)));
    }

    public <R> Seq<R> mapWithNext(final BiFunction<? super E, ? super E, R> translate) {
        return isEmpty ? empty() : new Seq<>(translate.apply(value, tail.value), tail.mapWithNext(translate));
    }

    public <R> Seq<R> mapWithPrev(final BiFunction<? super E, ? super E, R> translate) {
        return mapWith(add(null), translate);
    }

    public <R> R reduceR(final R init, final BiFunction<? super R, ? super E, ? extends R> combine) { // left-to-right
        return isEmpty ? init : combine.apply(tail.reduceR(init, combine), value);
    }

    public E reduceR(final BiFunction<? super E, ? super E, ? extends E> combine) {
        return isEmpty ? null : tail.reduceR(value, combine);
    }

    public <R> R reduce(final R init, final BiFunction<? super R, ? super E, ? extends R> combine) { // right-to-left
        return isEmpty ? init : tail.reduce(combine.apply(init, value), combine);
    }

    public E reduce(final BiFunction<? super E, ? super E, ? extends E> combine) {
        return isEmpty ? null : tail.reduce(value, combine);
    }

    public Seq<E> where(final Predicate<? super E> predicate) {
        return isEmpty ? this : predicate.test(value) ? new Seq<>(value, tail.where(predicate)) : tail.where(predicate);
    }

    public Seq<E> reverse() {
        return reduce(empty(), (acc, element) -> new Seq<>(element, acc));
    }

    public Seq<E> sortedBy(final Comparator<? super E> comparator) {
        if (length <= 1) {
            return this;
        } else if (length == 2) {
            return comparator.compare(value, tail.value) <= 0 ? this : Seq.of(tail.value, value);
        }
        final Seq<E> lower = tail.where(element -> comparator.compare(value, element) <= 0).sortedBy(comparator);
        final Seq<E> upper = tail.where(element -> comparator.compare(value, element) > 0).sortedBy(comparator);
        return lower.add(value).addSeq(upper);
    }

    public Seq<E> sorted() {
        return sortedBy(Comparator.comparing((Object a) -> ((Comparable) a)));
    }

    public E min(final Comparator<? super E> comparator) {
        return reduce((a, b) -> comparator.compare(a, b) < 0 ? a : b);
    }

    public E min() {
        return (E) min(Comparator.comparing((Object a) -> ((Comparable) a)));
    }

    public E max(final Comparator<? super E> comparator) {
        return reduce((a, b) -> comparator.compare(a, b) > 0 ? a : b);
    }

    public E max() {
        return (E) max(Comparator.comparing((Object a) -> ((Comparable) a)));
    }

    public Seq<Seq<E>> split(final Predicate<? super E> predicate) {
        return reduceR(
                Seq.of(empty(), empty()),
                (acc, element) -> predicate.test(element)
                        ? Seq.of(acc.value, acc.tail.value.add(element))
                        : Seq.of(acc.value.add(element), acc.tail.value));
    }

    public Seq<E> first(final int n) {
        return isEmpty ? this : n == 0 ? empty() : new Seq<>(value, tail.first(n - 1));
    }

    public Seq<E> last(final int n) {
        return length <= n ? this : tail.last(n);
    }

    public Seq<E> skip(final int n) {
        return isEmpty || n <= 0 ? this : tail.skip(n - 1);
    }

    public Seq<E> takeWhile(final Predicate<? super E> predicate) {
        return isEmpty ? this : predicate.test(value) ? new Seq<>(value, tail.takeWhile(predicate)) : empty();
    }

    public Seq<E> skipWhile(final Predicate<? super E> predicate) {
        return isEmpty ? this : predicate.test(value) ? tail.skipWhile(predicate) : this;
    }

    public Seq<E> each(final Consumer<? super E> consumer) {
        consumer.accept(value);
        if (!isEmpty) tail.forEach(consumer);
        return this;
    }

    public Seq<Seq<E>> batch(final int size) {
        record Accumulator<E>(Seq<Seq<E>> all, Seq<E> batch) {
        }
        final Accumulator<E> acc = reduceR(
                new Accumulator<>(empty(), empty()),
                (accumulator, element) -> accumulator.batch.length < size
                        ? new Accumulator<>(accumulator.all, accumulator.batch.add(element))
                        : new Accumulator<>(accumulator.all.add(accumulator.batch), Seq.of(element)));
        return acc.batch.isEmpty ? acc.all : acc.all.add(acc.batch);
    }

    public Seq<E> print() {
        System.out.println(this);
        return this;
    }

    public Seq<E> print(final String separator) {
        System.out.println("[" + reduceR("", (sum, element) -> sum.isBlank() ? element + "" : element + separator + sum) + "]");
        return this;
    }

    @Override
    public String toString() {
        return "[" + reduceR("", (sum, element) -> sum.isBlank() ? element + "" : element + " " + sum) + "]";
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private Seq<E> seq = Seq.this;

            @Override
            public boolean hasNext() {
                return !seq.isEmpty;
            }

            @Override
            public E next() {
                final E value = seq.value;
                seq = seq.tail;
                return value;
            }
        };
    }

    public static void main(String[] args) {
        Seq.range(1, 10).print();
        System.out.println(Seq.of(10, 20, -5, 7).max(Integer::compare));
        Seq.of(10, 7, 20, -5, 7, 0).sortedBy(Integer::compare).print();
        Seq.fromIterable(List.of("x", "y", "z")).print();
        Seq.of("a", "b", "c", "d", "e").mapWithPrev((value, prev) -> prev + "=>" + value).print();
        Seq.of("a", "b", "c", "d", "e").mapWithNext((value, next) -> value + "=>" + next).print();
    }
}