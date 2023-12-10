package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Str;

import java.util.LinkedList;
import java.util.Map;
import java.util.function.Consumer;

/**
 * https://adventofcode.com/2023/day/10
 * 1)
 * 2)
 */
class D10 extends AoC {

    final Grid grid;
    final Grid.It start;

    D10(final String input) {
        super(input);
        this.grid = Grid.of(lines);
        this.start = findStart();
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

    Grid.It findStart() {
        for (final Grid.It it : grid) {
            if (it.ch == 'S') {
                return it;
            }
        }
        return null;
    }

    private long traverse(Consumer<P> process) {
        final LinkedList<P> path = new LinkedList<>();
        if (start.y - 1 >= 0 && "|F7".contains("" + grid.at(start.x, start.y - 1))) {
            path.addFirst(new P(start.x, start.y - 1, D.Up, 1));
        } else if ((start.y + 1 < grid.n) && "|JL".contains("" + grid.at(start.x, start.y + 1))) {
            path.addFirst(new P(start.x, start.y + 1, D.Down, 1));
        } else if ((start.x - 1 >= 0) && "-LF".contains("" + grid.at(start.x - 1, start.y))) {
            path.addFirst(new P(start.x - 1, start.y, D.Left, 1));
        } else if ((start.x + 1 < grid.m) && "-7J".contains("" + grid.at(start.x + 1, start.y))) {
            path.addFirst(new P(start.x + 1, start.y, D.Right, 1));
        }
        while (!path.isEmpty()) {
            final P p = path.removeFirst();
            if (process != null) process.accept(p);
            final char ch = grid.at(p.x, p.y);
            if (ch == 'S') { //back at start
                return p.l / 2;
            }
            final P next = switch (ch) {
                case '|' -> p.d == D.Up
                        ? new P(p.x, p.y - 1, D.Up, p.l + 1)
                        : new P(p.x, p.y + 1, D.Down, p.l + 1);
                case '-' -> p.d == D.Right
                        ? new P(p.x + 1, p.y, D.Right, p.l + 1)
                        : new P(p.x - 1, p.y, D.Left, p.l + 1);
                case 'F' -> p.d == D.Left
                        ? new P(p.x, p.y + 1, D.Down, p.l + 1)
                        : new P(p.x + 1, p.y, D.Right, p.l + 1);
                case '7' -> p.d == D.Up
                        ? new P(p.x - 1, p.y, D.Left, p.l + 1)
                        : new P(p.x, p.y + 1, D.Down, p.l + 1);
                case 'J' -> p.d == D.Down
                        ? new P(p.x - 1, p.y, D.Left, p.l + 1)
                        : new P(p.x, p.y - 1, D.Up, p.l + 1);
                case 'L' -> p.d == D.Down
                        ? new P(p.x + 1, p.y, D.Right, p.l + 1)
                        : new P(p.x, p.y - 1, D.Up, p.l + 1);
                default -> throw new IllegalStateException(p.toString());
            };
            path.addLast(next);
        }
        return 0L;
    }

    enum D {Up, Down, Left, Right}

    record P(int x, int y, D d, int l) {
    }

}
