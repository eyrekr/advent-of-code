package com.github.eyrekr.mutable;

import com.github.eyrekr.immutable.Opt;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HpTest {

    @Test
    void test() {
        final Hp<String> heap = Hp.empty();
        heap.add(10, "a").add(15, "b").add(20, "c").add(17, "d").print();
        final Opt<String> a = heap.removeFirst();
        assertThat(a.present).isTrue();
        assertThat(a.value).isEqualTo("a");

        heap.print().add(25, "e").print().add(1, "a").print();
    }
}
