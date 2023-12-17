package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestD17 {
    final String SAMPLE = """
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

    @Test
    void sampleStar1() {
        assertThat(new D17(SAMPLE).star1()).isEqualTo(102L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D17(SAMPLE).star2()).isEqualTo(0L);
    }

    @Test
    void star1() {
        assertThat(new D17(Str.testResource("D17.txt")).star1()).isEqualTo(0L);
    }

    @Test
    void star2() {
        assertThat(new D17(Str.testResource("D17.txt")).star2()).isEqualTo(0L);
    }

}
