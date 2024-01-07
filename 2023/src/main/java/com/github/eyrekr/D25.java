package com.github.eyrekr;

import com.github.eyrekr.common.Just;
import com.github.eyrekr.common.Numbered;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.output.Out;
import org.apache.commons.lang3.StringUtils;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Map;

/**
 * https://adventofcode.com/2023/day/25
 * 1)
 * 2)
 */
class D25 extends AoC {

    final SecureRandom random = Just.get(SecureRandom::getInstanceStrong);
    final int n;
    final int[][] adjacency;

    D25(final String input) {
        super(input);
        final Seq<Seq<String>> adjacencyList = lines.map(line -> StringUtils.split(line, " :")).map(Seq::fromArray);
        final Map<String, Integer> map = adjacencyList.flatMap(seq -> seq).unique().numbered().toMap(Numbered::value, Numbered::i);

        this.n = map.size();
        this.adjacency = new int[n][n];
        adjacencyList.each(seq -> {
            final int i = map.get(seq.value);
            seq.tail.map(map::get).each(j -> adjacency[i][j] = adjacency[j][i] = 1);
        });
    }

    @Override
    long star1() {
        return karger();
    }

    @Override
    long star2() {
        return 0L;
    }

    int karger() {
        for (int attempt = 0; ; attempt++) {
            //work on a copy of the adjacency matrix
            final int[][] a = new int[n][n];
            int numberOfEdges = 0;
            for (int i = 0; i < n * n; i++) {
                a[i % n][i / n] = adjacency[i % n][i / n];
                numberOfEdges += adjacency[i % n][i / n];
            }

            //remember the sizes of the components
            final int[] componentSize = new int[n];
            Arrays.fill(componentSize, 1);

            //repeat random edge contraction until there are only 2 vertices left
            for (int step = 0; step < n - 2; step++) {
                //pick random edge
                int edge = random.nextInt(numberOfEdges), k = 0;
                while (true) {
                    edge -= a[k % n][k / n];
                    if (edge < 0) break;
                    k++;
                }
                final int i = k % n, j = k / n;
                //merge the two components
                componentSize[i] += componentSize[j];
                componentSize[j] = 0;

                //contract the edge
                numberOfEdges = numberOfEdges - a[i][j] - a[j][i]; //total number of edges decreases by the number of edges between the 2 nodes
                for (int l = 0; l < n; l++) {
                    a[i][l] = (i == l || j == l) ? 0 : a[i][l] + a[j][l];
                    a[l][i] = a[i][l];
                    a[j][l] = a[l][j] = 0;
                }

                //debug
                //Out.print("Step @r**%d**@@ that contracted edge (@c%d@@, @c%d@@) with @r**%d**@@ edges left\n", step, i, j, numberOfEdges);
                //print(a);
            }
            Out.print("%3d: %s**%s** --->", attempt + 1, numberOfEdges / 2 == 3 ? "@G" : "@R", numberOfEdges / 2);
            int answer = 1;
            for (int l = 0; l < n; l++)
                if (componentSize[l] > 0) {
                    Out.print(" %d", componentSize[l]);
                    answer = answer * componentSize[l];
                }
            Out.print("\n");

            //we know the k-edge connectivity is 3
            if (numberOfEdges / 2 == 3) return answer;
        }
    }

    void print(final int[][] a) {
        final int n = a.length;
        for (int i = 0; i < n * n; i++)
            Out.print("%s%2d%s", a[i % n][i / n] == 0 ? "@W" : "@k", a[i % n][i / n], i % n == n - 1 ? "\n" : " ");
    }
}
