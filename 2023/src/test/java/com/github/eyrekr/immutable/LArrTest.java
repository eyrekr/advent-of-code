package com.github.eyrekr.immutable;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LArrTest {

    static final LArr ARR = LArr.of(-1, 0, 1)
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
        assertThat(LArr.of(1, 2, 3)).isEqualTo(LArr.fromArray(new long[]{1, 2, 3}));
    }

    @Test
    void empty() {
        assertThat(LArr.empty()).isEqualTo(LArr.fromArray(new long[0]));
    }

    @Test
    void fromIterable() {
        assertThat(LArr.fromIterable(List.of(1L, 2L, 3L))).isEqualTo(LArr.fromArray(new long[]{1, 2, 3}));
    }

    @Test
    void range() {
        assertThat(LArr.range(1, 6)).isEqualTo(LArr.fromArray(new long[]{1, 2, 3, 4, 5}));
    }

    @Test
    void addLast() {
        assertThat(LArr.of(1, 2, 3).addLast(4)).isEqualTo(LArr.of(1, 2, 3, 4));
    }

    @Test
    void arr_arrLast() {
        assertThat(ARR.addLast(99)).isEqualTo(LArr.of(-3, -2, -1, 0, 1, 2, 3, 99));
    }

    @Test
    void removeLast() {
        assertThat(LArr.of(1, 2, 3).removeLast()).isEqualTo(LArr.of(1, 2));
    }

    @Test
    void arr_removeLast() {
        assertThat(ARR.removeLast()).isEqualTo(LArr.of(-3, -2, -1, 0, 1, 2));
    }

    @Test
    void addFirst_Once() {
        assertThat(LArr.of(2, 3, 4).addFirst(1)).isEqualTo(LArr.of(1, 2, 3, 4));
    }

    @Test
    void arr_addFirst() {
        assertThat(ARR.addFirst(99)).isEqualTo(LArr.of(99, -3, -2, -1, 0, 1, 2, 3));
    }

    @Test
    void addFirst_Twice() {
        assertThat(LArr.of(3, 4).addFirst(2).addFirst(1)).isEqualTo(LArr.of(1, 2, 3, 4));
    }

    @Test
    void removeFirst() {
        assertThat(LArr.of(1, 2, 3, 4).removeFirst()).isEqualTo(LArr.of(2, 3, 4));
    }

    @Test
    void arr_removeFirst() {
        assertThat(ARR.removeFirst()).isEqualTo(LArr.of(-2, -1, 0, 1, 2, 3));
    }

    @Test
    void at_0() {
        assertThat(LArr.of(1, 2, 3, 4).at(0)).isEqualTo(1);
    }

    @Test
    void arr_at_0() {
        assertThat(ARR.at(0)).isEqualTo(-3);
    }

    @Test
    void at_1() {
        assertThat(LArr.of(1, 2, 3, 4).at(1)).isEqualTo(2);
    }

    @Test
    void arr_at_1() {
        assertThat(ARR.at(1)).isEqualTo(-2);
    }

    @Test
    void at_N() {
        assertThat(LArr.of(1, 2, 3, 4).at(4)).isEqualTo(1);
    }

    @Test
    void arr_at_N() {
        assertThat(ARR.at(ARR.length)).isEqualTo(-3);
    }

    @Test
    void at_Minus1() {
        assertThat(LArr.of(1, 2, 3, 4).at(-1)).isEqualTo(4);
    }

    @Test
    void arr_at_Minus1() {
        assertThat(ARR.at(-1)).isEqualTo(3);
    }

    @Test
    void at_Minus2() {
        assertThat(LArr.of(1, 2, 3, 4).at(-2)).isEqualTo(3);
    }

    @Test
    void arr_at_Minus2() {
        assertThat(ARR.at(-2)).isEqualTo(2);
    }

    @Test
    void at_0WhenEmpty() {
        assertThat(LArr.empty().at(0)).isEqualTo(0);
    }

    @Test
    void has() {
        assertThat(LArr.of(1, 2, 3, 4).has(1)).isTrue();
    }

    @Test
    void arr_has() {
        assertThat(ARR.has(0)).isTrue();
    }

    @Test
    void has_WhenEmpty() {
        assertThat(LArr.empty().has(1)).isFalse();
    }

    @Test
    void has_Fail() {
        assertThat(LArr.of(1, 2, 3, 4).has(5)).isFalse();
    }

    @Test
    void has_ZeroNotPickedByAccident() {
        assertThat(LArr.of(1, 2, 3, 4).has(0)).isFalse();
    }

    @Test
    void atLeastOneIs() {
        assertThat(LArr.of(1, 2, 3, 4).atLeastOneIs(l -> l > 3)).isTrue();
    }

    @Test
    void arr_atLeastOneIs() {
        assertThat(ARR.atLeastOneIs(l -> l == 0)).isTrue();
    }


    @Test
    void atLeastOneIs_Fail() {
        assertThat(LArr.of(1, 2, 3, 4).atLeastOneIs(l -> l < 0)).isFalse();
    }

    @Test
    void atLeastOneIsNot() {
        assertThat(LArr.of(1, 2, 3, 4).atLeastOneIsNot(l -> l > 3)).isTrue();
    }

    @Test
    void arr_atLeastOneIsNot() {
        assertThat(ARR.atLeastOneIsNot(l -> l == 0)).isTrue();
    }

    @Test
    void atLeastOneIsNot_Fail() {
        assertThat(LArr.of(1, 2, 3, 4).atLeastOneIsNot(l -> l >= 0)).isFalse();
    }

    @Test
    void allMatch() {
        assertThat(LArr.of(1, 1, 1, 1).allMatch(1)).isTrue();
    }

    @Test
    void allMatch_Fail() {
        assertThat(LArr.of(1, 1, 0, 1).allMatch(1)).isFalse();
    }

    @Test
    void allMatch_WhenEmpty() {
        assertThat(LArr.empty().allMatch(1)).isTrue();
    }

    @Test
    void arr_allMatch() {
        assertThat(ARR.allMatch(0)).isFalse();
    }

    @Test
    void allAre() {
        assertThat(LArr.of(1, 2, 3, 4).allAre(l -> l > 0)).isTrue();
    }

    @Test
    void allAre_Fail() {
        assertThat(LArr.of(1, 2, 3, 4).allAre(l -> l < 4)).isFalse();
    }

    @Test
    void allAre_WhenEmpty() {
        assertThat(LArr.empty().allAre(l -> l > 0)).isTrue();
    }

    @Test
    void arr_allAre() {
        assertThat(ARR.allAre(l -> l < 99)).isTrue();
    }

    @Test
    void noneMatch() {
        assertThat(LArr.of(1, 2, 3, 4).noneMatch(0)).isTrue();
    }

    @Test
    void noneMatch_Fail() {
        assertThat(LArr.of(1, 2, 3, 4).noneMatch(2)).isFalse();
    }

    @Test
    void noneMatch_WhenEmpty() {
        assertThat(LArr.empty().noneMatch(0)).isTrue();
    }

    @Test
    void arr_noneMatch() {
        assertThat(ARR.noneMatch(99)).isTrue();
    }

    @Test
    void noneIs() {
        assertThat(LArr.of(1, 2, 3, 4).noneIs(l -> l < 0)).isTrue();
    }

    @Test
    void noneIs_Fail() {
        assertThat(LArr.of(1, 2, 3, 4).noneIs(l -> l == 0)).isTrue();
    }

    @Test
    void noneIs_WhenEmpty() {
        assertThat(LArr.empty().noneIs(l -> l == 0)).isTrue();
    }

    @Test
    void arr_noneIs() {
        assertThat(ARR.noneIs(l -> l > 99)).isTrue();
    }


    @Test
    void reduce() {
        assertThat(LArr.of(1, 2, 3, 4, -1, -2, -3, -4).reduce(1L, Long::sum)).isOne();
    }

    @Test
    void reduce_ToString() {
        assertThat(LArr.of(1, 2, 3, 4, -1, -2, -3, -4).reduce("", (acc, l) -> acc + l)).isEqualTo("1234-1-2-3-4");
    }

    @Test
    void reduce_WhenEmpty() {
        assertThat(LArr.empty().reduce(1L, Long::sum)).isOne();
    }

    @Test
    void arr_reduce() {
        assertThat(ARR.reduce(0L, Long::sum)).isZero();
    }

    @Test
    void sum() {
        assertThat(LArr.of(1, 2, 3, 4, 5 - 1, -2, -3, -4).sum()).isEqualTo(5);
    }

    @Test
    void sum_WhenEmpty() {
        assertThat(LArr.empty().sum()).isZero();
    }

    @Test
    void arr_sum() {
        assertThat(ARR.sum()).isZero();
    }

    @Test
    void prod() {
        assertThat(LArr.of(1, 2, 3, 4, -1).prod()).isEqualTo(-24);
    }

    @Test
    void prod_WhenEmpty() {
        assertThat(LArr.empty().prod()).isZero();
    }

    @Test
    void arr_prod() {
        assertThat(ARR.prod()).isEqualTo(0);
    }

    @Test
    void min() {
        assertThat(LArr.of(1, 2, 3, -1, 0, 4).min()).isEqualTo(-1);
    }

    @Test
    void min_WhenEmpty() {
        assertThat(LArr.empty().min()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    void arr_min() {
        assertThat(ARR.min()).isEqualTo(-3);
    }


    @Test
    void max() {
        assertThat(LArr.of(1, 2, 3, -1, 0, 4).max()).isEqualTo(4);
    }

    @Test
    void max_WhenEmpty() {
        assertThat(LArr.empty().max()).isEqualTo(Long.MIN_VALUE);
    }

    @Test
    void arr_max() {
        assertThat(ARR.max()).isEqualTo(3);
    }

    @Test
    void where() {
        assertThat(LArr.of(1, 2, 3, -1, 0, 4).where(l -> l <= 0)).isEqualTo(LArr.of(-1, 0));
    }

    @Test
    void where_WhenEMpty() {
        assertThat(LArr.empty().where(l -> l <= 0)).isEqualTo(LArr.empty());
    }

    @Test
    void arr_where() {
        assertThat(ARR.where(l -> l % 2 == 0)).isEqualTo(LArr.of(-2, 0, 2));
    }

    @Test
    void reverse() {
        assertThat(LArr.of(1, 2, 3, -1, 0, 4).reverse()).isEqualTo(LArr.of(4, 0, -1, 3, 2, 1));
    }

    @Test
    void arr_reverse() {
        assertThat(ARR.reverse()).isEqualTo(LArr.of(3, 2, 1, 0, -1, -2, -3));
    }

    @Test
    void sort() {
        assertThat(LArr.of(1, 2, 0, 3, -1, 0, 4).sort()).isEqualTo(LArr.of(-1, 0, 0, 1, 2, 3, 4));
    }

    @Test
    void arr_sort() {
        assertThat(ARR.sort()).isEqualTo(ARR);
    }

    @Test
    void sortBy() {
        assertThat(LArr.of(1, 2, 0, 3, -1, 0, 4).sortBy(Comparator.comparingLong(l -> -l)))
                .isEqualTo(LArr.of(4, 3, 2, 1, 0, 0, -1));
    }

    @Test
    void arr_sortBy() {
        assertThat(ARR.sortBy(Comparator.comparingLong(Math::abs))).isEqualTo(LArr.of(0, 1, -1, 2, -2, 3, -3));
    }

    @Test
    void unique() {
        assertThat(LArr.of(1, 2, 0, 1, -1, 0, 4).unique()).isEqualTo(LArr.of(1, 2, 0, -1, 4));
    }

    @Test
    void unique_whenEmpty() {
        assertThat(LArr.empty().unique()).isEqualTo(LArr.empty());
    }

    @Test
    void arr_unique() {
        assertThat(ARR.unique()).isEqualTo(ARR);
    }

    @Test
    void first() {
        assertThat(LArr.of(1, 2, 0, 1, -1, 0, 4).first(2)).isEqualTo(LArr.of(1, 2));
    }

    @Test
    void first_WhenShorter() {
        assertThat(LArr.of(1, 2).first(3)).isEqualTo(LArr.of(1, 2));
    }

    @Test
    void last() {
        assertThat(LArr.of(1, 2, 0, 1, -1, 0, 4).last(2)).isEqualTo(LArr.of(0, 4));
    }

    @Test
    void last_WhenShorter() {
        assertThat(LArr.of(1, 2).last(3)).isEqualTo(LArr.of(1, 2));
    }

    @Test
    void skip() {
        assertThat(LArr.of(1, 2, 0, 1, -1, 0).skip(2)).isEqualTo(LArr.of(0, 1, -1, 0));
    }

    @Test
    void skip_WhenShorter() {
        assertThat(LArr.of(1, 2).skip(3)).isEqualTo(LArr.empty());
    }

    @Test
    void takeWhile() {
        assertThat(LArr.of(1, 2, 0, 1, -1, 0).takeWhile(l -> l > 0)).isEqualTo(LArr.of(1, 2));
    }

    @Test
    void takeWhile_Fail() {
        assertThat(LArr.of(1, 2).takeWhile(l -> l < 0)).isEqualTo(LArr.empty());
    }

    @Test
    void skipWhile() {
        assertThat(LArr.of(1, 2, 0, 1, -1, 0).skipWhile(l -> l > 0)).isEqualTo(LArr.of(0, 1, -1, 0));
    }

    @Test
    void skipWhile_Fail() {
        assertThat(LArr.of(1, 2).skipWhile(l -> l > 0)).isEqualTo(LArr.empty());
    }

    @Test
    void map() {
        assertThat(LArr.of(1, 2, 0, 1, -1, 0).map(l -> -l)).isEqualTo(LArr.of(-1, -2, 0, -1, 1, 0));
    }

    @Test
    void map_WhenEmpty() {
        assertThat(LArr.empty().map(l -> -l)).isEqualTo(LArr.empty());
    }

    @Test
    void mapWith() {
        assertThat(LArr.of(1, 2, 0, 1, -1, 0)
                .mapWith(LArr.of(1, 2, 3, 4, 5, 6), (a, b) -> b - a))
                .isEqualTo(LArr.of(0, 0, 3, 3, 6, 6));
    }

    @Test
    void mapWith_WhenShorter() {
        assertThat(LArr.of(1, 2, 0, 1, -1, 0)
                .mapWith(LArr.of(1, 2), (a, b) -> b - a))
                .isEqualTo(LArr.of(0, 0));
    }

    @Test
    void prodWith() {
        assertThat(LArr.of(1, 2, 3)
                .prodWith(LArr.of(-1, -2), (a, b) -> b * a))
                .isEqualTo(LArr.of(-1, -2, -2, -4, -3, -6));
    }

    @Test
    void prodUpperTriangleWith() {
        assertThat(LArr.of(1, 2, 3, 4)
                .prodUpperTriangleWith(LArr.of(1, 2, 3, 4), (a, b) -> b * a))
                .isEqualTo(LArr.of(2, 3, 4, 6, 8, 12));
    }

    @Test
    void flatMap() {
        assertThat(LArr.of(1, 2, 3, 4).flatMap(l -> LArr.of(l, -l)))
                .isEqualTo(LArr.of(1, -1, 2, -2, 3, -3, 4, -4));
    }

    @Test
    void flatMap_Removal() {
        assertThat(LArr.of(1, 2, 3, 4).flatMap(l -> l % 2 == 0 ? LArr.of(l) : LArr.empty()))
                .isEqualTo(LArr.of(2, 4));
    }

    @Test
    void equals() {
        assertThat(LArr.of(1, 2, 3, 4)).isEqualTo(LArr.of(1, 2, 3, 4));
    }

    @Test
    void equals_Fail() {
        assertThat(LArr.of(1, 2, 3, 4)).isNotEqualTo(LArr.of(1, 4, 2, 3));
    }

    @Test
    void toArray() {
        assertThat(LArr.of(1, 2, 3, 4).toArray()).isEqualTo(new long[]{1, 2, 3, 4});
    }

    @Test
    void toSet() {
        assertThat(LArr.of(1, 2, 3, 4).toSet()).isEqualTo(Set.of(1L, 2L, 3L, 4L));
    }


}
