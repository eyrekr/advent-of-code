package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestD11 {
    final String sample = """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
            """;
    final String input = Str.testResource("D11.txt");

    @Test
    void sampleStar1() {
        assertThat(new D11(sample).star1()).isEqualTo(374L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D11(sample).star2()).isEqualTo(82000210L);
    }

    @Test
    void star1() {
        assertThat(new D11(input).star1()).isEqualTo(9509330L);
    }

    @Test
    void star2() {
        assertThat(new D11(input).star2()).isEqualTo(635832237682L);
    }
}
