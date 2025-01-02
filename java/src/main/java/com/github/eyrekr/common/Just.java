package com.github.eyrekr.common;

import java.util.concurrent.Callable;

public final class Just {

    public static <T> T get(final Callable<T> supplier) {
        try {
            return supplier.call();
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void repeat(final long times, final Runnable runnable) {
        for (int i = 0; i < times; i++) runnable.run();
    }

}