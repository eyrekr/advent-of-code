package com.github.eyrekr;

import com.github.eyrekr.util.Grid;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * https://adventofcode.com/2023/day/16
 * 1) 7517
 * 2) 7741
 */
class D16 extends AoC {

    final static Instr[] INSTRUCTIONS = new Instr[]{
            new Instr('-', Grid.D.D, Grid.D.L),
            new Instr('-', Grid.D.D, Grid.D.R),
            new Instr('-', Grid.D.U, Grid.D.L),
            new Instr('-', Grid.D.U, Grid.D.R),
            new Instr('|', Grid.D.L, Grid.D.U),
            new Instr('|', Grid.D.L, Grid.D.D),
            new Instr('|', Grid.D.R, Grid.D.U),
            new Instr('|', Grid.D.R, Grid.D.D),
            new Instr('\\', Grid.D.L, Grid.D.U),
            new Instr('\\', Grid.D.R, Grid.D.D),
            new Instr('\\', Grid.D.U, Grid.D.L),
            new Instr('\\', Grid.D.D, Grid.D.R),
            new Instr('/', Grid.D.L, Grid.D.D),
            new Instr('/', Grid.D.R, Grid.D.U),
            new Instr('/', Grid.D.U, Grid.D.R),
            new Instr('/', Grid.D.D, Grid.D.L),
    };

    final Grid grid;

    D16(final String input) {
        super(input);
        grid = Grid.of(input);
    }

    long star1() {
        return calculate(new Step(0, 0, Grid.D.R));
    }

    long star2() {
        long max = 0L;
        for (int x = 0; x < grid.m; x++) {
            max = Math.max(max, calculate(new Step(x, 0, Grid.D.D)));
            max = Math.max(max, calculate(new Step(x, grid.n - 1, Grid.D.U)));
        }
        for (int y = 0; y < grid.n; y++) {
            max = Math.max(max, calculate(new Step(0, y, Grid.D.R)));
            max = Math.max(max, calculate(new Step(grid.m - 1, grid.n - 1, Grid.D.L)));
        }
        return max;
    }

    long calculate(final Step start) {
        final Grid mask = Grid.of(grid.m, grid.n);
        final LinkedList<Step> buffer = new LinkedList<>();
        buffer.addFirst(start);
        final Set<Step> processed = new HashSet<>();
        while (!buffer.isEmpty()) {
            final Step step = buffer.removeFirst();
            if (processed.contains(step)) continue;
            processed.add(step); // prevent infinite cycles
            mask.a[step.x][step.y] = '#'; // energize
            final Grid.It it = grid.it(step.x, step.y);
            boolean justPass = true;
            for (final var instr : INSTRUCTIONS) {
                if (instr.ch == it.ch && instr.in == step.d) {
                    it.constrainedTo(instr.out).ifPresent(next -> buffer.addLast(new Step(next.x, next.y, instr.out)));
                    justPass = false;
                }
            }
            if (justPass) it.constrainedTo(step.d).ifPresent(next -> buffer.addLast(new Step(next.x, next.y, step.d)));
        }
        return mask.chWhere(ch -> ch == '#').length;
    }

    record Instr(char ch, Grid.D in, Grid.D out) {
    }

    record Step(int x, int y, Grid.D d) {
    }
}