package com.github.eyrekr.y2025;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D08Test {

    final String sample1 = """
            162,817,812
            57,618,57
            906,360,560
            592,479,940
            352,342,300
            466,668,158
            542,29,236
            431,825,988
            739,650,466
            52,470,668
            216,146,977
            819,987,18
            117,168,530
            805,96,715
            346,949,466
            970,615,88
            941,993,340
            862,61,35
            984,92,344
            425,690,689
            """;

    final String input = Out.testResource("2025/D08.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D08(sample1).star1(10)).isEqualTo(40L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D08(input).star1(1000)).isEqualTo(46398L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D08(sample1).star2()).isEqualTo(25272L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D08(input).star2()).isEqualTo(8141888143L);
    }

}
