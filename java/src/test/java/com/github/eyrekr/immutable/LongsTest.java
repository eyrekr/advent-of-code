package com.github.eyrekr.immutable;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LongsTest {

    static final Longs ARR = Longs.of(-1, 0, 1)
            .addFirst(-2)
            .addLast(2)
            .addFirst(-3)
            .addLast(3)
            .addFirst(-4)
            .addLast(4)
            .removeFirst()
            .removeLast()
            .print(" ");


    @Test
    void of() {
        assertThat(Longs.of(1, 2, 3)).isEqualTo(Longs.fromArray(new long[]{1, 2, 3}));
    }

    @Test
    void empty() {
        assertThat(Longs.empty()).isEqualTo(Longs.fromArray(new long[0]));
    }

    @Test
    void fromIterable() {
        assertThat(Longs.fromIterable(List.of(1L, 2L, 3L))).isEqualTo(Longs.fromArray(new long[]{1, 2, 3}));
    }

    @Test
    void range() {
        assertThat(Longs.range(1, 6)).isEqualTo(Longs.fromArray(new long[]{1, 2, 3, 4, 5}));
    }

    @Test
    void addLast() {
        assertThat(Longs.of(1, 2, 3).addLast(4)).isEqualTo(Longs.of(1, 2, 3, 4));
    }

    @Test
    void arr_arrLast() {
        assertThat(ARR.addLast(99)).isEqualTo(Longs.of(-3, -2, -1, 0, 1, 2, 3, 99));
    }

    @Test
    void removeLast() {
        assertThat(Longs.of(1, 2, 3).removeLast()).isEqualTo(Longs.of(1, 2));
    }

    @Test
    void arr_removeLast() {
        assertThat(ARR.removeLast()).isEqualTo(Longs.of(-3, -2, -1, 0, 1, 2));
    }

    @Test
    void addFirst_Once() {
        assertThat(Longs.of(2, 3, 4).addFirst(1)).isEqualTo(Longs.of(1, 2, 3, 4));
    }

    @Test
    void arr_addFirst() {
        assertThat(ARR.addFirst(99)).isEqualTo(Longs.of(99, -3, -2, -1, 0, 1, 2, 3));
    }

    @Test
    void addFirst_Twice() {
        assertThat(Longs.of(3, 4).addFirst(2).addFirst(1)).isEqualTo(Longs.of(1, 2, 3, 4));
    }

    @Test
    void removeFirst() {
        assertThat(Longs.of(1, 2, 3, 4).removeFirst()).isEqualTo(Longs.of(2, 3, 4));
    }

    @Test
    void arr_removeFirst() {
        assertThat(ARR.removeFirst()).isEqualTo(Longs.of(-2, -1, 0, 1, 2, 3));
    }

    @Test
    void at_0() {
        assertThat(Longs.of(1, 2, 3, 4).at(0)).isEqualTo(1);
    }

    @Test
    void arr_at_0() {
        assertThat(ARR.at(0)).isEqualTo(-3);
    }

    @Test
    void at_1() {
        assertThat(Longs.of(1, 2, 3, 4).at(1)).isEqualTo(2);
    }

    @Test
    void arr_at_1() {
        assertThat(ARR.at(1)).isEqualTo(-2);
    }

    @Test
    void at_N() {
        assertThat(Longs.of(1, 2, 3, 4).at(4)).isEqualTo(1);
    }

    @Test
    void arr_at_N() {
        assertThat(ARR.at(ARR.length)).isEqualTo(-3);
    }

    @Test
    void at_Minus1() {
        assertThat(Longs.of(1, 2, 3, 4).at(-1)).isEqualTo(4);
    }

    @Test
    void arr_at_Minus1() {
        assertThat(ARR.at(-1)).isEqualTo(3);
    }

    @Test
    void at_Minus2() {
        assertThat(Longs.of(1, 2, 3, 4).at(-2)).isEqualTo(3);
    }

    @Test
    void arr_at_Minus2() {
        assertThat(ARR.at(-2)).isEqualTo(2);
    }

    @Test
    void at_0WhenEmpty() {
        assertThat(Longs.empty().at(0)).isEqualTo(0);
    }

    @Test
    void has() {
        assertThat(Longs.of(1, 2, 3, 4).has(1)).isTrue();
    }

    @Test
    void arr_has() {
        assertThat(ARR.has(0)).isTrue();
    }

    @Test
    void has_WhenEmpty() {
        assertThat(Longs.empty().has(1)).isFalse();
    }

    @Test
    void has_Fail() {
        assertThat(Longs.of(1, 2, 3, 4).has(5)).isFalse();
    }

    @Test
    void has_ZeroNotPickedByAccident() {
        assertThat(Longs.of(1, 2, 3, 4).has(0)).isFalse();
    }

    @Test
    void atLeastOneIs() {
        assertThat(Longs.of(1, 2, 3, 4).atLeastOneIs(l -> l > 3)).isTrue();
    }

    @Test
    void arr_atLeastOneIs() {
        assertThat(ARR.atLeastOneIs(l -> l == 0)).isTrue();
    }


    @Test
    void atLeastOneIs_Fail() {
        assertThat(Longs.of(1, 2, 3, 4).atLeastOneIs(l -> l < 0)).isFalse();
    }

    @Test
    void atLeastOneIsNot() {
        assertThat(Longs.of(1, 2, 3, 4).atLeastOneIsNot(l -> l > 3)).isTrue();
    }

    @Test
    void arr_atLeastOneIsNot() {
        assertThat(ARR.atLeastOneIsNot(l -> l == 0)).isTrue();
    }

    @Test
    void atLeastOneIsNot_Fail() {
        assertThat(Longs.of(1, 2, 3, 4).atLeastOneIsNot(l -> l >= 0)).isFalse();
    }

    @Test
    void allMatch() {
        assertThat(Longs.of(1, 1, 1, 1).allMatch(1)).isTrue();
    }

    @Test
    void allMatch_Fail() {
        assertThat(Longs.of(1, 1, 0, 1).allMatch(1)).isFalse();
    }

    @Test
    void allMatch_WhenEmpty() {
        assertThat(Longs.empty().allMatch(1)).isTrue();
    }

    @Test
    void arr_allMatch() {
        assertThat(ARR.allMatch(0)).isFalse();
    }

    @Test
    void allAre() {
        assertThat(Longs.of(1, 2, 3, 4).allAre(l -> l > 0)).isTrue();
    }

    @Test
    void allAre_Fail() {
        assertThat(Longs.of(1, 2, 3, 4).allAre(l -> l < 4)).isFalse();
    }

    @Test
    void allAre_WhenEmpty() {
        assertThat(Longs.empty().allAre(l -> l > 0)).isTrue();
    }

    @Test
    void arr_allAre() {
        assertThat(ARR.allAre(l -> l < 99)).isTrue();
    }

    @Test
    void noneMatch() {
        assertThat(Longs.of(1, 2, 3, 4).noneMatch(0)).isTrue();
    }

    @Test
    void noneMatch_Fail() {
        assertThat(Longs.of(1, 2, 3, 4).noneMatch(2)).isFalse();
    }

    @Test
    void noneMatch_WhenEmpty() {
        assertThat(Longs.empty().noneMatch(0)).isTrue();
    }

    @Test
    void arr_noneMatch() {
        assertThat(ARR.noneMatch(99)).isTrue();
    }

    @Test
    void noneIs() {
        assertThat(Longs.of(1, 2, 3, 4).noneIs(l -> l < 0)).isTrue();
    }

    @Test
    void noneIs_Fail() {
        assertThat(Longs.of(1, 2, 3, 4).noneIs(l -> l == 0)).isTrue();
    }

    @Test
    void noneIs_WhenEmpty() {
        assertThat(Longs.empty().noneIs(l -> l == 0)).isTrue();
    }

    @Test
    void arr_noneIs() {
        assertThat(ARR.noneIs(l -> l > 99)).isTrue();
    }


    @Test
    void reduce() {
        assertThat(Longs.of(1, 2, 3, 4, -1, -2, -3, -4).reduce(1L, Long::sum)).isOne();
    }

    @Test
    void reduce_ToString() {
        assertThat(Longs.of(1, 2, 3, 4, -1, -2, -3, -4).reduce("", (acc, l) -> acc + l)).isEqualTo("1234-1-2-3-4");
    }

    @Test
    void reduce_WhenEmpty() {
        assertThat(Longs.empty().reduce(1L, Long::sum)).isOne();
    }

    @Test
    void arr_reduce() {
        assertThat(ARR.reduce(0L, Long::sum)).isZero();
    }

    @Test
    void sum() {
        assertThat(Longs.of(1, 2, 3, 4, 5 - 1, -2, -3, -4).sum()).isEqualTo(5);
    }

    @Test
    void sum_WhenEmpty() {
        assertThat(Longs.empty().sum()).isZero();
    }

    @Test
    void arr_sum() {
        assertThat(ARR.sum()).isZero();
    }

    @Test
    void prod() {
        assertThat(Longs.of(1, 2, 3, 4, -1).prod()).isEqualTo(-24);
    }

    @Test
    void prod_WhenEmpty() {
        assertThat(Longs.empty().prod()).isZero();
    }

    @Test
    void arr_prod() {
        assertThat(ARR.prod()).isEqualTo(0);
    }

    @Test
    void min() {
        assertThat(Longs.of(1, 2, 3, -1, 0, 4).min()).isEqualTo(-1);
    }

    @Test
    void min_WhenEmpty() {
        assertThat(Longs.empty().min()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    void arr_min() {
        assertThat(ARR.min()).isEqualTo(-3);
    }


    @Test
    void max() {
        assertThat(Longs.of(1, 2, 3, -1, 0, 4).max()).isEqualTo(4);
    }

    @Test
    void max_WhenEmpty() {
        assertThat(Longs.empty().max()).isEqualTo(Long.MIN_VALUE);
    }

    @Test
    void arr_max() {
        assertThat(ARR.max()).isEqualTo(3);
    }

    @Test
    void where() {
        assertThat(Longs.of(1, 2, 3, -1, 0, 4).where(l -> l <= 0)).isEqualTo(Longs.of(-1, 0));
    }

    @Test
    void where_WhenEMpty() {
        assertThat(Longs.empty().where(l -> l <= 0)).isEqualTo(Longs.empty());
    }

    @Test
    void arr_where() {
        assertThat(ARR.where(l -> l % 2 == 0)).isEqualTo(Longs.of(-2, 0, 2));
    }

    @Test
    void reverse() {
        assertThat(Longs.of(1, 2, 3, -1, 0, 4).reversed()).isEqualTo(Longs.of(4, 0, -1, 3, 2, 1));
    }

    @Test
    void arr_reverse() {
        assertThat(ARR.reversed()).isEqualTo(Longs.of(3, 2, 1, 0, -1, -2, -3));
    }

    @Test
    void sort() {
        assertThat(Longs.of(1, 2, 0, 3, -1, 0, 4).sorted()).isEqualTo(Longs.of(-1, 0, 0, 1, 2, 3, 4));
    }

    @Test
    void arr_sort() {
        assertThat(ARR.sorted()).isEqualTo(ARR);
    }

    @Test
    void sortBy() {
        assertThat(Longs.of(1, 2, 0, 3, -1, 0, 4).sortedBy(l -> -l))
                .isEqualTo(Longs.of(4, 3, 2, 1, 0, 0, -1));
    }

    @Test
    void arr_sortBy() {
        assertThat(ARR.sortedBy(Math::abs)).isEqualTo(Longs.of(0, 1, -1, 2, -2, 3, -3));
    }

    @Test
    void unique() {
        assertThat(Longs.of(1, 2, 0, 1, -1, 0, 4).unique()).isEqualTo(Longs.of(1, 2, 0, -1, 4));
    }

    @Test
    void unique_whenEmpty() {
        assertThat(Longs.empty().unique()).isEqualTo(Longs.empty());
    }

    @Test
    void arr_unique() {
        assertThat(ARR.unique()).isEqualTo(ARR);
    }

    @Test
    void first() {
        assertThat(Longs.of(1, 2, 0, 1, -1, 0, 4).first(2)).isEqualTo(Longs.of(1, 2));
    }

    @Test
    void first_WhenShorter() {
        assertThat(Longs.of(1, 2).first(3)).isEqualTo(Longs.of(1, 2));
    }

    @Test
    void last() {
        assertThat(Longs.of(1, 2, 0, 1, -1, 0, 4).last(2)).isEqualTo(Longs.of(0, 4));
    }

    @Test
    void last_WhenShorter() {
        assertThat(Longs.of(1, 2).last(3)).isEqualTo(Longs.of(1, 2));
    }

    @Test
    void skip() {
        assertThat(Longs.of(1, 2, 0, 1, -1, 0).skip(2)).isEqualTo(Longs.of(0, 1, -1, 0));
    }

    @Test
    void skip_WhenShorter() {
        assertThat(Longs.of(1, 2).skip(3)).isEqualTo(Longs.empty());
    }

    @Test
    void takeWhile() {
        assertThat(Longs.of(1, 2, 0, 1, -1, 0).takeWhile(l -> l > 0)).isEqualTo(Longs.of(1, 2));
    }

    @Test
    void takeWhile_Fail() {
        assertThat(Longs.of(1, 2).takeWhile(l -> l < 0)).isEqualTo(Longs.empty());
    }

    @Test
    void skipWhile() {
        assertThat(Longs.of(1, 2, 0, 1, -1, 0).skipWhile(l -> l > 0)).isEqualTo(Longs.of(0, 1, -1, 0));
    }

    @Test
    void skipWhile_Fail() {
        assertThat(Longs.of(1, 2).skipWhile(l -> l > 0)).isEqualTo(Longs.empty());
    }

    @Test
    void map() {
        assertThat(Longs.of(1, 2, 0, 1, -1, 0).map(l -> -l)).isEqualTo(Longs.of(-1, -2, 0, -1, 1, 0));
    }

    @Test
    void map_WhenEmpty() {
        assertThat(Longs.empty().map(l -> -l)).isEqualTo(Longs.empty());
    }

    @Test
    void mapWith() {
        assertThat(Longs.of(1, 2, 0, 1, -1, 0)
                .mapWith(Longs.of(1, 2, 3, 4, 5, 6), (a, b) -> b - a))
                .isEqualTo(Longs.of(0, 0, 3, 3, 6, 6));
    }

    @Test
    void mapWith_WhenShorter() {
        assertThat(Longs.of(1, 2, 0, 1, -1, 0)
                .mapWith(Longs.of(1, 2), (a, b) -> b - a))
                .isEqualTo(Longs.of(0, 0));
    }

    @Test
    void prodWith() {
        assertThat(Longs.of(1, 2, 3)
                .prodWith(Longs.of(-1, -2), (a, b) -> b * a))
                .isEqualTo(Longs.of(-1, -2, -2, -4, -3, -6));
    }

    @Test
    void prodUpperTriangleWith() {
        assertThat(Longs.of(1, 2, 3, 4)
                .prodUpperTriangleWith(Longs.of(1, 2, 3, 4), (a, b) -> b * a))
                .isEqualTo(Longs.of(2, 3, 4, 6, 8, 12));
    }

    @Test
    void flatMap() {
        assertThat(Longs.of(1, 2, 3, 4).flatMap(l -> Longs.of(l, -l)))
                .isEqualTo(Longs.of(1, -1, 2, -2, 3, -3, 4, -4));
    }

    @Test
    void flatMap_Removal() {
        assertThat(Longs.of(1, 2, 3, 4).flatMap(l -> l % 2 == 0 ? Longs.of(l) : Longs.empty()))
                .isEqualTo(Longs.of(2, 4));
    }

    @Test
    void equals() {
        assertThat(Longs.of(1, 2, 3, 4)).isEqualTo(Longs.of(1, 2, 3, 4));
    }

    @Test
    void equals_Fail() {
        assertThat(Longs.of(1, 2, 3, 4)).isNotEqualTo(Longs.of(1, 4, 2, 3));
    }

    @Test
    void toArray() {
        assertThat(Longs.of(1, 2, 3, 4).toArray()).isEqualTo(new long[]{1, 2, 3, 4});
    }

    @Test
    void toSet() {
        assertThat(Longs.of(1, 2, 3, 4).toSet()).isEqualTo(Set.of(1L, 2L, 3L, 4L));
    }


}
