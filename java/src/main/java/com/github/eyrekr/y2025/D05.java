package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Int;
import com.github.eyrekr.immutable.Longs;

import java.util.Iterator;

class D05 extends Aoc {

    final Longs ingredientIds;
    final Arr<Int> freshIngredientRanges;

    D05(final String input) {
        final String[] block = input.split("\n\n");
        freshIngredientRanges = Arr.ofLinesFromString(block[0]).map(Int::fromString);
        ingredientIds = Longs.fromString(block[1]);
    }

    @Override
    public long star1() {
        return ingredientIds.countWhere(id -> freshIngredientRanges.atLeastOneIs(range -> range.contains(id)));
    }

    @Override
    public long star2() {
        final Arr<Int> sortedRanges = freshIngredientRanges.sortedBy(range -> range.a);
        Arr<Int> mergedRanges = Arr.empty();

        final Iterator<Int> queue = sortedRanges.iterator();
        Int previousRange = null;
        while (queue.hasNext()) {
            final Int range = queue.next();
            if (previousRange == null) previousRange = range;
            else if (previousRange.overlaps(range)) previousRange = previousRange.merge(range);
            else {
                mergedRanges = mergedRanges.addLast(previousRange);
                previousRange = range;
            }
            if (!queue.hasNext()) mergedRanges = mergedRanges.addLast(previousRange);
        }

        return mergedRanges.mapToLongs(Int::length).sum();
    }

}