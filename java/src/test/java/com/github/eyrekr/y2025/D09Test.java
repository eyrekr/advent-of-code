package com.github.eyrekr.y2025;

import com.github.eyrekr.AocTest;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class D09Test extends AocTest {

    D09Test() {
        constructor(D09::new);
        sample("""
                7,1
                11,1
                11,7
                9,7
                9,5
                2,5
                2,3
                7,3
                """);
        star1(50L, 4776487744L);
        star2(24L, -1L);
    }

    @Test
    void isInsideRectangle() {
        final D09.P a = new D09.P(5,5),
                b = new D09.P(10,10),
                o = new D09.P(0,0),
                c1 = new D09.P((5+10)/2, 5),
                c2 = new D09.P((5+10)/2, 10),
                c3 = new D09.P(5, (5+10)/2),
                c4 = new D09.P(10, (5+10)/2);
        final D09.Rectangle r = new D09.Rectangle(a,b);
       assertThat(r.isPointInside(a)).isFalse();
       assertThat(r.isPointInside(b)).isFalse();
       assertThat(r.isPointInside(r.center())).isTrue();
       assertThat(r.isPointInside(c1)).isFalse();
       assertThat(r.isPointInside(c2)).isFalse();
       assertThat(r.isPointInside(c3)).isFalse();
       assertThat(r.isPointInside(c4)).isFalse();
       assertThat(r.isPointInside(o)).isFalse();
    }

    @Test
    void isOnLine() {
        final D09.P a = new D09.P(5,5),
                b = new D09.P(10,5),
                o = new D09.P(0,0),
                c1 = new D09.P((5+10)/2, 5),
                c2 = new D09.P((5+10)/2, 10),
                c3 = new D09.P(5, (5+10)/2),
                c4 = new D09.P(10, (5+10)/2);
        assertThat(o.isOnLine(a,b)).isFalse();
        assertThat(o.isOnLine(b,a)).isFalse();
        assertThat(c1.isOnLine(a,b)).isTrue();
        assertThat(c2.isOnLine(a,b)).isFalse();
        assertThat(c3.isOnLine(a,b)).isFalse();
        assertThat(c4.isOnLine(a,b)).isFalse();
        assertThat(a.isOnLine(a,b)).isTrue();
        assertThat(b.isOnLine(a,b)).isTrue();
    }
}
