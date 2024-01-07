package com.github.eyrekr;

import com.github.eyrekr.common.Numbered;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.output.Out;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * https://adventofcode.com/2023/day/25
 * 1)
 * 2)
 */
class D25 extends AoC {

    final int n;
    final int[][] a;

    D25(final String input) {
        super(input);
        final Seq<Seq<String>> adjacencyList = lines.map(line -> StringUtils.split(line, " :")).map(Seq::fromArray);
        final Map<String, Integer> map = adjacencyList.flatMap(seq -> seq).unique().numbered().toMap(Numbered::value, Numbered::i);

        this.n = map.size();
        this.a = new int[n][n];
        adjacencyList.each(seq -> {
            final int i = map.get(seq.value);
            seq.tail.map(map::get).each(j -> a[i][j] = a[j][i] = 1);
        });
    }

    @Override
    long star1() {
        print();
        return 0L;
    }

    @Override
    long star2() {
        return 0L;
    }

    void print() {
        Out.print("@g%d\n", n);
        for (int i = 0; i < n * n; i++) Out.print("%2d%s", a[i % n][i / n], i % n == n-1 ? "\n" : " ");
    }
}
