package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;
import org.apache.commons.lang3.StringUtils;

/**
 * https://adventofcode.com/2023/day/12
 * 1) 6871
 * 2)
 */
class D12 extends AoC {

    final Seq<Row> rows;

    D12(final String input) {
        super(input);
        rows = lines.map(line -> new Row(
                StringUtils.substringBefore(line, ' '),
                Seq.fromArray(StringUtils.split(StringUtils.substringBefore(line, ' '), '.')),
                Str.longs(StringUtils.substringAfter(line, ' '))));
    }

    long star1() {
        //return rows.map(this::arrangements).reduce(Long::sum);
        return rows
                .map(row -> {
                    Str.print("**%s**  %s\n", row.theWholeStencil, row.rle);
                    long sum = tryToArrange(row.stencils, row.rle, "  -").sum;
                    Str.print("**%s**  %s  => %d\n", row.theWholeStencil, row.rle, sum);
                    return sum;
                })
                .reduce(Long::sum);
    }

    record Result(long sum, boolean possible) {

        static Result done(long sum) {
            return new Result(sum, true);
        }


        static final Result IMPOSSIBLE = new Result(0L, false);
    }

    static Result tryToArrange(final Seq<String> stencils, final Seq<Long> rle, final String level) {
        Str.print("%s %s  %s\n", level, stencils.value, rle.value);
        if (rle.isEmpty) {
            // several options here
            if (stencils.isEmpty) {
                return Result.done(0);
            } else if (stencils.length == 1 && stencils.value.matches("\\?+")) {
                // only ???? remain in the last group,
                // there is just one way to satisfy them when RLE is empty => all must be .
                return Result.done(1L);
            } else {
                // there is a problem, bad guess above
                return Result.IMPOSSIBLE;
            }
        }
        if (stencils.isEmpty) {
            return Result.IMPOSSIBLE;
        }

        final int runLength = rle.value.intValue();
        // find ways how to satisfy the first run
        // - it must be satisfied by the first group
        // - but not necessarily byt the whole group
        final String stencil = stencils.value;
        if (stencil.length() == runLength) {
            // easy case - the whole group must satisfy the run
            // try to arrange the remaining stencils
            final var r = tryToArrange(stencils.tail, rle.tail, "  " + level);
            return r;
        } else if (stencil.length() < runLength) {
            // bad guess previously, the group is not big enough to satisfy the run
            return Result.IMPOSSIBLE;
        } else {
            // the dreaded case where we must iterate over all the options
            // we know we have a run of length say 3, then we have a stencil of length say 6,
            // so there are (in theory) at most 4 ways to place the ### into the stencil
            // ###... .###.. ..###. ...###
            // that does not sound that dreadful

            Result result = Result.IMPOSSIBLE;

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
                final var r = tryToArrange(remainingStencils, rle.tail, "  " + level);
                // combine the result with what we have so far
                if (r.possible && !result.possible) {
                    // we found the first way to do it
                    result = r;
                } else if (r.possible && result.possible) {
                    // we already have functional solutions and now we have some more, good
                    result = Result.done(result.sum + r.sum);
                } else if (!r.possible) {
                    // keep the results we have, it was not possible to satisfy the remaining runs/stencils
                }
            }
            return result;
        }
    }

    long arrangements(final Row row) {
        final int q = StringUtils.countMatches(row.theWholeStencil, '?');
        if (q == 0) {
            return 1L;
        }
        final long options = 1L << q;
        final char[] filledStencil = row.theWholeStencil.toCharArray();
        long sum = 0L;
        Str.print("**ROW %s**  %s  %s\n", row.theWholeStencil, row.rle.toString(), row.stencils.toString());
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
                //Str.print(" - @G%s %s\n", String.valueOf(filledStencil), rle.toString());
            } else {
                //Str.print("  -@R%s %s\n", String.valueOf(filledStencil), rle.toString());
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
        return 0L;
    }


    record Row(String theWholeStencil, Seq<String> stencils, Seq<Long> rle) {
    }
}