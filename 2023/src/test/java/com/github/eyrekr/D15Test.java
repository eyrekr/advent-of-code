package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D15Test {
    final String sample = "rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7";
    final String input = Str.testResource("D15.txt");

    @Test
    void sampleStar1() {
        assertThat(new D15(sample).star1()).isEqualTo(1320L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D15(sample).star2()).isEqualTo(145L);
    }

    @Test
    void star1() {
        assertThat(new D15(input).star1()).isEqualTo(515495L);
    }

    @Test
    void star2() {
        assertThat(new D15(input).star2()).isEqualTo(229349L);
    }

}
