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

    final static Instruction[] INSTRUCTIONS = new Instruction[]{
            new Instruction('-', Grid.Direction.Down, Grid.Direction.Left),
            new Instruction('-', Grid.Direction.Down, Grid.Direction.Right),
            new Instruction('-', Grid.Direction.Up, Grid.Direction.Left),
            new Instruction('-', Grid.Direction.Up, Grid.Direction.Right),
            new Instruction('|', Grid.Direction.Left, Grid.Direction.Up),
            new Instruction('|', Grid.Direction.Left, Grid.Direction.Down),
            new Instruction('|', Grid.Direction.Right, Grid.Direction.Up),
            new Instruction('|', Grid.Direction.Right, Grid.Direction.Down),
            new Instruction('\\', Grid.Direction.Left, Grid.Direction.Up),
            new Instruction('\\', Grid.Direction.Right, Grid.Direction.Down),
            new Instruction('\\', Grid.Direction.Up, Grid.Direction.Left),
            new Instruction('\\', Grid.Direction.Down, Grid.Direction.Right),
            new Instruction('/', Grid.Direction.Left, Grid.Direction.Down),
            new Instruction('/', Grid.Direction.Right, Grid.Direction.Up),
            new Instruction('/', Grid.Direction.Up, Grid.Direction.Right),
            new Instruction('/', Grid.Direction.Down, Grid.Direction.Left),
    };

    final Grid grid;

    D16(final String input) {
        super(input);
        grid = Grid.of(input);
    }

    long star1() {
        return calculate(new Step(0, 0, Grid.Direction.Right));
    }

    long star2() {
        long max = 0L;
        for (int x = 0; x < grid.m; x++) {
            max = Math.max(max, calculate(new Step(x, 0, Grid.Direction.Down)));
            max = Math.max(max, calculate(new Step(x, grid.n - 1, Grid.Direction.Up)));
        }
        for (int y = 0; y < grid.n; y++) {
            max = Math.max(max, calculate(new Step(0, y, Grid.Direction.Right)));
            max = Math.max(max, calculate(new Step(grid.m - 1, grid.n - 1, Grid.Direction.Left)));
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
                if (instr.ch == it.ch && instr.in == step.direction) {
                    it.tryToGo(instr.out).ifPresent(next -> buffer.addLast(new Step(next.x, next.y, instr.out)));
                    justPass = false;
                }
            }
            if (justPass) it.tryToGo(step.direction).ifPresent(next -> buffer.addLast(new Step(next.x, next.y, step.direction)));
        }
        return mask.chWhere(ch -> ch == '#').length;
    }

    record Instruction(char ch, Grid.Direction in, Grid.Direction out) {
    }

    record Step(int x, int y, Grid.Direction direction) {
    }
}