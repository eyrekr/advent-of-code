package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class D15Test extends AocTest {

    D15Test() {
        constructor(D15::new);
        sample("""
                ##########
                #..O..O.O#
                #......O.#
                #.OO..O.O#
                #..O@..O.#
                #O#..O...#
                #O..O..O.#
                #.OO.O.OO#
                #....O...#
                ##########
                
                <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
                vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
                ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
                <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
                ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
                ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
                >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
                <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
                ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
                v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
                """);

        star1(10092L, 1476771L);
        star2(9021L, 0L);
    }

    @Test
    void twiceAsWide() {
        assertThat(
                D15.twiceAsWide("""
                        ##########
                        #..O..O.O#
                        #......O.#
                        #.OO..O.O#
                        #..O@..O.#
                        #O#..O...#
                        #O..O..O.#
                        #.OO.O.OO#
                        #....O...#
                        ##########
                        """))
                .as("grid twice as wide")
                .isEqualTo("""
                        ####################
                        ##....[]....[]..[]##
                        ##............[]..##
                        ##..[][]....[]..[]##
                        ##....[]@.....[]..##
                        ##[]##....[]......##
                        ##[]....[]....[]..##
                        ##..[][]..[]..[][]##
                        ##........[]......##
                        ####################
                        """);
    }

    @Test
    void mini() {
        new D15("""
                #######
                #...#.#
                #.....#
                #..OO@#
                #..O..#
                #.....#
                #######
                
                <vv<<^^<<^^
                """).star2();
    }
}
