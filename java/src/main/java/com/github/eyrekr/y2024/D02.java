package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Int;
import com.github.eyrekr.immutable.Longs;

/**
 * https://adventofcode.com/2024/day/2
 * 1) 483
 * 2) 528
 */
class D02 extends Aoc {

    static final Int AscendingDeltas = new Int(1, 3);
    static final Int DescendingDeltas = new Int(-3, -1);

    final Arr<Longs> reports;

    D02(final String input) {
        reports = Arr.ofLinesFromString(input).map(Longs::fromString);
    }

    @Override
    public long star1() {
        return reports
                .map(Longs::deltas)
                .where(deltas -> deltas.allAre(AscendingDeltas::contains) || deltas.allAre(DescendingDeltas::contains))
                .length;
    }

    @Override
    public long star2() {
        return reports
                .map(Longs::deltas)
                .where(deltas -> safeAscendingWithDampening(deltas) || safeDescendingWithDampening(deltas))
                .length;
    }

    private boolean safeAscendingWithDampening(final Longs deltas) { // ascending
        final Arr<BadLevel> badLevels = deltas.reduce(
                Arr.empty(),
                (arr, value, next, prev, i, first, last) -> AscendingDeltas.notContains(value)
                        ? arr.addLast(new BadLevel(value, next, prev, i, first, last))
                        : arr);
        return switch (badLevels.length) {
            case 0 -> true;
            case 1 -> {
                final BadLevel bad = badLevels.at(0);
                yield bad.first
                        || bad.last
                        || AscendingDeltas.contains(bad.value + bad.previousValue)
                        || AscendingDeltas.contains(bad.value + bad.nextValue);
            }
            case 2 -> {
                final BadLevel a = badLevels.at(0), b = badLevels.at(1);
                yield a.i + 1 == b.i && AscendingDeltas.contains(a.value + b.value);
            }
            default -> false;
        };
    }

    private boolean safeDescendingWithDampening(final Longs deltas) { // descending
        final Arr<BadLevel> badLevels = deltas.reduce(
                Arr.empty(),
                (arr, value, next, prev, i, first, last) -> DescendingDeltas.notContains(value)
                        ? arr.addLast(new BadLevel(value, next, prev, i, first, last))
                        : arr);
        return switch (badLevels.length) {
            case 0 -> true;
            case 1 -> {
                final BadLevel bad = badLevels.at(0);
                yield bad.first
                        || bad.last
                        || DescendingDeltas.contains(bad.value + bad.previousValue)
                        || DescendingDeltas.contains(bad.value + bad.nextValue);
            }
            case 2 -> {
                final BadLevel a = badLevels.at(0), b = badLevels.at(1);
                yield a.i + 1 == b.i && DescendingDeltas.contains(a.value + b.value);
            }
            default -> false;
        };
    }


    record BadLevel(long value, long nextValue, long previousValue, int i, boolean first, boolean last) {
    }
}