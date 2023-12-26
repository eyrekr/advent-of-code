package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D10Test {
    final String sample = """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ
            """;

    final String sample2A = """
            .F----7F7F7F7F-7....
            .|F--7||||||||FJ....
            .||.FJ||||||||L7....
            FJL7L7LJLJ||LJ.L-7..
            L--J.L7...LJS7F-7L7.
            ....F-J..F7FJ|L7L7L7
            ....L7.F7||L7|.L7L7|
            .....|FJLJ|FJ|F7|.LJ
            ....FJL-7.||.||||...
            ....L---J.LJ.LJLJ...
            """;

    final  String sample2B = """
            FF7FSF7F7F7F7F7F---7
            L|LJ||||||||||||F--J
            FL-7LJLJ||||||LJL-77
            F--JF--7||LJLJ7F7FJ-
            L---JF-JLJ.||-FJLJJ7
            |F|F-JF---7F7-L7L|7|
            |FFJF7L7F-JF7|JL---7
            7-L-JL7||F7|L7F-7F7|
            L.L7LFJ|||||FJL7||LJ
            L7JLJL-JLJLJL--JLJ.L
            """;

    final String input = Str.testResource("D10.txt");

    @Test
    void sampleStar1() {
        assertThat(new D10(sample).star1()).isEqualTo(8L);
    }

    @Test
    void sampleStar2A() {
        assertThat(new D10(sample2A).star2()).isEqualTo(8L);
    }

    @Test
    void sampleStar2B() {
        assertThat(new D10(sample2B).star2()).isEqualTo(10L);
    }

    @Test
    void star1() {
        assertThat(new D10(input).star1()).isEqualTo(6903L);
    }

    @Test
    void star2() {
        assertThat(new D10(input).star2()).isEqualTo(265L);
    }
}
