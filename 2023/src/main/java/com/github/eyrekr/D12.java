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
            final int indexOfLastPossiblePlacement = stencil.length() - runLength;
            for (int i = 0; i <= indexOfLastPossiblePlacement; i++) {
                final String prefix = StringUtils.substring(stencil, 0, i);
                if(!skippable(prefix)){ // there are no fixed # in the prefix of the stencil
                    return result;
                }
                final boolean isLastPlacement = (i == indexOfLastPossiblePlacement); // this is the end of the line,
                if (!isLastPlacement) {
                    // check the next character after the run, it must be ?
                    if (stencil.charAt(i + runLength) == '#') {
                        // bummer, we cannot put a . there, it is already taken by #
                        // but that does not mean that we cannot continue, we can still try other placements, they might work
                        continue; // continue with placing the run into the stencil
                    }
                }

                // we have arrived here, which means that we can successfully place the run into the stencil;
                // that means that we have to split the current stencil

                // there are two cases in which the stencil does not need to be split - basically when we consume it all
                // 1. when it is the last possible placement
                // 2. when it is the second last possible placement (we need the last ? to be a .)
                final Seq<String> remainingStencils;
                if (isLastPlacement || (i == indexOfLastPossiblePlacement - 1)) { // the last or the 2nd last
                    remainingStencils = stencils.tail;
                } else {
                    // here, we must split the stencil
                    final String substencil = StringUtils.substring(stencil, i + runLength + 1); // the +1 is for the .
                    remainingStencils = stencils.tail.prepend(substencil);
                }


                // ok, so this is the last placement which means we will not be splitting the stencil,
                // we have consumed the whole stencil
                final var r = tryToArrange(remainingStencils, rle.tail, cache);
                cache.put(key(remainingStencils, rle.tail), r);

                // combine the result with what we have so far
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