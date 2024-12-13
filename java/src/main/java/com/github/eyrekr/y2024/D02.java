package com.github.eyrekr.y2024;

import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Int;
import com.github.eyrekr.immutable.Longs;

/**
 * https://adventofcode.com/2024/day/2
 * 1) 483
 * 2)
 */
class D02 {

    static final Int AllowedAscendingDelta = new Int(1, 3);
    static final Int AllowedDescendingDelta = new Int(-3, -1);

    final Arr<Longs> reports;

    D02(final String input) {
        reports = Arr.ofLinesFromString(input).map(Longs::fromString);
    }

    long star1() {
        return reports.where(this::safe).length;
    }

    long star2() {
        return reports.where(this::safeAfterDampening).length;
    }

    private boolean safe(final Longs levels) {
        final var deltas = levels.deltas();
        return switch (levels.ordering()) {
            case Constant, Random -> false;
            case Ascending -> deltas.allAre(AllowedAscendingDelta::contains);
            case Descending -> deltas.allAre(AllowedDescendingDelta::contains);
        };
    }

    private boolean safeAfterDampening(final Longs levels) {
        final var deltas = levels.deltas();
        return safeAscendingWithDampening(deltas) || safeDescendingWithDampening(deltas);
    }

    private boolean safeAscendingWithDampening(final Longs deltas) { // ascending
        final Arr<BadLevel> badLevels = deltas.reduce(
                Arr.empty(),
                (arr, value, i, first, last) -> AllowedAscendingDelta.notContains(value)
                        ? arr.addLast(new BadLevel(value, i, first, last))
                        : arr);
        return switch (badLevels.length) {
            case 0 -> true;
            case 1 -> {
                final BadLevel a = badLevels.at(0);
                yield a.first || a.last || a.value == 0;
            }
            case 2 -> {
                final BadLevel a = badLevels.at(0), b = badLevels.at(1);
                yield a.i + 1 == b.i && AllowedAscendingDelta.contains(a.value + b.value);
            }
            default -> false;
        };
    }

    private boolean safeDescendingWithDampening(final Longs deltas) { // descending
        final Arr<BadLevel> badLevels = deltas.reduce(
                Arr.empty(),
                (arr, value, i, first, last) -> AllowedDescendingDelta.notContains(value)
                        ? arr.addLast(new BadLevel(value, i, first, last))
                        : arr);
        return switch (badLevels.length) {
            case 0 -> true;
            case 1 -> {
                final BadLevel a = badLevels.at(0);
                yield a.first || a.last || a.value == 0;
            }
            case 2 -> {
                final BadLevel a = badLevels.at(0), b = badLevels.at(1);
                yield a.i + 1 == b.i && AllowedDescendingDelta.contains(a.value + b.value);
            }
            default -> false;
        };
    }


    record BadLevel(long value, int i, boolean first, boolean last) {
    }
}