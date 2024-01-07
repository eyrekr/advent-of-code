package com.github.eyrekr;

import com.github.eyrekr.mutable.Grid;
import com.github.eyrekr.output.Out;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D14Test {
    final String sample = """
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
    final String input = Out.testResource("D14.txt");

    @Test
    void sampleStar1() {
        assertThat(new D14(sample).star1()).isEqualTo(136L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D14(sample).star2()).isEqualTo(64L);
    }

    @Test
    void star1() {
        assertThat(new D14(input).star1()).isEqualTo(106186L);
    }

    @Test
    void star2() {
        assertThat(new D14(input).star2()).isEqualTo(106390L);
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
        final Grid actual = afterCycles(Grid.of(sample), 1);
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
        final Grid actual = afterCycles(Grid.of(sample), 2);
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
        final Grid actual = afterCycles(Grid.of(sample), 3);
        assertThat(actual).isEqualTo(expected);
        assertThat(D14.score(actual)).isEqualTo(69);
    }

    private Grid afterCycles(final Grid grid, final int n) {
        Grid result = grid;
        for (int i = 0; i < n; i++) {
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
