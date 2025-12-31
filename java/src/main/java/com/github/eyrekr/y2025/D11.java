package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.Arr;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.MoreObjects.firstNonNull;

class D11 extends Aoc {

    static final String InitialDevice = "you";
    static final String TerminalDevice = "out";
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
        final Map<String, Long> paths = new HashMap<>();
        paths.put(InitialDevice, 1L);
        final Arr<String> queue = Arr.of(InitialDevice);
        queue.doWhileNotEmptyWithoutRepeats(input -> graph.getOrDefault(input, Seq.empty())
                .each(output -> {
                    final long count = paths.get(input);
                    paths.compute(output, (key, value) -> count + firstNonNull(value, 0L));
                }));
        return paths.getOrDefault(TerminalDevice, -1L);
    }

    @Override
    public long star2() {
        return -1L;
    }

}
