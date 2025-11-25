package com.github.eyrekr.aop;

import com.github.eyrekr.annotation.Memoize;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.HashMap;
import java.util.Map;

@Aspect
public class MemoizeAspect {

    private final Map<String, Map<String, Object>> caches = new HashMap<>();

    @Around("@annotation(memoize)")
    public Object cacheMethod(final ProceedingJoinPoint joinPoint, final Memoize memoize) throws Throwable {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final String name = signature.getMethod().getDeclaringClass().getName() + "?" + memoize.value();
        final Map<String, Object> cache = caches.computeIfAbsent(name, unused -> new HashMap<>());
        final String key = buildKeyFromArgumentValues(joinPoint.getArgs());

        System.out.printf("%s.get(%s) = %s\n", name, key, cache.get(key));

        if (cache.containsKey(key)) return cache.get(key); // the value for the key can be null!

        final Object value = joinPoint.proceed();
        cache.put(key, value);
        return value;
    }

    private static String buildKeyFromArgumentValues(final Object[] args) {
        final StringBuilder builder = new StringBuilder();
        if (args != null) for (final Object arg : args) serialize(arg, builder);
        return builder.toString();
    }

    private static void serialize(final Object object, final StringBuilder builder) {
        if (!builder.isEmpty()) builder.append(':');
        if (object == null) builder.append("null");
        else if (object instanceof final String string) builder.append("'").append(string).append("'");
        else if (object instanceof final Boolean value) builder.append((boolean) value);
        else if (object instanceof final Integer value) builder.append((int) value);
        else if (object instanceof final Long value) builder.append((long) value);
        else if (object instanceof final Double value) builder.append((double) value);
        else if (object instanceof final Float value) builder.append((float) value);
        else if (object instanceof final Character value) builder.append((char) value);
        else throw new UnsupportedOperationException(object.getClass().getName());
    }


}
