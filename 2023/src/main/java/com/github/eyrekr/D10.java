package com.github.eyrekr;

import com.github.eyrekr.output.Out;
import com.github.eyrekr.raster.Direction;
import com.github.eyrekr.util.Grid;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.function.Consumer;

/**
 * https://adventofcode.com/2023/day/10
 * 1) 6903
 * 2) 265
 */
class D10 extends AoC {

    static final Map<Character, Character> ASCII = Map.of(
            'S', '*',
            '-', '─',
            '|', '│',
            'F', '┌',
            'J', '┘',
            '7', '┐',
            'L', '└');

    final Grid grid;

    D10(final String input) {
        super(input);
        this.grid = Grid.of(input).replace(ch -> ASCII.getOrDefault(ch, Grid.C0));
    }

    long star1() {
        return traverse(x -> {
        });
    }

    long star2() {
        final char[][] stencil = new char[grid.m][grid.n];
        traverse(it -> stencil[it.x][it.y] = it.ch);
        return area(stencil);
    }

    long area(final char[][] stencil) { // ray casting
        long area = 0;
        for (int y = 0; y < grid.n; y++) {
            boolean inside = false;
            char open = Grid.C0;
            for (int x = 0; x < grid.m; x++) {
                final char ch = stencil[x][y];
                if (ch > 0) { // border
                    Out.print("@w%s", ch);
                    if (ch == '─') {
                        //no change
                    } else if (ch == '│') {
                        inside = !inside;
                    } else if (open == '┌' && ch == '┘') {
                        // no change
                        open = '\0';
                    } else if (open == '└' && ch == '┐') {
                        // no change
                        open = '\0';
                    } else {
                        inside = !inside;
                        open = ch;
                    }
                } else if (inside) { // empty space inside
                    Out.print("@R**X**");
                    area++;
                } else { // empty space outside
                    Out.print(" ");
                }
            }
            Out.print("\n");
        }
        return area;
    }

    long traverse(final Consumer<Grid.It> process) {
        final Grid.It start = grid.first('*');
        State state = new State(start, Direction.Up, 0);
        while (true) {
            process.accept(state.it);
            if (state.it.ch == '*' && state.length > 0) {
                return state.length / 2;
            }
            state = state.next();
        }
    }

    record State(Grid.It it, Direction in, int length) {
        State next() {
            final Direction out = switch (it.ch) {
                case '*' -> StringUtils.contains("│┌┐", it.neighbours4[0]) ? Direction.Up
                        : StringUtils.contains("│└┘", it.neighbours4[3]) ? Direction.Down
                        : Direction.Right;
                case '│', '─' -> in;
                case '┌' -> in == Direction.Left ? Direction.Down : Direction.Right;
                case '┐' -> in == Direction.Up ? Direction.Left : Direction.Down;
                case '┘' -> in == Direction.Down ? Direction.Left : Direction.Up;
                case '└' -> in == Direction.Down ? Direction.Right : Direction.Up;
                default -> throw new IllegalStateException(toString());
            };
            return new State(it.go(out), out, length + 1);
        }
    }
}
