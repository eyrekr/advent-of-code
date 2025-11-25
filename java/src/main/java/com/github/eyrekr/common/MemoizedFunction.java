package com.github.eyrekr.common;

import java.util.HashMap;
import java.util.Map;

public abstract class MemoizedFunction<T, R> {
    private final Map<T, R> cache = new HashMap<>();

    public final R get(final T value) {
        final R memoizedResult = cache.get(value);
        if (memoizedResult != null) return memoizedResult;

        final R result = calculate(value);
        cache.put(value, result);
        return result;
    }

    protected abstract R calculate(final T value);
}
