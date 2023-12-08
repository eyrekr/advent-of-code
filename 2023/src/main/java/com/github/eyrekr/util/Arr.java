package com.github.eyrekr.util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class Arr<E> /*implements Iterable<E> */{
    /*
    private static final Object[] EMPTY = new Object[0];

    public final E value;
    public final int length;
    public final boolean isEmpty;
    private final E[] data;
    private final int a;
    private final int b;

    private Arr(final E[] data, final int a, final int b) {
        this.data = data;
        this.a = a;
        this.b = b;
        this.length = b - a;
        this.isEmpty = this.length == 0;
        this.value = isEmpty ? null : data[a];
    }

    private Arr(final E[] data) {
        this(data, 0, data.length);
    }


    public static <T> Arr<T> empty() {
        return new Arr<>((T[]) EMPTY, 0, 0);
    }

    @SafeVarargs
    public static <T> Arr<T> of(final T first, final T... rest) {

    }

    public static <T> Arr<T> fromArray(final T[] array) {
        return array == null || array.length == 0 ? empty() : new Arr<>(Arrays.copyOf(array, array.length), 0, array.length);
    }

//    public static <T> Arr<T> fromIterable(final Iterable<T> collection) {
//
//    }
//
//    public static <T> Arr<T> fromIterator(final Iterator<T> iterator) {
//
//    }

    private E[] copy(int leading, int trailing) {
        final Class<?> newType = data.getClass();
        final int length = b - a + leading + trailing;
        E[] copy = ((Object) data.getClass() == (Object) Object[].class)
                ? (E[]) new Object[length]
                : (E[]) Array.newInstance(newType.getComponentType(), length);
        for (int i = a; i < b; i++) {
            copy[leading + i] = data[a + i];
        }
        return copy;
    }

    private E

    public Arr<E> prepend(final E value) {
        final E[] arr = copy(1, 0);
        arr[0] = value;
        return new Arr<>(arr);
    }

    public Arr<E> append(final E value) {
        final E[] arr = copy(0, 1);
        arr[b] = value;
        return new Arr<>(arr);
    }

    public E at(final int i) {
        return (i % length == 0) ? value : i > 0 ? tail.at((i % length) - 1) : tail.at(i % length);
    }

    public boolean has(final E value) {
        for (int i = a; i < b; i++) {
            if (Objects.equals(data[i], value)) {
                return true;
            }
        }
        return false;
    }

    public <R> Arr<R> map(final Function<? super E, R> translate) {
        final E[] arr = copy(0, 0);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = translate(arr[i]);
        }
    }

    public <C, R> Arr<R> carryMap(final C carry, final BiFunction<? super C, ? super E, R> translate) {

    }

    public <F, R> Arr<R> mapWith(final Arr<F> arr, final BiFunction<? super E, ? super F, R> translate) {

    }

    public <R> Arr<R> flatMap(final Function<? super E, Arr<R>> translate) {

    }

    public <R> Arr<R> mapWithNext(final BiFunction<? super E, ? super E, R> translate) {

    }

    public <R> Arr<R> mapWithPrev(final BiFunction<? super E, ? super E, R> translate) {

    }

    public <R> R reduceR(final R init, final BiFunction<? super R, ? super E, ? extends R> combine) { // left-to-right

    }

    public E reduceR(final BiFunction<? super E, ? super E, ? extends E> combine) {

    }

    public <R> R reduce(final R init, final BiFunction<? super R, ? super E, ? extends R> combine) { // right-to-left

    }

    public E reduce(final BiFunction<? super E, ? super E, ? extends E> combine) {

    }

    public Arr<E> where(final Predicate<? super E> predicate) {

    }

    public Arr<E> reverse() {

    }

    public Arr<E> sortedBy(final Comparator<? super E> comparator) {

    }

    public Arr<E> sorted() {

    }

    public E min(final Comparator<? super E> comparator) {

    }

    public E min() {

    }

    public E max(final Comparator<? super E> comparator) {

    }

    public E max() {

    }

    public Arr<Arr<E>> split(final Predicate<? super E> predicate) {

    }

    public Arr<E> first(final int n) {

    }

    public Arr<E> last(final int n) {

    }

    public Arr<E> skip(final int n) {

    }

    public Arr<E> takeWhile(final Predicate<? super E> predicate) {

    }

    public Arr<E> skipWhile(final Predicate<? super E> predicate) {

    }

    public Arr<E> each(final Consumer<? super E> consumer) {
        for (int i = a; i < b; i++) {
            consumer.accept(data[i]);
        }
        return this;
    }

    public Arr<Arr<E>> batch(final int size) {

    }

    public Arr<E> print() {
        System.out.println(this);
        return this;
    }

    public Arr<E> print(final String separator) {

        return this;
    }

    @Override
    public String toString() {

    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private Arr<E> arr = Arr.this;
            private int i = arr.a;

            @Override
            public boolean hasNext() {
                return i < arr.b;
            }

            @Override
            public E next() {
                final E value = arr.data[i];
                i++;
                return value;
            }
        };
    }

    public static void main(String[] args) {
        Arr.range(1, 10).print();
        System.out.println(Arr.of(10, 20, -5, 7).max(Integer::compare));
        Arr.of(10, 7, 20, -5, 7, 0).sortedBy(Integer::compare).print();
        Arr.fromIterable(List.of("x", "y", "z")).print();
        Arr.of("a", "b", "c", "d", "e").mapWithPrev((value, prev) -> prev + "=>" + value).print();
        Arr.of("a", "b", "c", "d", "e").mapWithNext((value, next) -> value + "=>" + next).print();
    }*/
}