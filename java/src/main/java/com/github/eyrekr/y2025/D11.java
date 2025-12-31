package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.Arr;
import com.github.eyrekr.output.Out;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

class D11 extends Aoc {

    final int all = 0, fft = 1, dac = 2, both = 3;
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
        return countPaths("you").get("out")[all];
    }

    @Override
    public long star2() {
        return countPaths("svr").get("out")[both];
    }

    // works by accident; the traversal should be in topological order!
    Map<String, long[]> countPaths(final String start) {
        final Map<String, long[]> paths = new HashMap<>();
        paths.put(start, new long[]{1, 0, 0, 0});

        final Arr<String> queue = Arr.of(start);
        queue.doWhileNotEmptyWithoutRepeats(input -> graph.getOrDefault(input, Seq.empty())
                .each(output -> {
                    final long[] i = paths.get(input);
                    final long[] o = paths.computeIfAbsent(output, key -> new long[]{0, 0, 0, 0});
                    o[all] += i[all];
                    if ("fft".equals(output)) {
                        o[fft] += i[all];
                        o[dac] += i[dac];
                        o[both] += i[dac];
                    } else if ("dac".equals(output)) {
                        o[fft] += i[fft];
                        o[dac] += i[all];
                        o[both] += i[fft];
                    } else {
                        o[fft] += i[fft];
                        o[dac] += i[dac];
                        o[both] += i[both];
                    }

                    Out.print("""
                                    %s -> %s : %d·%d·%d·%d
                                    """,
                            input, output, o[0], o[1], o[2], o[3]);
                }));
        return paths;
    }
}
