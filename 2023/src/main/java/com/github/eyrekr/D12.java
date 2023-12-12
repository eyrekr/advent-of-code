package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

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
        return rows.map(row -> tryToArrange(row.stencils, row.rle, new HashMap<>())).reduce(Long::sum);
    }

    long star2() {
        return unfoldedRows.map(row -> tryToArrange(row.stencils, row.rle, new HashMap<>())).reduce(Long::sum);
    }

    static long tryToArrange(final Seq<String> stencils, final Seq<Long> rle, final Map<String, Long> cache) {
        final Long valueFromCache = cache.get(key(stencils, rle));
        if (valueFromCache != null) {
            return valueFromCache;
        }

        if (rle.isEmpty) return stencils.allAre(D12::skippable) ? 1 : 0;
        if (stencils.isEmpty) return 0;

        final int runLength = rle.value.intValue();
        final String stencil = stencils.value;

        long result = 0;
        if (skippable(stencil)) {
            result = tryToArrange(stencils.tail, rle, cache);
            cache.put(key(stencils.tail, rle), result);
        }

        if (stencil.length() < runLength) {
            return result;
        } else {
            final int lastI = stencil.length() - runLength;
            for (int i = 0; i <= lastI; i++) {
                final String prefix = StringUtils.substring(stencil, 0, i);
                if (!skippable(prefix)) return result; // there are fixed # in the prefix of the stencil

                if (i < lastI && stencil.charAt(i + runLength) == '#') {
                    continue; // after the placement there must be ? (translated to .) which separates the runs
                }

                final Seq<String> remainingStencils;
                if (i >= lastI - 1) { // the last or the 2nd last
                    remainingStencils = stencils.tail; // nothing remains of the original stencil
                } else {
                    final String substencil = StringUtils.substring(stencil, i + runLength + 1); // the +1 is for the .
                    remainingStencils = stencils.tail.prepend(substencil); // the remainder of the stencil
                }

                final var r = tryToArrange(remainingStencils, rle.tail, cache);
                cache.put(key(remainingStencils, rle.tail), r);

                result = result + r;
            }
            return result;
        }
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

    static String key(final Seq<String> stencils, final Seq<Long> runs) {
        return stencils.toString() + runs.toString();
    }

    record Row(Seq<String> stencils, Seq<Long> rle) {
        static Row fromString(final String line) {
            return new Row(Seq.fromArray(StringUtils.split(StringUtils.substringBefore(line, ' '), '.')), Str.longs(StringUtils.substringAfter(line, ' ')));
        }
    }
}