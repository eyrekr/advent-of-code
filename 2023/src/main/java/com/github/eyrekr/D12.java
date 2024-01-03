package com.github.eyrekr;

import com.github.eyrekr.immutable.Seq;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * https://adventofcode.com/2023/day/12
 * 1) 6871
 * 2) 2043098029844
 */
class D12 extends AoC {

    final Seq<Row> rows;
    final Seq<Row> unfoldedRows;

    D12(final String input) {
        super(input);
        rows = lines.map(Row::fromString);
        unfoldedRows = lines.map(line -> {
                    final String[] ab = StringUtils.split(line);
                    return StringUtils.repeat(ab[0], "?", 5) + " " + StringUtils.repeat(ab[1], ",", 5);
                })
                .map(Row::fromString);
    }

    long star1() {
        return rows.map(row -> arrange(row.stencils, row.runs, new Cache())).reduce(Long::sum).get();
    }

    long star2() {
        return unfoldedRows.map(row -> arrange(row.stencils, row.runs, new Cache())).reduce(Long::sum).get();
    }

    static long arrange(final Seq<String> stencils, final Seq<Long> runs, final Cache cache) {
        return cache.get(stencils, runs, () -> {
            if (runs.isEmpty) return stencils.allAre(D12::skippable) ? 1L : 0L;
            if (stencils.isEmpty) return 0L;

            final int runLength = runs.value.intValue();
            final String stencil = stencils.value;

            long result = skippable(stencil) ? arrange(stencils.tail, runs, cache) : 0L;
            final int lastI = stencil.length() - runLength;
            for (int i = 0; i <= lastI; i++) {
                final String prefix = StringUtils.substring(stencil, 0, i);
                if (!skippable(prefix)) return result;
                if (i < lastI && stencil.charAt(i + runLength) == '#')
                    continue; // after the placement there must be ? (translated to .) which separates the runs

                final String remainder = StringUtils.substring(stencil, i + runLength + 1);
                final Seq<String> remainingStencils = remainder.isBlank() ? stencils.tail : stencils.tail.addFirst(remainder);

                final var r = arrange(remainingStencils, runs.tail, cache);
                result = result + r;
            }
            return result;
        });
    }

    static boolean skippable(final String stencil) {
        if (stencil != null) {
            for (int i = 0; i < stencil.length(); i++) {
                if (stencil.charAt(i) != '?') {
                    return false;
                }
            }
        }
        return true;
    }

    record Row(Seq<String> stencils, Seq<Long> runs) {
        static Row fromString(final String line) {
            return new Row(
                    Seq.fromArray(StringUtils.split(StringUtils.substringBefore(line, ' '), '.')),
                    Seq.ofNumbersFromString(StringUtils.substringAfter(line, ' ')));
        }
    }

    static class Cache {
        final Map<String, Long> map = new HashMap<>();

        long get(final Seq<String> stencils, final Seq<Long> runs, final Supplier<Long> compute) {
            final String key = stencils.toString() + runs.toString();
            final Long cachedValue = map.get(key);
            if (cachedValue != null) {
                return cachedValue;
            }
            final long value = compute.get();
            map.put(key, value);
            return value;
        }
    }
}