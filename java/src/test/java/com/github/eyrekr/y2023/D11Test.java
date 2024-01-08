package com.github.eyrekr.y2023;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D11Test {
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
    final String input = Out.testResource("2023/D11.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D11(sample).star1()).isEqualTo(374L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D11(sample).star2()).isEqualTo(82000210L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D11(input).star1()).isEqualTo(9509330L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D11(input).star2()).isEqualTo(635832237682L);
    }
}
