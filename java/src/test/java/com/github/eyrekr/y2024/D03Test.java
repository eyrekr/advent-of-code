package com.github.eyrekr.y2024;

import com.github.eyrekr.AocTest;

class D03Test extends AocTest {

    D03Test() {
        super(D03::new,
                new Star("xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))", 161L, 174336360L),
                new Star("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))", 48L, 88802350L));
    }
}
