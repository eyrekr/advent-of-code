package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;

/**
 * https://adventofcode.com/2023/day/9
 * 1) 2043677056
 * 2) 1062
 */
class D09 extends AoC {

    final Seq<Seq<Long>> sequences;

    D09(final String input) {
        super(input);
        this.sequences = lines.map(Str::longs);
    }

    long star1() {
        return this.sequences.map(this::extrapolateNext).reduce(0L, Long::sum);
    }

    long star2() {
        return this.sequences.map(this::extrapolatePrevious).reduce(0L, Long::sum);
    }

    long extrapolateNext(final Seq<Long> numbers) {
        var tmp = numbers;
        var lastValues = Seq.<Long>empty();
        while (!tmp.allMatch(0L)) {
            lastValues = lastValues.prepend(tmp.last(1).value);
            tmp = tmp.mapWithPrev((value, previous) -> previous == null ? null : value - previous).tail;
            tmp.print();
        }
        return lastValues.reduce(Long::sum).longValue();
    }

    long extrapolatePrevious(final Seq<Long> numbers) {
        var tmp = numbers;
        var firstValues = Seq.<Long>empty();
        while (!tmp.allMatch(0L)) {
            firstValues = firstValues.prepend(tmp.value);
            tmp = tmp.mapWithPrev((value, previous) -> previous == null ? null : value - previous).tail;
            tmp.print();
        }
        return firstValues.reduce((acc, value) -> value - acc).longValue();
    }
}
