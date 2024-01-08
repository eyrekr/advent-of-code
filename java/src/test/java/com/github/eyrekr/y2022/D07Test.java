package com.github.eyrekr.y2022;

import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D07Test {

    final String sample = """
            $ cd /
            $ ls
            dir a
            14848514 b.txt
            8504156 c.dat
            dir d
            $ cd a
            $ ls
            dir e
            29116 f
            2557 g
            62596 h.lst
            $ cd e
            $ ls
            584 i
            $ cd ..
            $ cd ..
            $ cd d
            $ ls
            4060174 j
            8033020 d.log
            5626152 d.ext
            7214296 k
            """;

    final String input = Out.testResource("2022/D07.txt");

    @Test
    void sampleStar1() {
        Assertions.assertThat(new D07(sample).star1()).isEqualTo(95437L);
    }

    @Test
    void star1() {
        Assertions.assertThat(new D07(input).star1()).isEqualTo(1783610L);
    }

    @Test
    void sampleStar2() {
        Assertions.assertThat(new D07(sample).star2()).isEqualTo(24933642L);
    }

    @Test
    void star2() {
        Assertions.assertThat(new D07(input).star2()).isEqualTo(0L);
    }
}
