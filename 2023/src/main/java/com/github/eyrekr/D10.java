package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Grid.D;
import com.github.eyrekr.util.Str;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.function.Consumer;

/**
 * https://adventofcode.com/2023/day/10
 * 1) 6903
 * 2) 265
 */
class D10 extends AoC {

    final Grid grid;

    D10(final String input) {
        super(input);
        this.grid = Grid.of(lines);
    }

    long star1() {
        return traverse(ignored -> {
        });
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
        traverse(state -> xx[state.it.y][state.it.x] = T.getOrDefault(state.it.ch, '\0'));
        long area = 0;
        for (int y = 0; y < grid.n; y++) {
            boolean inside = false;
            char open = '\0';
            for (int x = 0; x < grid.m; x++) {
                final char ch = xx[y][x];
                if (ch > 0) { // border
                    Str.print("@w%s", ch);
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
        final Grid.It s = grid.first(it -> it.ch == 'S');
        State state = new State(s, D.U, 0);
        while (state != null) {
            process.accept(state);
            if (state.it.ch == 'S' && state.l > 0) {
                return state.l / 2;
            }
            state = state.next();
        }
        return 0L;
    }

    record State(Grid.It it, D direction, int l) {
        State next() {
            final D newDirection = switch (it.ch) {
                case 'S' -> StringUtils.contains("|F7", it.neighbours4[0]) ? D.U
                        : StringUtils.contains("|JL", it.neighbours4[3]) ? D.D
                        : StringUtils.contains("-LF", it.neighbours4[1]) ? D.L
                        : D.R;
                case '|' -> direction;
                case '-' -> direction;
                case 'F' -> direction == D.L ? D.D : D.R;
                case '7' -> direction == D.U ? D.L : D.D;
                case 'J' -> direction == D.D ? D.L : D.U;
                case 'L' -> direction == D.D ? D.R : D.U;
                default -> throw new IllegalStateException(toString());
            };
            return new State(it.to(newDirection), newDirection, l + 1);
        }
    }

}
