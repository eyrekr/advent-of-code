package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;
import org.apache.commons.lang3.StringUtils;

/**
 * https://adventofcode.com/2023/day/12
 * 1)
 * 2)
 */
class D12 extends AoC {

    final Seq<Row> rows;

    D12(final String input) {
        super(input);
        rows = lines.map(line -> new Row(
                StringUtils.substringBefore(line, ' '),
                Str.longs(StringUtils.substringAfter(line, ' '))));
    }

    long star1() {
        return rows.map(this::arrangements).reduce(Long::sum);
    }

    long arrangements(final Row row) {
        final int q = StringUtils.countMatches(row.stencil, '?');
        if (q == 0) {
            return 1L;
        }
        final long options = 1L << q;
        final char[] filledStencil = row.stencil.toCharArray();
        long sum = 0L;
        Str.print("**ROW %s**  %s\n", row.stencil, row.rle.toString());
        for (long option = 0L; option < options; option++) {
            long x = option;
            for (int i = 0; i < filledStencil.length; i++) {
                switch (row.stencil.charAt(i)) {
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


    record Row(String stencil, Seq<Long> rle) {
    }
}