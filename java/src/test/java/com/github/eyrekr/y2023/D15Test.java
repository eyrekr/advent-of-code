package com.github.eyrekr.y2023;

import com.github.eyrekr.output.Out;
import com.github.eyrekr.y2023.D15;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D15Test {
    final String sample = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";
    final String input = Out.testResource("D15.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D15(sample).star1()).isEqualTo(1320L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D15(sample).star2()).isEqualTo(145L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D15(input).star1()).isEqualTo(515495L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D15(input).star2()).isEqualTo(229349L);
    }

}
