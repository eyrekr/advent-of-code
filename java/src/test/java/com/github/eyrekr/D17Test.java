package com.github.eyrekr;

import com.github.eyrekr.output.Out;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D17Test {
    final String sample = """
            2413432311323
            3215453535623
            3255245654254
            3446585845452
            4546657867536
            1438598798454
            4457876987766
            3637877979653
            4654967986887
            4564679986453
            1224686865563
            2546548887735
            4322674655533
            """;
    final String input = Out.testResource("D17.txt");

    @Test
    void sampleStar1() {
        assertThat(new D17(sample).star1()).isEqualTo(102L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D17(sample).star2()).isEqualTo(94L);
    }

    @Test
    void star1() {
        assertThat(new D17(input).star1()).isEqualTo(684L);
    }

    @Test
    void star2() {
        assertThat(new D17(input).star2()).isEqualTo(822L);
    }

}
