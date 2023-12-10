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

    static final Map<Character, Character> NICER = Map.of(
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
        this.grid = Grid.of(lines).map(it -> NICER.getOrDefault(it.ch, Grid.C0)).print();
    }

    long star1() {
        return traverse(ignored -> {
        });
    }

    long star2() {
        final char[][] xx = new char[grid.n][grid.m];

        traverse(state -> xx[state.it.y][state.it.x] = state.it.ch);
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
        final Grid.It start = grid.first(it -> it.ch == '*');
        State state = new State(start, D.U, 0);
        while (state != null) {
            process.accept(state);
            if (state.it.ch == '*' && state.l > 0) {
                return state.l / 2;
            }
            state = state.next();
        }
        return 0L;
    }

    record State(Grid.It it, D direction, int l) {
        State next() {
            final D newDirection = switch (it.ch) {
                case '*' -> StringUtils.contains("│┌┐", it.neighbours4[0]) ? D.U
                        : StringUtils.contains("│└┘", it.neighbours4[3]) ? D.D
                        : D.R;
                case '│' -> direction;
                case '─' -> direction;
                case '┌' -> direction == D.L ? D.D : D.R;
                case '┐' -> direction == D.U ? D.L : D.D;
                case '┘' -> direction == D.D ? D.L : D.U;
                case '└' -> direction == D.D ? D.R : D.U;
                default -> throw new IllegalStateException(toString());
            };
            return new State(it.to(newDirection), newDirection, l + 1);
        }
    }
}
