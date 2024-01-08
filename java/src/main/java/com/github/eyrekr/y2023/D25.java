package com.github.eyrekr.y2023;

import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.output.Out;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * https://adventofcode.com/2023/day/25
 * 1) 613870
 * <p>
 * https://en.wikipedia.org/wiki/Karger%27s_algorithm
 */
class D25 {

    final Map<String, Arr<String>> graph = new HashMap<>();

    D25(final String input) {
        final Seq<Seq<String>> adjacencyList = Seq.ofLinesFromString(input)
                .map(line -> StringUtils.split(line, " :"))
                .map(Seq::fromArray);
        adjacencyList.each(seq -> seq.tail.each(node -> {
            graph.put(seq.value, graph.computeIfAbsent(seq.value, key -> Arr.empty()).addLast(node));
            graph.put(node, graph.computeIfAbsent(node, key -> Arr.empty()).addLast(seq.value));
        }));
    }

    long star1() {
        for (int attempt = 0; ; attempt++) {
            //work on a copy of the graph
            final Map<String, Arr<String>> multigraph = new HashMap<>(this.graph);

            //remember the sizes of the components
            final Map<String, Integer> componentSize = new HashMap<>();

            //repeat random edge contraction until there are only 2 vertices left
            for (int n = multigraph.keySet().size(); n > 2; n--) {
                //pick "random" edge
                final String u = Arr.fromIterable(multigraph.keySet()).random();
                final String v = multigraph.get(u).random();

                //contract the edge
                final String w = "V" + n;
                final var nodesToU = multigraph.remove(u).where(node -> !Objects.equals(node, v));
                final var nodesToV = multigraph.remove(v).where(node -> !Objects.equals(node, u));
                nodesToU.each(node -> multigraph.put(node, multigraph.get(node).replaceAll(u, w)));
                nodesToV.each(node -> multigraph.put(node, multigraph.get(node).replaceAll(v, w)));
                multigraph.put(w, nodesToU.addLast(nodesToV));

                //merge the two components
                componentSize.put(w, firstNonNull(componentSize.remove(u), 1) + firstNonNull(componentSize.remove(v), 1));
            }

            final int k = multigraph.values().iterator().next().length;
            Out.print("%3d: %s%s\n", attempt + 1, k == 3 ? "@G" : "@R", k);

            //we know the k-edge connectivity is 3
            if (k == 3) return componentSize.values().stream().reduce(1, (a, b) -> a * b);
        }
    }
}
