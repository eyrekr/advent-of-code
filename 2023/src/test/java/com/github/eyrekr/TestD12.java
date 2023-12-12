package com.github.eyrekr;

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
        assertThat(new D12(SAMPLE).star2()).isEqualTo(0L);
    }

    @Test
    void star1() {
        assertThat(new D12(Str.testResource("D12.txt")).star1()).isEqualTo(0L);
    }

    @Test
    void star2() {
        assertThat(new D12(Str.testResource("D12.txt")).star2()).isEqualTo(0L);
    }
}
