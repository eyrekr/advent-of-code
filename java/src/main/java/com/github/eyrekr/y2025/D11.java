package com.github.eyrekr.y2025;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.Arr;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.util.function.Predicate.not;

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

    Map<String, long[]> countPaths(final String start) {
        final Map<String, long[]> paths = new HashMap<>();
        paths.put(start, new long[]{1, 0, 0, 0});
        topologicallySortedVertices(graph, start, new HashSet<>(), Arr.empty())
                .each(u -> graph.getOrDefault(u, Seq.empty())
                        .each(v -> {
                            final long[] i = paths.get(u);
                            if (i == null) return;
                            final long[] o = paths.computeIfAbsent(v, key -> new long[]{0, 0, 0, 0});
                            o[all] += i[all];
                            if ("fft".equals(v)) {
                                o[fft] += i[all];
                                o[dac] += i[dac];
                                o[both] += i[dac];
                            } else if ("dac".equals(v)) {
                                o[fft] += i[fft];
                                o[dac] += i[all];
                                o[both] += i[fft];
                            } else {
                                o[fft] += i[fft];
                                o[dac] += i[dac];
                                o[both] += i[both];
                            }
                        }));
        return paths;
    }

    Arr<String> topologicallySortedVertices(final Map<String, Seq<String>> graph,
                                            final String u,
                                            final Set<String> visited,
                                            final Arr<String> order) {
        visited.add(u);
        graph.getOrDefault(u, Seq.empty()).where(not(visited::contains)).each(v -> topologicallySortedVertices(graph, v, visited, order));
        return order.addFirst(u); // post-order traversal
    }

}