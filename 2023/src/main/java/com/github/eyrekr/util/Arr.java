package com.github.eyrekr.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Arr<E> /*implements Iterable<E> */{
//    public final E value;
//    public final int length;
//    public final boolean isEmpty;
//
//    private Arr(final E value, final Arr<E> tail) {
//        this.value = value;
//        this.length = (tail == null) ? 0 : tail.length + 1;
//        this.tail = tail;
//        this.isEmpty = (tail == null);
//    }
//
//    public static <T> Arr<T> empty() {
//        return new Arr<>(null, null);
//    }
//
//    @SafeVarargs
//    public static <T> Arr<T> of(final T first, final T... rest) {
//        return rest != null ? fromArray(rest).add(first) : new Arr<>(first, empty());
//    }
//
//    public static <T> Arr<T> fromArray(final T[] array) {
//        return array == null ? empty() : range(0, array.length).carryMap(array, (arr, i) -> arr[i.intValue()]);
//    }
//
//    public static <T> Arr<T> fromIterable(final Iterable<T> collection) {
//        return fromIterator(collection.iterator());
//    }
//
//    public static <T> Arr<T> fromIterator(final Iterator<T> iterator) {
//        return iterator.hasNext() ? new Arr<>(iterator.next(), fromIterator(iterator)) : empty();
//    }
//
//    public static Arr<Long> range(final long startInclusive, final long endExclusive) {
//        return startInclusive >= endExclusive ? empty() : new Arr<>(startInclusive, range(startInclusive + 1, endExclusive));
//    }
//
//    public Arr<E> prepend(final E value) {
//        return new Arr<>(value, this);
//    }
//
//    public Arr<E> addSeq(final Arr<E> seq) {
//        return seq.reduceR(this, (acc, element) -> new Arr<>(element, acc));
//    }
//
//    public E at(final int i) {
//        return (i % length == 0) ? value : i > 0 ? tail.at((i % length) - 1) : tail.at(i % length);
//    }
//
//    public boolean has(final E value) {
//        return !isEmpty && (this.value == value || tail.has(value));
//    }
//
//    public <R> Arr<R> map(final Function<? super E, R> translate) {
//        return isEmpty ? empty() : new Arr<>(translate.apply(value), tail.map(translate));
//    }
//
//    public <C, R> Arr<R> carryMap(final C carry, final BiFunction<? super C, ? super E, R> translate) {
//        return isEmpty ? empty() : new Arr<>(translate.apply(carry, value), tail.carryMap(carry, translate));
//    }
//
//    public <F, R> Arr<R> mapWith(final Arr<F> seq, final BiFunction<? super E, ? super F, R> translate) {
//        return isEmpty || seq.isEmpty ? empty() : new Arr<>(translate.apply(value, seq.value), tail.mapWith(seq.tail, translate));
//    }
//
//    public <R> Arr<R> flatMap(final Function<? super E, Arr<R>> translate) {
//        return reduceR(empty(), (acc, element) -> acc.addSeq(translate.apply(element)));
//    }
//
//    public <R> Arr<R> mapWithNext(final BiFunction<? super E, ? super E, R> translate) {
//        return isEmpty ? empty() : new Arr<>(translate.apply(value, tail.value), tail.mapWithNext(translate));
//    }
//
//    public <R> Arr<R> mapWithPrev(final BiFunction<? super E, ? super E, R> translate) {
//        return mapWith(add(null), translate);
//    }
//
//    public <R> R reduceR(final R init, final BiFunction<? super R, ? super E, ? extends R> combine) { // left-to-right
//        return isEmpty ? init : combine.apply(tail.reduceR(init, combine), value);
//    }
//
//    public E reduceR(final BiFunction<? super E, ? super E, ? extends E> combine) {
//        return isEmpty ? null : tail.reduceR(value, combine);
//    }
//
//    public <R> R reduce(final R init, final BiFunction<? super R, ? super E, ? extends R> combine) { // right-to-left
//        return isEmpty ? init : tail.reduce(combine.apply(init, value), combine);
//    }
//
//    public E reduce(final BiFunction<? super E, ? super E, ? extends E> combine) {
//        return isEmpty ? null : tail.reduce(value, combine);
//    }
//
//    public Arr<E> where(final Predicate<? super E> predicate) {
//        return isEmpty ? this : predicate.test(value) ? new Arr<>(value, tail.where(predicate)) : tail.where(predicate);
//    }
//
//    public Arr<E> reverse() {
//        return reduce(empty(), (acc, element) -> new Arr<>(element, acc));
//    }
//
//    public Arr<E> sortedBy(final Comparator<? super E> comparator) {
//        if (length <= 1) {
//            return this;
//        } else if (length == 2) {
//            return comparator.compare(value, tail.value) <= 0 ? this : Arr.of(tail.value, value);
//        }
//        final Arr<E> lower = tail.where(element -> comparator.compare(value, element) <= 0).sortedBy(comparator);
//        final Arr<E> upper = tail.where(element -> comparator.compare(value, element) > 0).sortedBy(comparator);
//        return lower.add(value).addSeq(upper);
//    }
//
//    public Arr<E> sorted() {
//        return sortedBy(Comparator.comparing((Object a) -> ((Comparable) a)));
//    }
//
//    public E min(final Comparator<? super E> comparator) {
//        return reduce((a, b) -> comparator.compare(a, b) < 0 ? a : b);
//    }
//
//    public E min() {
//        return (E) min(Comparator.comparing((Object a) -> ((Comparable) a)));
//    }
//
//    public E max(final Comparator<? super E> comparator) {
//        return reduce((a, b) -> comparator.compare(a, b) > 0 ? a : b);
//    }
//
//    public E max() {
//        return (E) max(Comparator.comparing((Object a) -> ((Comparable) a)));
//    }
//
//    public Arr<Arr<E>> split(final Predicate<? super E> predicate) {
//        return reduceR(
//                Arr.of(empty(), empty()),
//                (acc, element) -> predicate.test(element)
//                        ? Arr.of(acc.value, acc.tail.value.add(element))
//                        : Arr.of(acc.value.add(element), acc.tail.value));
//    }
//
//    public Arr<E> first(final int n) {
//        return isEmpty ? this : n == 0 ? empty() : new Arr<>(value, tail.first(n - 1));
//    }
//
//    public Arr<E> last(final int n) {
//        return length <= n ? this : tail.last(n);
//    }
//
//    public Arr<E> skip(final int n) {
//        return isEmpty || n <= 0 ? this : tail.skip(n - 1);
//    }
//
//    public Arr<E> takeWhile(final Predicate<? super E> predicate) {
//        return isEmpty ? this : predicate.test(value) ? new Arr<>(value, tail.takeWhile(predicate)) : empty();
//    }
//
//    public Arr<E> skipWhile(final Predicate<? super E> predicate) {
//        return isEmpty ? this : predicate.test(value) ? tail.skipWhile(predicate) : this;
//    }
//
//    public Arr<E> each(final Consumer<? super E> consumer) {
//        consumer.accept(value);
//        if (!isEmpty) tail.forEach(consumer);
//        return this;
//    }
//
//    public Arr<Arr<E>> batch(final int size) {
//        record Accumulator<E>(Arr<Arr<E>> all, Arr<E> batch) {
//        }
//        final Accumulator<E> acc = reduceR(
//                new Accumulator<>(empty(), empty()),
//                (accumulator, element) -> accumulator.batch.length < size
//                        ? new Accumulator<>(accumulator.all, accumulator.batch.add(element))
//                        : new Accumulator<>(accumulator.all.add(accumulator.batch), Arr.of(element)));
//        return acc.batch.isEmpty ? acc.all : acc.all.add(acc.batch);
//    }
//
//    public Arr<E> print() {
//        System.out.println(this);
//        return this;
//    }
//
//    public Arr<E> print(final String separator) {
//        System.out.println("[" + reduceR("", (sum, element) -> sum.isBlank() ? element + "" : element + separator + sum) + "]");
//        return this;
//    }
//
//    @Override
//    public String toString() {
//        return "[" + reduceR("", (sum, element) -> sum.isBlank() ? element + "" : element + " " + sum) + "]";
//    }
//
//    @Override
//    public Iterator<E> iterator() {
//        return new Iterator<>() {
//            private Arr<E> seq = Arr.this;
//
//            @Override
//            public boolean hasNext() {
//                return !seq.isEmpty;
//            }
//
//            @Override
//            public E next() {
//                final E value = seq.value;
//                seq = seq.tail;
//                return value;
//            }
//        };
//    }
//
//    public static void main(String[] args) {
//        Arr.range(1, 10).print();
//        System.out.println(Arr.of(10, 20, -5, 7).max(Integer::compare));
//        Arr.of(10, 7, 20, -5, 7, 0).sortedBy(Integer::compare).print();
//        Arr.fromIterable(List.of("x", "y", "z")).print();
//        Arr.of("a", "b", "c", "d", "e").mapWithPrev((value, prev) -> prev + "=>" + value).print();
//        Arr.of("a", "b", "c", "d", "e").mapWithNext((value, next) -> value + "=>" + next).print();
//    }
}