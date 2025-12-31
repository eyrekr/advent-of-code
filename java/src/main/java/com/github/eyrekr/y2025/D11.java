package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.Arr;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.MoreObjects.firstNonNull;

class D11 extends Aoc {

    final Map<String, Seq<String>> graph;

    D11(final String input) {
        graph = new HashMap<>();
        Arr.ofLinesFromString(input).each(line -> {
            final String[] blocks = StringUtils.split(line, ':');
            graph.put(blocks[0], Seq.ofWordsFromString(blocks[1], " "));
        });
    }

    @Override
    public long star1() {
        return countPaths("you").get("out");
    }

    @Override
    public long star2() {
        return -1L;
    }

    // works by accident; the traversal should be in topological order!
    Map<String, Long> countPaths(final String start) {
        final Map<String, Long> paths = new HashMap<>();
        paths.put(start, 1L);
        final Arr<String> queue = Arr.of(start);
        queue.doWhileNotEmptyWithoutRepeats(input -> graph.getOrDefault(input, Seq.empty())
                .each(output -> {
                    final long count = paths.get(input);
                    paths.compute(output, (key, value) -> count + firstNonNull(value, 0L));
                }));
        return paths;
    }
}
