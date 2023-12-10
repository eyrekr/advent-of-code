package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Grid.D;
import com.github.eyrekr.util.Str;

import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;

/**
 * https://adventofcode.com/2023/day/10
 * 1) 6903
 * 2) 265
 */
class D10 extends AoC {

    final Grid grid;
    final Grid.It start;

    D10(final String input) {
        super(input);
        this.grid = Grid.of(lines);
        this.start = grid.first(it->it.ch == 'S');
    }

    long star1() {
        return traverse(null);
    }

    long star2() {
        final char[][] xx = new char[grid.n][grid.m];
        final Map<Character, Character> T = Map.of(
                'S', '*',
                '-', '─',
                '|', '│',
                'F', '┌',
                'J', '┘',
                '7', '┐',
                'L', '└'
        );
        traverse(p -> xx[p.y][p.x] = T.getOrDefault(grid.at(p.x, p.y), '\0'));
        long area = 0;
        for (int y = 0; y < grid.n; y++) {
            boolean inside = false;
            char open = '\0';
            for (int x = 0; x < grid.m; x++) {
                final char ch = xx[y][x];
                if (ch > 0) { // border
                    Str.print("@w%s", ch);
                    if(ch=='─') {
                        //no change
                    } else if(ch == '│') {
                        inside = !inside;
                    } else if(open == '┌' && ch == '┘') {
                        // no change
                        open = '\0';
                    } else if(open == '└' && ch == '┐' ) {
                        // no change
                        open = '\0';
                    } else {
                        inside = !inside;
                        open = ch;
                    }
                } else if (inside) { // empty space inside
                    Str.print("@R**X**");
                    area++;
                } else { // empty space outside
                    Str.print(" ");
                }
            }
            Str.print("\n");
        }
        return area;
    }

    private long traverse(Consumer<State> process) {
        final LinkedList<State> path = new LinkedList<>();
        if (start.y - 1 >= 0 && "|F7".contains("" + grid.at(start.x, start.y - 1))) {
            path.addFirst(new State(start.x, start.y - 1, D.U, 1));
        } else if ((start.y + 1 < grid.n) && "|JL".contains("" + grid.at(start.x, start.y + 1))) {
            path.addFirst(new State(start.x, start.y + 1, D.D, 1));
        } else if ((start.x - 1 >= 0) && "-LF".contains("" + grid.at(start.x - 1, start.y))) {
            path.addFirst(new State(start.x - 1, start.y, D.L, 1));
        } else if ((start.x + 1 < grid.m) && "-7J".contains("" + grid.at(start.x + 1, start.y))) {
            path.addFirst(new State(start.x + 1, start.y, D.R, 1));
        }
        while (!path.isEmpty()) {
            final State state = path.removeFirst();
            if (process != null) process.accept(state);
            final char ch = grid.at(state.x, state.y);
            if (ch == 'S') { //back at start
                return state.l / 2;
            }
            final State next = switch (ch) {
                case '|' -> state.d == D.U
                        ? new State(state.x, state.y - 1, D.U, state.l + 1)
                        : new State(state.x, state.y + 1, D.D, state.l + 1);
                case '-' -> state.d == D.R
                        ? new State(state.x + 1, state.y, D.R, state.l + 1)
                        : new State(state.x - 1, state.y, D.L, state.l + 1);
                case 'F' -> state.d == D.L
                        ? new State(state.x, state.y + 1, D.D, state.l + 1)
                        : new State(state.x + 1, state.y, D.R, state.l + 1);
                case '7' -> state.d == D.U
                        ? new State(state.x - 1, state.y, D.L, state.l + 1)
                        : new State(state.x, state.y + 1, D.D, state.l + 1);
                case 'J' -> state.d == D.D
                        ? new State(state.x - 1, state.y, D.L, state.l + 1)
                        : new State(state.x, state.y - 1, D.U, state.l + 1);
                case 'L' -> state.d == D.D
                        ? new State(state.x + 1, state.y, D.R, state.l + 1)
                        : new State(state.x, state.y - 1, D.U, state.l + 1);
                default -> throw new IllegalStateException(state.toString());
            };
            path.addLast(next);
        }
        return 0L;
    }

    record State(int x, int y, D d, int l) {
        State over(final char ch) {
            return switch (ch) {
                case '|' -> d == D.U ? new State(x, y - 1, D.U, l + 1) : new State(x, y + 1, D.D, l + 1);
                case '-' -> d == D.R ? new State(x + 1, y, D.R, l + 1) : new State(x - 1, y, D.L, l + 1);
                case 'F' -> d == D.L ? new State(x, y + 1, D.D, l + 1) : new State(x + 1,y, D.R, l + 1);
                case '7' -> d == D.U ? new State(x - 1, y, D.L, l + 1) : new State(x, y + 1, D.D, l + 1);
                case 'J' -> d == D.D ? new State(x - 1, y, D.L, l + 1) : new State(x, y - 1, D.U, l + 1);
                case 'L' -> d == D.D ? new State(x + 1, y, D.R, l + 1) : new State(x, y - 1, D.U, l + 1);
                default -> throw new IllegalStateException(toString());
            };
        }
    }

}
