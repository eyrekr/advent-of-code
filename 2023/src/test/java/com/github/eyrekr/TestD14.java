package com.github.eyrekr;

import com.github.eyrekr.util.Grid;
import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestD14 {
    final String SAMPLE = """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
            """;

    @Test
    void sampleStar1() {
        assertThat(new D14(SAMPLE).star1()).isEqualTo(136L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D14(SAMPLE).star2()).isEqualTo(64L);
    }

    @Test
    void star1() {
        assertThat(new D14(Str.testResource("D14.txt")).star1()).isEqualTo(106186L);
    }

    @Test
    void star2() {
        assertThat(new D14(Str.testResource("D14.txt")).star2()).isEqualTo(0L);
    }

    @Test
    void testAfter1Cycle() {
        final Grid expected = Grid.of("""
                .....#....
                ....#...O#
                ...OO##...
                .OO#......
                .....OOO#.
                .O#...O#.#
                ....O#....
                ......OOOO
                #...O###..
                #..OO#....
                """);
        final Grid actual = afterCycles(Grid.of(SAMPLE), 1);
        assertThat(actual).isEqualTo(expected);
        assertThat(D14.score(actual)).isEqualTo(87);
    }

    @Test
    void testAfter2Cycles() {
        final Grid expected = Grid.of("""
                .....#....
                ....#...O#
                .....##...
                ..O#......
                .....OOO#.
                .O#...O#.#
                ....O#...O
                .......OOO
                #..OO###..
                #.OOO#...O
                """);
        final Grid actual = afterCycles(Grid.of(SAMPLE), 2);
        assertThat(actual).isEqualTo(expected);
        assertThat(D14.score(actual)).isEqualTo(69);
    }

    @Test
    void testAfter3Cycles() {
        final Grid expected = Grid.of("""
                .....#....
                ....#...O#
                .....##...
                ..O#......
                .....OOO#.
                .O#...O#.#
                ....O#...O
                .......OOO
                #...O###.O
                #.OOO#...O
                """);
        final Grid actual = afterCycles(Grid.of(SAMPLE), 3);
        assertThat(actual).isEqualTo(expected);
        assertThat(D14.score(actual)).isEqualTo(69);
    }

    private Grid afterCycles(final Grid grid, final int n) {
        Grid result = grid;
        for(int i = 0; i < n; i++) {
            result = D14.cycle(result);
        }
        return result;
    }

    @Test
    void testScore() {
        final Grid grid = Grid.of("""
                OOOO.#.O..
                OO..#....#
                OO..O##..O
                O..#.OO...
                ........#.
                ..#....#.#
                ..O..#.O.O
                ..O.......
                #....###..
                #....#....
                """);
        assertThat(D14.score(grid)).isEqualTo(136L);
    }
}
