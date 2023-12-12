package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * https://adventofcode.com/2023/day/12
 * 1) 6871
 * 2)
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
        }).map(Row::fromString);
    }

    long star1() {
        return rows
                .map(row -> {
                    long sum = tryToArrange(row.stencils, row.rle);
                    long control = arrangements(row, true);
                    Str.print("%s**%-40s**  %-40s  %10d %10d\n", control == sum ? "@g" : "@r", row.theWholeStencil, row.rle, sum, control);
                    return sum;
                })
                .reduce(Long::sum);
    }

    static long tryToArrange(final Seq<String> stencils, final Seq<Long> rle) {
        return tryToArrange(stencils, rle, new HashMap<>());
    }

    static long tryToArrange(final Seq<String> stencils, final Seq<Long> rle, final Map<String, Long> cache) {
        final Long valueFromCache = cache.get(key(stencils, rle));
        if (valueFromCache != null) {
            return valueFromCache;
        }

        if (rle.isEmpty) {
            // several options here
            if (stencils.isEmpty) {
                return 1; // one way to satisfy it
            } else if (stencils.allAre(D12::skippable)) {
                // only ???? remain in the last groups!! (not just one group)!!!
                // there is just one way to satisfy them when RLE is empty => all must be .
                return 1;
            } else {
                // there is a problem, bad guess above
                return 0;
            }
        }
        if (stencils.isEmpty) {
            return 0;
        }

        final int runLength = rle.value.intValue();
        // find ways how to satisfy the first run
        // - it must be satisfied by the first group
        // - but not necessarily byt the whole group
        final String stencil = stencils.value;

        // number of ways to combine the runs into the stencils
        long result = 0;
        {
            // try to go over skippable stencils
            // meaning: those stencils that are all ???? we can make them vanish by converting the whole ???? to ....
            // these cases must also be counted
            if (skippable(stencil)) {
                // this is now the baseline
                result = tryToArrange(stencils.tail, rle, cache);
                cache.put(key(stencils.tail, rle), result);
            }
        }


        if (stencil.length() == runLength) {
            // easy case - the whole group must satisfy the run
            // try to arrange the remaining stencils
            final var r = tryToArrange(stencils.tail, rle.tail, cache);
            cache.put(key(stencils.tail, rle.tail), r);
            return result + r;
        } else if (stencil.length() < runLength) {
            // bad guess previously, the group is not big enough to satisfy the run
            return result;
        } else {
            // the dreaded case where we must iterate over all the options
            // we know we have a run of length say 3, then we have a stencil of length say 6,
            // so there are (in theory) at most 4 ways to place the ### into the stencil
            // ###... .###.. ..###. ...###
            // that does not sound that dreadful

            final int indexOfLastPossiblePlacement = stencil.length() - runLength;
            for (int i = 0; i <= indexOfLastPossiblePlacement; i++) { // i = all possible starting positions of the run in the stencil
                // we must first verify that the stencil supports a run of this size
                // the problem is that we can have multiple # predetermined!

                // we are trying to put ###. or .###. or ..### or ...### into the stencil;
                // be careful, because if it is not the terminating ...###, we must use one more . to terminate the run!

                // REMEMBER: THE STENCIL ONLY HAS ? AND # IN THEM, NOTHING ELSE
                // 1. all characters before the position i must be ? (we need to replace them with .)
                for (int j = 0; j < i; j++) {
                    if (stencil.charAt(j) == '#') {
                        // this is not a valid state, we cannot put ..###. into a stencil ?#????
                        // we can abandon all placement attempts because we will never fit any other run into the stencil
                        return result;
                    }
                }
                // 2. the next runLength characters after the position i must be #
                // but that is easily done - because the stencil is only # or ?, we can satisfy that simply by turning ? into #
                // there is nothing we have to do about this here

                // 3. the character after the run must be either <end> or ? which we need to turn into the .
                // that means that the only thing that can kill it is # after the run
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
        for (int i = 0; i < stencil.length(); i++) {
            if (stencil.charAt(i) != '?') {
                return false;
            }
        }
        return true;
    }

    static String key(final Seq<String> stencils, final Seq<Long> runs) {
        return stencils.toString() + runs.toString();
    }

    long arrangements(final Row row, boolean show) {
        final int q = StringUtils.countMatches(row.theWholeStencil, '?');
        if (q == 0) {
            return 1L;
        }
        final long options = 1L << q;
        final char[] filledStencil = row.theWholeStencil.toCharArray();
        long sum = 0L;
        for (long option = 0L; option < options; option++) {
            long x = option;
            for (int i = 0; i < filledStencil.length; i++) {
                switch (row.theWholeStencil.charAt(i)) {
                    case '.' -> filledStencil[i] = '.';
                    case '#' -> filledStencil[i] = '#';
                    case '?' -> {
                        filledStencil[i] = (x & 1L) > 0 ? '#' : '.';
                        x = x >> 1;
                    }
                }
            }
            final Seq<Long> rle = rle(filledStencil);
            final boolean matches = rle.equals(row.rle);
            if (matches) {
                sum++;
                if (show) {
                    Str.print("@w%s\n", String.valueOf(filledStencil));
                }
            }
        }
        return sum;
    }

    Seq<Long> rle(final char[] stencil) {
        Seq<Long> seq = Seq.empty();
        long run = 0L;
        for (char ch : stencil) {
            switch (ch) {
                case '.': {
                    if (run > 0L) {
                        seq = seq.append(run);
                        run = 0L;
                    }
                    break;
                }
                case '#': {
                    run++;
                    break;
                }
                case '?': {
                    return null;
                }
            }
        }
        if (run > 0L) {
            seq = seq.append(run);
        }
        return seq;
    }

    long star2() {
        return unfoldedRows
                .map(row -> tryToArrange(row.stencils, row.rle))
                .reduce(Long::sum);
    }


    record Row(String theWholeStencil, Seq<String> stencils, Seq<Long> rle) {
        static Row fromString(final String line) {
            return new Row(
                    StringUtils.substringBefore(line, ' '),
                    Seq.fromArray(StringUtils.split(StringUtils.substringBefore(line, ' '), '.')),
                    Str.longs(StringUtils.substringAfter(line, ' ')));
        }
    }
}