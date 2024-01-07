package com.github.eyrekr.mutable;

import com.github.eyrekr.math.Algebra;

public final class Hp<E> {
    private static final int MIN_CAPACITY = 16;

    private int length;
    private X<E>[] a;

    private Hp(final int capacity) {
        final int n = 1 << Algebra.log2(capacity);
        this.a = new X[Math.max(MIN_CAPACITY, n)];
        this.length = 0;
    }

    private void makeSureThereIsEnoughCapacity() {
        if (length < a.length) return;
        final int n = a.length << 1;
        final X<E>[] b = new X[n];
        System.arraycopy(a, 0, b, 0, length);
        a = b;
    }

    private void down(final int k) {
        int current = k, left = 2*k +1, right = 2*k +2;

    }

    private void up(final int k) {
        int current = k, parent = (k - 1) / 2;
        while (current > 0 && a[current].key < a[parent].key) {
            swap(current, parent);
            current = parent;
            parent = (parent - 1) / 2;
        }
    }

    private void swap(final int i, final int j) {
        final X<E> p = a[i], q = a[j];
        a[i] = q;
        a[j] = p;
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

    public E getMin() {
        return length > 0 ? a[0].element : null;
    }

    public E extractMin() {
        return null;
    }

    static class X<T> {
        final T element;
        int key;

        X(T element, int key) {
            this.element = element;
            this.key = key;
        }
    }
}
