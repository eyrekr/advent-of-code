package com.github.eyrekr.y2025;

import com.github.eyrekr.AocTest;
import com.github.eyrekr.output.Out;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class D12Test {

    final String input = Out.testResource("2025/D12.txt");

    @Test
    void star1() {
        Assertions.assertThat(new D12(input).star1()).isEqualTo(505L);
    }

}
