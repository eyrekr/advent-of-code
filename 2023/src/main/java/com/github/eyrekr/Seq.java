package com.github.eyrekr;

import java.util.Comparator;
import java.util.Iterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

final class Seq<E> implements Iterable<E> {
    final E value;
    final int length;
    final Seq<E> tail;
    final boolean isEmpty;

    private Seq(final E value, final Seq<E> tail) {
        this.value = value;
        this.length = (tail == null) ? 0 : tail.length + 1;
        this.tail = tail;
        this.isEmpty = (tail == null);
    }

    static <T> Seq<T> empty() {
        return new Seq<>(null, null);
    }

    static <T> Seq<T> of(final T first, final T... rest) {
        Seq<T> seq = empty();
        if (rest != null) {
            for (int i = rest.length - 1; i >= 0; i--) {
                seq = new Seq<>(rest[i], seq);
            }
        }
        return seq.add(first);
    }

    static <T> Seq<T> fromArray(final T[] array) {
        Seq<T> seq = empty();
        if (array != null) {
            for (int i = array.length - 1; i >= 0; i--) {
                seq = new Seq<>(array[i], seq);
            }
        }
        return seq;
    }

    static <T> Seq<T> fromIterable(final Iterable<T> collection) {
        Seq<T> seq = empty();
        if (collection != null) {
            for (final T element : collection) {
                seq = new Seq<>(element, seq);
            }
        }
        return seq.reverse();
    }

    Seq<E> add(final E value) {
        return new Seq<>(value, this);
    }

    Seq<E> addSeq(final Seq<E> seq) {
        return seq.reduce(this, (sum, element) -> new Seq<>(element, sum));
    }

    E at(final int i) {
        return (i % length == 0) ? value : i > 0 ? tail.at((i % length) - 1) : tail.at(i % length);
    }

    boolean has(final E value) {
        return !isEmpty && (this.value == value || tail.has(value));
    }

    <R> Seq<R> map(final Function<? super E, R> translate) {
        return isEmpty ? empty() : new Seq<>(translate.apply(value), tail.map(translate));
    }

    <R> Seq<R> flatMap(final Function<? super E, Seq<R>> translate) {
        return reduce(empty(), (seq, element) -> seq.addSeq(translate.apply(element)));
    }

    <R> Seq<R> genMap(final Generator<? super E, R> generator) {
        return genMapContextual(generator, null);
    }

    private <R> Seq<R> genMapContextual(final Generator<? super E, R> generator, final E previous) {
        return isEmpty
                ? empty()
                : tail.genMapContextual(generator, value).addSeq(generator.generate(previous, value, tail.value));
    }

    @FunctionalInterface
    interface Generator<T, R> {
        Seq<R> generate(T previous, T current, T next);
    }

    <R> R reduce(final R init, final BiFunction<? super R, ? super E, ? extends R> combine) { // left-to-right
        return isEmpty ? init : combine.apply(tail.reduce(init, combine), value);
    }

    <R> R reduceR(final R init, final BiFunction<? super R, ? super E, ? extends R> combine) { // right-to-left
        return isEmpty ? init : tail.reduceR(combine.apply(init, value), combine);
    }

    Seq<E> filter(final Predicate<? super E> predicate) {
        return isEmpty ? this : predicate.test(value) ? new Seq<>(value, tail.filter(predicate)) : tail.filter(predicate);
    }

    Seq<E> reverse() {
        return reduceR(empty(), (seq, element) -> new Seq<>(element, seq));
    }

    Seq<E> sortedBy(final Comparator<? super E> comparator) {
        if (isEmpty) {
            return this;
        }
        final Seq<E> lower = tail.filter(element -> comparator.compare(value, element) <= 0).sortedBy(comparator);
        final Seq<E> upper = tail.filter(element -> comparator.compare(value, element) > 0).sortedBy(comparator);
        return lower.add(value).addSeq(upper);
    }

    Seq<E> sorted() {
        return sortedBy(Comparator.comparing((Object a) -> ((Comparable) a)));
    }

    E min(final Comparator<? super E> comparator) {
        if (isEmpty) {
            return null;
        }
        final E min = tail.min(comparator);
        return min == null ? value : comparator.compare(value, min) < 0 ? value : min;
    }

    E max(final Comparator<? super E> comparator) {
        if (isEmpty) {
            return null;
        }
        final E max = tail.max(comparator);
        return max == null ? value : comparator.compare(value, max) > 0 ? value : max;
    }

    Seq<Seq<E>> split(final Predicate<? super E> predicate) {
        return reduce(Seq.of(empty(), empty()), (acc, element) -> predicate.test(element) ? Seq.of(acc.value, acc.tail.value.add(element)) : Seq.of(acc.value.add(element), acc.tail.value));
    }

    Seq<E> first(final int n) {
        return isEmpty ? this : n == 0 ? empty() : new Seq<>(value, tail.first(n - 1));
    }

    Seq<E> last(final int n) {
        return length <= n ? this : tail.last(n);
    }

    Seq<E> skip(final int n) {
        return isEmpty || n <= 0 ? this : tail.skip(n - 1);
    }

    Seq<E> takeWhile(final Predicate<? super E> predicate) {
        return isEmpty ? this : predicate.test(value) ? new Seq<>(value, tail.takeWhile(predicate)) : empty();
    }

    Seq<E> skipWhile(final Predicate<? super E> predicate) {
        return isEmpty ? this : predicate.test(value) ? tail.skipWhile(predicate) : this;
    }

    Seq<E> each(final Consumer<? super E> consumer) {
        consumer.accept(value);
        if (!isEmpty) tail.forEach(consumer);
        return this;
    }

    Seq<Seq<E>> batch(final int size) {
        record Accumulator<E>(Seq<Seq<E>> all, Seq<E> batch) {
        }
        final Accumulator<E> acc = reduce(
                new Accumulator<>(empty(), empty()),
                (accumulator, element) -> accumulator.batch.length < size
                        ? new Accumulator<>(accumulator.all, accumulator.batch.add(element))
                        : new Accumulator<>(accumulator.all.add(accumulator.batch), Seq.of(element)));
        return acc.batch.isEmpty ? acc.all : acc.all.add(acc.batch);
    }

    Seq<E> print() {
        System.out.println(this);
        return this;
    }

    Seq<E> print(final String separator) {
        System.out.println("[" + reduce("", (sum, element) -> sum.isBlank() ? element + "" : element + separator + sum) + "]");
        return this;
    }

    @Override
    public String toString() {
        return "[" + reduce("", (sum, element) -> sum.isBlank() ? element + "" : element + " " + sum) + "]";
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
        Seq.of("a", "b", "c", "d", "e").genMap((prev, cur, next)->Seq.of(prev+cur, cur, cur+next)).print();
    }
}