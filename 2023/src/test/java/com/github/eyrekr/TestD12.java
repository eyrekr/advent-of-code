package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestD12 {
    final String SAMPLE = """
            ???.### 1,1,3
            .??..??...?##. 1,1,3
            ?#?#?#?#?#?#?#? 1,3,1,6
            ????.#...#... 4,1,1
            ????.######..#####. 1,6,5
            ?###???????? 3,2,1
            """;

    @Test
    void sampleStar1() {
        assertThat(new D12(SAMPLE).star1()).isEqualTo(21L);
    }

    @Test
    void sampleStar2() {
        assertThat(new D12(SAMPLE).star2()).isEqualTo(525152L);
    }

    @Test
    void star1() {
        assertThat(new D12(Str.testResource("D12.txt")).star1()).isEqualTo(6871L);
    }

    @Test
    void star2() {
        assertThat(new D12(Str.testResource("D12.txt")).star2()).isEqualTo(0L);
    }

    @Test
    void t1() {
        final var result = D12.tryToArrange(Seq.of("???", "###"), Seq.of(1L, 1L, 3L));
        assertThat(result).isEqualTo(1);
    }

    @Test
    void t2() {
        final var result = D12.tryToArrange(Seq.of("??", "??", "?##"), Seq.of(1L, 1L, 3L));
        assertThat(result).isEqualTo(4);
    }

    @Test
    void t3() {
        final var result = D12.tryToArrange(Seq.of("?#?#?#?#?#?#?#?"), Seq.of(1L, 3L, 1L, 6L));
        assertThat(result).isEqualTo(1);
    }

    @Test
    void t4() {
        final var result = D12.tryToArrange(Seq.of("????", "#", "#"), Seq.of(4L, 1L, 1L));
        assertThat(result).isEqualTo(1);
    }

    @Test
    void t5() {
        final var result = D12.tryToArrange(Seq.of("????", "######", "#####"), Seq.of(1L, 6L, 5L));
        assertThat(result).isEqualTo(4);
    }

    @Test
    void t6() {
        final var result = D12.tryToArrange(Seq.of("?###????????"), Seq.of(3L, 2L, 1L));
        assertThat(result).isEqualTo(10);
    }

    @Test
    void t7() {
        final var result = D12.tryToArrange(Seq.of("#????????", "???"), Seq.of(5L, 1L, 1L, 1L));
        assertThat(result).isEqualTo(6);
    }

    @Test
    void t8() {
        /*
            ##.#.#.##..##.....
            ##.#.#..##.##.....
            ##...#..#..##...##
         */
        final var result = D12.tryToArrange(Seq.of("#?", "?", "#??#???#", "?", "??"), Seq.of(2L, 1L, 1L, 2L, 2L));
        assertThat(result).isEqualTo(3);
    }


    @Test
    void t9() {
        /*
            ###.#..........
            ###..#.........
            ###....#.......
            ###.....#......
            ###......#.....
            ###.......#....
            ###........#...
            ###..........#.
         */
        final var result = D12.tryToArrange(Seq.of("?#?", "??", "?????", "?"), Seq.of(3L, 1L));
        assertThat(result).isEqualTo(8);
    }

    @Test
    void t() {
        final String input = """
                #???#?#?.?##?#?.#. 7,5,1
                ??#?#?#????.????#? 6,2,1,1
                .#????????.??? 5,1,1,1
                ?#.??.??????? 1,1,1,2
                #.???#?##.??.????. 1,1,1,2,2,1
                ?????????.??? 7,1
                ?#?.?#????# 1,2,3
                .????..??#?. 3,2
                ???..?#?.##???##.?#? 2,2,3,2,2
                ??#?.#..???? 3,1,2
                ??##?#???????? 5,1,1
                #????????#??#?? 1,10
                .?????????? 4,1
                .??#?##???#???## 6,1,1,4
                ?#?#?.?#??.??????#? 4,1,4,2
                ?##?#??#???? 7,1
                ##???.?????. 2,1,1,1
                """;
        final var result = new D12(input).star1();
        assertThat(result).isEqualTo(147L);
    }
}
