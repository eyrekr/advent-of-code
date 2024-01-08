package com.github.eyrekr.y2023;

import com.github.eyrekr.AoC;
import com.github.eyrekr.immutable.Seq;

/**
 * https://adventofcode.com/2023/day/9
 * 1) 2043677056
 * 2) 1062
 */
class D09 extends AoC {

    final Seq<Seq<Long>> sequences;

    D09(final String input) {
        super(input);
        this.sequences = lines.map(Seq::ofNumbersFromString);
    }

    long star1() {
        return this.sequences.map(this::extrapolateNext).reduce(0L, Long::sum);
    }

    long star2() {
        return this.sequences.map(this::extrapolatePrevious).reduce(0L, Long::sum);
    }

    long extrapolateNext(final Seq<Long> numbers) {
        var seq = numbers;
        var lastValues = Seq.<Long>empty();
        while (!seq.allMatch(0L)) {
            lastValues = lastValues.addFirst(seq.lastValue);
            seq = seq.mapWithPrev((value, previous) -> previous == null ? null : value - previous).tail;
            seq.print();
        }
        return lastValues.reduce(Long::sum).get();
    }

    long extrapolatePrevious(final Seq<Long> numbers) {
        var seq = numbers;
        var firstValues = Seq.<Long>empty();
        while (!seq.allMatch(0L)) {
            firstValues = firstValues.addFirst(seq.value);
            seq = seq.mapWithPrev((value, previous) -> previous == null ? null : value - previous).tail;
            seq.print();
        }
        return firstValues.reduce((acc, value) -> value - acc).get();
    }
}
