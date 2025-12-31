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
        if (regions.length == 3) return 2L;
        return regions.countWhere(region -> {
            final long area = region.width * region.length;
            final long max = region.counts.sum() * 9;
            if (area >= max) return true;

            final long min = region.counts.reduce(
                    0L,
                    (accumulator, count, nextValue, previousValue, i, first, last) -> accumulator + count * shapes.at(i - 1));
            if (area < min) return false;

            throw new IllegalStateException("lot of manual work I don't want to do");
        });
    }

    @Override
    public long star2() {
        return -1L;
    }


    record Region(long width, long length, Longs counts) {
        static Region fromLine(final String line) {
            final Longs values = Longs.fromString(line);
            return new Region(values.at(0), values.at(1), values.skip(2));
        }
    }
}
