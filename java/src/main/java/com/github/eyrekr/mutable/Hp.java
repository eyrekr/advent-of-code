package com.github.eyrekr.mutable;

import com.github.eyrekr.immutable.Opt;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.math.Algebra;
import com.github.eyrekr.output.Out;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Min-Heap
 */
public final class Hp<E> implements Iterable<E> {
    private static final int MIN_CAPACITY = 16;

    private int length;
    private long[] keys;
    private Object[] values;


    private Hp(final int capacity) {
        final int n = Math.max(MIN_CAPACITY, 1 << Algebra.log2(capacity));
        values = new Object[n];
        keys = new long[n];
        length = 0;
    }

    private Hp<E> makeSureThereIsEnoughRoomToGrow() {
        if (length < values.length) return this;
        final int n = values.length << 1;
        values = Arrays.copyOf(values, n);
        keys = Arrays.copyOf(keys, n);
        return this;
    }

    public static <T> Hp<T> empty() {
        return new Hp<>(MIN_CAPACITY);
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

    public Opt<E> getFirst() {
        return isNotEmpty() ? Opt.of((E) values[0]) : Opt.empty();
    }

    public Opt<E> removeFirst() {
        final Opt<E> e = getFirst();
        if (e.missing) return e;
        swap(0, length - 1).removeLast().heapifyDown(0);
        return e;
    }

    public Hp<E> add(final long key, final E value) {
        return makeSureThereIsEnoughRoomToGrow().addLast(key, value).heapifyUp(length - 1);
    }

    private Hp<E> heapifyUp(final int i) {
        if (i == 0) return this;
        final int p = (i - 1) / 2;
        if (keys[i] < keys[p]) swap(i, p).heapifyUp(p);
        return this;
    }

    private Hp<E> heapifyDown(final int i) {
        if (i >= length) return this;
        final int l = 2 * i + 1, r = 2 * i + 2;
        if (l < length) {
            final int j = r < length && keys[r] < keys[l] ? r : l;
            if (keys[i] > keys[j]) swap(i, j).heapifyDown(j);
        }
        return this;
    }

    private Hp<E> swap(final int i, final int j) {
        final long key = keys[i];
        keys[i] = keys[j];
        keys[j] = key;

        final Object value = values[i];
        values[i] = values[j];
        values[j] = value;
        return this;
    }

    private Hp<E> addLast(final long key, final E value) {
        keys[length] = key;
        values[length] = value;
        length += 1;
        return this;
    }

    private Hp<E> removeLast() {
        keys[length] = Integer.MAX_VALUE;
        values[length] = null;
        length -= 1;
        return this;
    }

    public <R> R reduce(final R init, final Reducer<? super E, ? super R, ? extends R> f) {
        R acc = init;
        for (int i = 0; i < length; i++) acc = f.reduce(acc, keys[i], (E) values[i]);
        return acc;
    }

    public Hp<E> each(final BiConsumer<? super Long, ? super E> consumer) {
        for (int i = 0; i < length; i++) consumer.accept(keys[i], (E) values[i]);
        return this;
    }

    public Hp<E> print() {
        return print(" ");
    }

    public Hp<E> print(final String separator) {
        return print(separator, (i, key, value) -> String.valueOf(value));
    }

    public Hp<E> print(final String separator, final Formatter<? super E> formatter) {
        for (int i = 0; i < length; i++) {
            final boolean last = i == length - 1;
            Out.print("%s%s", formatter.format(i, keys[i], (E) values[i]), last ? "\n" : separator);
        }
        return this;
    }

    public E[] toArray() {
        return (E[]) Arrays.copyOf(values, length);
    }

    public Seq<E> toSeq() {
        Seq<E> seq = Seq.empty();
        for (int i = length - 1; i >= 0; i--) seq = seq.addFirst((E) values[i]);
        return seq;
    }

    public Arr<E> toArr() {
        return Arr.fromArray((E[]) values);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i > 0) builder.append(' ');
            builder.append(values[i]);
        }
        return builder.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof final Hp that && this.length == that.length) {
            for (int i = 0; i < length; i++)
                if (!Objects.equals(this.keys[i], that.keys[i]) || !Objects.equals(this.values[i], that.values[i]))
                    return false;
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
            final E value = (E) values[i];
            i++;
            return value;
        }
    }


    @FunctionalInterface
    public interface Reducer<V, I, O extends I> {
        O reduce(I acc, long key, V value);
    }

    @FunctionalInterface
    public interface Formatter<V> {
        String format(int i, long key, V value);
    }
}
