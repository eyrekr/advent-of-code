package com.github.eyrekr;

import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

class D08Test {

    final String input = Str.testResource("D08.txt");

    @Test
    void star1() {
        Str.print("%d\n", new D08(input).star1());
    }

    @Test
    void star2() {
        Str.print("%d\n", new D08(input).star2());
    }
}
