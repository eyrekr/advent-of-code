package com.github.eyrekr;

import com.github.eyrekr.util.Fs;
import com.github.eyrekr.util.Str;
import org.junit.jupiter.api.Test;

class TestD08 {

    @Test
    void star1() {
        Str.print("%d\n", new D08(Fs.testResource("D08.txt")).star1());
    }

    @Test
    void star2() {
        Str.print("%d\n", new D08(Fs.testResource("D08.txt")).star2());
    }
}
