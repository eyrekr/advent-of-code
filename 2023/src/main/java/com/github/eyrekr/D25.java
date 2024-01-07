package com.github.eyrekr;

import com.github.eyrekr.common.Just;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.output.Out;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.MoreObjects.firstNonNull;

/**
 * https://adventofcode.com/2023/day/25
 * 1) 613870
 *
 * https://en.wikipedia.org/wiki/Karger%27s_algorithm
 */
class D25 extends AoC {

    final SecureRandom random = Just.get(SecureRandom::getInstanceStrong);
    final Seq<Seq<String>> adjacencyList;

    D25(final String input) {
        super(input);
        adjacencyList = lines.map(line -> StringUtils.split(line, " :")).map(Seq::fromArray);
    }

    @Override
    long star1() {
        for (int attempt = 0; ; attempt++) {
            //work on a copy of the graph
            final Map<String, Seq<String>> multigraph = new HashMap<>();
            adjacencyList.each(seq -> seq.tail.each(node -> {
                multigraph.put(seq.value, multigraph.computeIfAbsent(seq.value, key -> Seq.empty()).addFirst(node));
                multigraph.put(node, multigraph.computeIfAbsent(node, key -> Seq.empty()).addFirst(seq.value));
            }));

            //remember the sizes of the components
            final Map<String, Integer> componentSize = new HashMap<>();

            //repeat random edge contraction until there are only 2 vertices left
            for (int n = multigraph.keySet().size(); n > 2; n--) {
                //pick "random" edge
                final String u = multigraph.keySet().stream().skip(random.nextInt(n)).findFirst().get();
                final String v = multigraph.get(u).at(random.nextInt(multigraph.get(u).length));

                //contract the edge
                final String w = "V" + n;
                final var nodesToU = multigraph.remove(u).where(node -> !Objects.equals(node, v));
                final var nodesToV = multigraph.remove(v).where(node -> !Objects.equals(node, u));
                nodesToU.each(node -> multigraph.put(node, multigraph.get(node).replaceAll(u, w)));
                nodesToV.each(node -> multigraph.put(node, multigraph.get(node).replaceAll(v, w)));
                multigraph.put(w, nodesToU.addSeq(nodesToV));

                //merge the two components
                componentSize.put(w, firstNonNull(componentSize.remove(u), 1) + firstNonNull(componentSize.remove(v), 1));
            }

            final int k = multigraph.values().iterator().next().length;
            Out.print("%3d: %s**%s** ---> %s\n", attempt + 1, k == 3 ? "@G" : "@R", k, componentSize.values().stream().map(String::valueOf).collect(Collectors.joining("x")));

            //we know the k-edge connectivity is 3
            if (k == 3) return componentSize.values().stream().reduce(1, (a, b) -> a * b);
        }
    }
}
