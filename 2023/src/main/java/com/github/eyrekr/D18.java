package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;

/**
 * https://adventofcode.com/2023/day/18
 * 1)
 * 2)
 */
class D18 extends AoC {

    final Seq<Instr> instructions;

    D18(final String input) {
        super(input);
        this.instructions = lines.map(Instr::of);
    }

    long star1() {
        final Dim size = Dim.from(instructions);
        final int n = size.y1 - size.y0 + 3;
        final int m = size.x1 - size.x0 + 3;
        Str.print("[%d,%d]->[%d,%d] => @c%d@@x@c%d@@\n", size.x0, size.y0, size.x1, size.y1, m, n);

        final char[][] a = new char[m][n];
        {//dig
            int x = -size.x0 + 1, y = -size.y0 + 1;
            a[x][y] = '#';
            for (final var i : instructions) {
                switch (i.d) {
                    case 'U' -> {
                        for (int j = 0; j < i.l; j++) a[x][y - j] = '#';
                        y = y - i.l;
                    }
                    case 'D' -> {
                        for (int j = 0; j < i.l; j++) a[x][y + j] = '#';
                        y = y + i.l;
                    }
                    case 'L' -> {
                        for (int j = 0; j < i.l; j++) a[x - j][y] = '#';
                        x = x - i.l;
                    }
                    case 'R' -> {
                        for (int j = 0; j < i.l; j++) a[x + j][y] = '#';
                        x = x + i.l;
                    }
                }
            }
        }

        {//flood fill
            final var queue = new LinkedList<int[]>();
            queue.addFirst(new int[]{0, 0});
            while (!queue.isEmpty()) {
                final var p = queue.removeFirst();
                final int x = p[0], y = p[1];
                if (x >= 0 && y >= 0 && x < m && y < n && a[x][y] == '\0') {
                    a[x][y] = 'X';
                    queue.addLast(new int[]{x - 1, y});
                    queue.addLast(new int[]{x + 1, y});
                    queue.addLast(new int[]{x, y - 1});
                    queue.addLast(new int[]{x, y + 1});
                }
            }
            //invert
            for (int y = 0; y < a[0].length; y++)
                for (int x = 0; x < a.length; x++)
                    a[x][y] = switch (a[x][y]) {
                        case '#' -> '#';
                        case 'X' -> '\0';
                        default -> 'X';
                    };
        }
        print(a);

        long sum = 0L;
        for (int y = 0; y < a[0].length; y++)
            for (int x = 0; x < a.length; x++)
                if(a[x][y] == '#' || a[x][y] == 'X') sum++;

        Str.print("@c%d\n", sum);
        return sum;
    }

    long star2() {
        return 0L;
    }


    void print(char[][] a) {
        for (int y = 0; y < a[0].length; y++) {
            for (char[] chars : a) {
                Str.print("%s",
                        switch (chars[y]) {
                            case '#' -> "@r#@@";
                            case 'X' -> "@gX@@";
                            default -> ".";
                        });
            }
            Str.print("\n");
        }
    }

    record Dim(int x0, int y0, int x1, int y1) {
        Dim absorb(final int x, final int y) {
            return new Dim(Math.min(x0, x), Math.min(y0, y), Math.max(x1, x), Math.max(y1, y));
        }

        static Dim from(final Seq<Instr> instructions) {
            Dim d = new Dim(0, 0, 0, 0);
            int x = 0, y = 0;
            for (final var i : instructions) {
                switch (i.d) {
                    case 'U' -> y = y - i.l;
                    case 'D' -> y = y + i.l;
                    case 'L' -> x = x - i.l;
                    case 'R' -> x = x + i.l;
                }
                d = d.absorb(x, y);
            }
            return d;
        }
    }

    record Instr(char d, int l, String color) {
        static Instr of(final String input) {
            final var data = StringUtils.split(input, " ()");
            return new Instr(data[0].charAt(0), Integer.parseInt(data[1]), data[2]);
        }
    }
}