package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Longs;
import org.apache.commons.lang3.StringUtils;

class D12 extends Aoc {

    final Longs shapes;
    final Arr<Region> regions;

    D12(final String input) {
        final String[] blocks = StringUtils.splitByWholeSeparator(input, "\n\n");
        shapes = Arr.fromArray(blocks).removeLast().toLongs(block -> StringUtils.countMatches(block, '#'));
        final int n = blocks.length - 1;
        regions = Arr.ofLinesFromString(blocks[n]).map(Region::fromLine);
    }

    @Override
    public long star1() {
        return regions.countWhere(region -> {
            // heuristic 1: the area of the region is so big that it can fit all shapes without overlapping
            final long area = region.width >= 3 && region.length >= 3 ? region.width * region.length : -1L;
            final long areaBigEnoughThatWillEasilyFitAllShapesWithoutAnyInterlocking = region.counts.sum() * 9;
            if (area >= areaBigEnoughThatWillEasilyFitAllShapesWithoutAnyInterlocking) return true;

            // heuristic 2: the area of the region is so small that it cannot fit all the shapes even with perfect interlocking (that leaves no space)
            final long minimumRequiredAreaToFitAllShapesWithPerfectInterlocking = region.counts.reduce(
                    0L,
                    (accumulator, count, nextValue, previousValue, i, first, last) -> accumulator + count * shapes.at(i));
            if (area < minimumRequiredAreaToFitAllShapesWithPerfectInterlocking) return false;

            throw new IllegalStateException("lot of manual work I don't want to do");
        });
    }

    record Region(long width, long length, Longs counts) {
        static Region fromLine(final String line) {
            final Longs values = Longs.fromString(line);
            return new Region(values.at(0), values.at(1), values.skip(2));
        }
    }
}
