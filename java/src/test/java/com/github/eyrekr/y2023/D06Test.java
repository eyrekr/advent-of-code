package com.github.eyrekr.y2023;

import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.y2023.D06;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class D06Test {
    final Seq<D06.Input> sample = Seq.of(new D06.Input(7, 9), new D06.Input(15, 40), new D06.Input(30, 200));
    final Seq<D06.Input> star1 = Seq.of(new D06.Input(47, 282), new D06.Input(70, 1079), new D06.Input(75, 1147), new D06.Input(66, 1062));
    final Seq<D06.Input> star2 = Seq.of(new D06.Input(47707566, 282107911471062L));


    @Test
    void sampleStar1() {
        Assertions.assertThat(new D06(sample).star1()).isEqualTo(288L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D06(star1).star1()).isEqualTo(281600L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D06(star2).star2()).isEqualTo(33875953L);
    }
}
