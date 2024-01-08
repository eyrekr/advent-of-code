package com.github.eyrekr.y2022;

import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D05Test {

    final String sample = """
                [D]   \s
            [N] [C]   \s
            [Z] [M] [P]
             1   2   3\s
                        
            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2
            """;

    final String input = Out.testResource("2022/D05.txt");
    final Seq<Seq<String>> sampleStacks = Seq.of(
            Seq.empty(),
            Seq.of("N", "Z"),
            Seq.of("D", "C", "M"),
            Seq.of("P"));
    final Seq<Seq<String>> stacks = Seq.of(
            Seq.empty(),
            Seq.of("F", "R", "W"), //1
            Seq.of("P", "W", "V", "D", "C", "M", "H", "T"), //2
            Seq.of("L", "N", "Z", "M", "P"), //3
            Seq.of("R", "H", "C", "J"), //4
            Seq.of("B", "T", "Q", "H", "G", "P", "C"), //5
            Seq.of("Z", "F", "L", "W", "C", "G"), //6
            Seq.of("C", "G", "J", "Z", "Q", "L", "V", "W"), //7
            Seq.of("C", "V", "T", "W", "F", "R", "N", "P"), //8
            Seq.of("V", "S", "R", "G", "H", "W", "J") //9
    );


    @Test
    void sampleStar1() {
        Assertions.assertThat(new D05(sampleStacks, sample).star1()).isEqualTo("CMZ");
    }

    @Test
    void star1() {
        Assertions.assertThat(new D05(stacks, input).star1()).isEqualTo("CVCWCRTVQ");
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D05(sampleStacks, sample).star2()).isEqualTo("MCD");
    }

    @Test
    void star2() {
        Assertions.assertThat(new D05(stacks, input).star2()).isEqualTo("");
    }
}
