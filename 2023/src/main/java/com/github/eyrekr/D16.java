package com.github.eyrekr;

import com.github.eyrekr.util.Grid;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * https://adventofcode.com/2023/day/16
 * 1) 7517
 * 2)
 */
class D16 extends AoC {

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
        for(int x = 0; x<grid.m;x++) {
            max = Math.max(max, calculate(new Step(x, 0, Grid.D.D)));
            max = Math.max(max, calculate(new Step(x, grid.n-1, Grid.D.U)));
        }
        for(int y = 0; y<grid.n;y++) {
            max = Math.max(max, calculate(new Step(0, y, Grid.D.R)));
            max = Math.max(max, calculate(new Step(grid.m-1, grid.n-1, Grid.D.L)));
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
            switch (it.ch) {
                case '.' ->
                        it.constrainedTo(step.d).ifPresent(next -> buffer.addLast(new Step(next.x, next.y, step.d)));
                case '-' -> {
                    if (step.d == Grid.D.L || step.d == Grid.D.R) {
                        it.constrainedTo(step.d).ifPresent(next -> buffer.addLast(new Step(next.x, next.y, step.d)));
                    } else {
                        it.constrainedTo(Grid.D.L).ifPresent(left -> buffer.addLast(new Step(left.x, left.y, Grid.D.L)));
                        it.constrainedTo(Grid.D.R).ifPresent(right -> buffer.addLast(new Step(right.x, right.y, Grid.D.R)));
                    }
                }
                case '|' -> {
                    if (step.d == Grid.D.U || step.d == Grid.D.D) {
                        it.constrainedTo(step.d).ifPresent(next -> buffer.addLast(new Step(next.x, next.y, step.d)));
                    } else {
                        it.constrainedTo(Grid.D.U).ifPresent(up -> buffer.addLast(new Step(up.x, up.y, Grid.D.U)));
                        it.constrainedTo(Grid.D.D).ifPresent(down -> buffer.addLast(new Step(down.x, down.y, Grid.D.D)));
                    }
                }
                case '\\' -> {
                    switch (step.d) {
                        case U ->
                                it.constrainedTo(Grid.D.L).ifPresent(left -> buffer.addLast(new Step(left.x, left.y, Grid.D.L)));
                        case D ->
                                it.constrainedTo(Grid.D.R).ifPresent(right -> buffer.addLast(new Step(right.x, right.y, Grid.D.R)));
                        case R ->
                                it.constrainedTo(Grid.D.D).ifPresent(down -> buffer.addLast(new Step(down.x, down.y, Grid.D.D)));
                        case L ->
                                it.constrainedTo(Grid.D.U).ifPresent(up -> buffer.addLast(new Step(up.x, up.y, Grid.D.U)));
                    }
                }
                case '/' -> {
                    switch (step.d) {
                        case U ->
                                it.constrainedTo(Grid.D.R).ifPresent(right -> buffer.addLast(new Step(right.x, right.y, Grid.D.R)));
                        case D ->
                                it.constrainedTo(Grid.D.L).ifPresent(left -> buffer.addLast(new Step(left.x, left.y, Grid.D.L)));
                        case R ->
                                it.constrainedTo(Grid.D.U).ifPresent(up -> buffer.addLast(new Step(up.x, up.y, Grid.D.U)));
                        case L ->
                                it.constrainedTo(Grid.D.D).ifPresent(down -> buffer.addLast(new Step(down.x, down.y, Grid.D.D)));
                    }
                }
            }
        }
        return mask.chWhere(ch -> ch == '#').length;
    }

    record Step(int x, int y, Grid.D d) {
    }
}