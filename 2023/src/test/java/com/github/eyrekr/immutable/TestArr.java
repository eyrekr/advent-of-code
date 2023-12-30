package com.github.eyrekr.immutable;

import com.github.eyrekr.immutable.Arr;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TestArr {

    static final Arr ARR = Arr.of(-1, 0, 1)
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
        assertThat(Arr.of(1, 2, 3)).isEqualTo(Arr.fromArray(new long[]{1, 2, 3}));
    }

    @Test
    void empty() {
        assertThat(Arr.empty()).isEqualTo(Arr.fromArray(new long[0]));
    }

    @Test
    void fromIterable() {
        assertThat(Arr.fromIterable(List.of(1L, 2L, 3L))).isEqualTo(Arr.fromArray(new long[]{1, 2, 3}));
    }

    @Test
    void range() {
        assertThat(Arr.range(1, 6)).isEqualTo(Arr.fromArray(new long[]{1, 2, 3, 4, 5}));
    }

    @Test
    void addLast() {
        assertThat(Arr.of(1, 2, 3).addLast(4)).isEqualTo(Arr.of(1, 2, 3, 4));
    }

    @Test
    void arr_arrLast() {
        assertThat(ARR.addLast(99)).isEqualTo(Arr.of(-3, -2, -1, 0, 1, 2, 3, 99));
    }

    @Test
    void removeLast() {
        assertThat(Arr.of(1, 2, 3).removeLast()).isEqualTo(Arr.of(1, 2));
    }

    @Test
    void arr_removeLast() {
        assertThat(ARR.removeLast()).isEqualTo(Arr.of(-3, -2, -1, 0, 1, 2));
    }

    @Test
    void addFirst_Once() {
        assertThat(Arr.of(2, 3, 4).addFirst(1)).isEqualTo(Arr.of(1, 2, 3, 4));
    }

    @Test
    void arr_addFirst() {
        assertThat(ARR.addFirst(99)).isEqualTo(Arr.of(99, -3, -2, -1, 0, 1, 2, 3));
    }

    @Test
    void addFirst_Twice() {
        assertThat(Arr.of(3, 4).addFirst(2).addFirst(1)).isEqualTo(Arr.of(1, 2, 3, 4));
    }

    @Test
    void removeFirst() {
        assertThat(Arr.of(1, 2, 3, 4).removeFirst()).isEqualTo(Arr.of(2, 3, 4));
    }

    @Test
    void arr_removeFirst() {
        assertThat(ARR.removeFirst()).isEqualTo(Arr.of(-2, -1, 0, 1, 2, 3));
    }

    @Test
    void at_0() {
        assertThat(Arr.of(1, 2, 3, 4).at(0)).isEqualTo(1);
    }

    @Test
    void arr_at_0() {
        assertThat(ARR.at(0)).isEqualTo(-3);
    }

    @Test
    void at_1() {
        assertThat(Arr.of(1, 2, 3, 4).at(1)).isEqualTo(2);
    }

    @Test
    void arr_at_1() {
        assertThat(ARR.at(1)).isEqualTo(-2);
    }

    @Test
    void at_N() {
        assertThat(Arr.of(1, 2, 3, 4).at(4)).isEqualTo(1);
    }

    @Test
    void arr_at_N() {
        assertThat(ARR.at(ARR.length)).isEqualTo(-3);
    }

    @Test
    void at_Minus1() {
        assertThat(Arr.of(1, 2, 3, 4).at(-1)).isEqualTo(4);
    }

    @Test
    void arr_at_Minus1() {
        assertThat(ARR.at(-1)).isEqualTo(3);
    }

    @Test
    void at_Minus2() {
        assertThat(Arr.of(1, 2, 3, 4).at(-2)).isEqualTo(3);
    }

    @Test
    void arr_at_Minus2() {
        assertThat(ARR.at(-2)).isEqualTo(2);
    }

    @Test
    void at_0WhenEmpty() {
        assertThat(Arr.empty().at(0)).isEqualTo(0);
    }

    @Test
    void has() {
        assertThat(Arr.of(1, 2, 3, 4).has(1)).isTrue();
    }

    @Test
    void arr_has() {
        assertThat(ARR.has(0)).isTrue();
    }

    @Test
    void has_WhenEmpty() {
        assertThat(Arr.empty().has(1)).isFalse();
    }

    @Test
    void has_Fail() {
        assertThat(Arr.of(1, 2, 3, 4).has(5)).isFalse();
    }

    @Test
    void has_ZeroNotPickedByAccident() {
        assertThat(Arr.of(1, 2, 3, 4).has(0)).isFalse();
    }

    @Test
    void atLeastOneIs() {
        assertThat(Arr.of(1, 2, 3, 4).atLeastOneIs(l -> l > 3)).isTrue();
    }

    @Test
    void arr_atLeastOneIs() {
        assertThat(ARR.atLeastOneIs(l -> l == 0)).isTrue();
    }


    @Test
    void atLeastOneIs_Fail() {
        assertThat(Arr.of(1, 2, 3, 4).atLeastOneIs(l -> l < 0)).isFalse();
    }

    @Test
    void atLeastOneIsNot() {
        assertThat(Arr.of(1, 2, 3, 4).atLeastOneIsNot(l -> l > 3)).isTrue();
    }

    @Test
    void arr_atLeastOneIsNot() {
        assertThat(ARR.atLeastOneIsNot(l -> l == 0)).isTrue();
    }

    @Test
    void atLeastOneIsNot_Fail() {
        assertThat(Arr.of(1, 2, 3, 4).atLeastOneIsNot(l -> l >= 0)).isFalse();
    }

    @Test
    void allMatch() {
        assertThat(Arr.of(1, 1, 1, 1).allMatch(1)).isTrue();
    }

    @Test
    void allMatch_Fail() {
        assertThat(Arr.of(1, 1, 0, 1).allMatch(1)).isFalse();
    }

    @Test
    void allMatch_WhenEmpty() {
        assertThat(Arr.empty().allMatch(1)).isTrue();
    }

    @Test
    void arr_allMatch() {
        assertThat(ARR.allMatch(0)).isFalse();
    }

    @Test
    void allAre() {
        assertThat(Arr.of(1, 2, 3, 4).allAre(l -> l > 0)).isTrue();
    }

    @Test
    void allAre_Fail() {
        assertThat(Arr.of(1, 2, 3, 4).allAre(l -> l < 4)).isFalse();
    }

    @Test
    void allAre_WhenEmpty() {
        assertThat(Arr.empty().allAre(l -> l > 0)).isTrue();
    }

    @Test
    void arr_allAre() {
        assertThat(ARR.allAre(l -> l < 99)).isTrue();
    }

    @Test
    void noneMatch() {
        assertThat(Arr.of(1, 2, 3, 4).noneMatch(0)).isTrue();
    }

    @Test
    void noneMatch_Fail() {
        assertThat(Arr.of(1, 2, 3, 4).noneMatch(2)).isFalse();
    }

    @Test
    void noneMatch_WhenEmpty() {
        assertThat(Arr.empty().noneMatch(0)).isTrue();
    }

    @Test
    void arr_noneMatch() {
        assertThat(ARR.noneMatch(99)).isTrue();
    }

    @Test
    void noneIs() {
        assertThat(Arr.of(1, 2, 3, 4).noneIs(l -> l < 0)).isTrue();
    }

    @Test
    void noneIs_Fail() {
        assertThat(Arr.of(1, 2, 3, 4).noneIs(l -> l == 0)).isTrue();
    }

    @Test
    void noneIs_WhenEmpty() {
        assertThat(Arr.empty().noneIs(l -> l == 0)).isTrue();
    }

    @Test
    void arr_noneIs() {
        assertThat(ARR.noneIs(l -> l > 99)).isTrue();
    }


    @Test
    void reduce() {
        assertThat(Arr.of(1, 2, 3, 4, -1, -2, -3, -4).reduce(1L, Long::sum)).isOne();
    }

    @Test
    void reduce_ToString() {
        assertThat(Arr.of(1, 2, 3, 4, -1, -2, -3, -4).reduce("", (acc, l) -> acc + l)).isEqualTo("1234-1-2-3-4");
    }

    @Test
    void reduce_WhenEmpty() {
        assertThat(Arr.empty().reduce(1L, Long::sum)).isOne();
    }

    @Test
    void arr_reduce() {
        assertThat(ARR.reduce(0L, Long::sum)).isZero();
    }

    @Test
    void sum() {
        assertThat(Arr.of(1, 2, 3, 4, 5 - 1, -2, -3, -4).sum()).isEqualTo(5);
    }

    @Test
    void sum_WhenEmpty() {
        assertThat(Arr.empty().sum()).isZero();
    }

    @Test
    void arr_sum() {
        assertThat(ARR.sum()).isZero();
    }

    @Test
    void prod() {
        assertThat(Arr.of(1, 2, 3, 4, -1).prod()).isEqualTo(-24);
    }

    @Test
    void prod_WhenEmpty() {
        assertThat(Arr.empty().prod()).isZero();
    }

    @Test
    void arr_prod() {
        assertThat(ARR.prod()).isEqualTo(0);
    }

    @Test
    void min() {
        assertThat(Arr.of(1, 2, 3, -1, 0, 4).min()).isEqualTo(-1);
    }

    @Test
    void min_WhenEmpty() {
        assertThat(Arr.empty().min()).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    void arr_min() {
        assertThat(ARR.min()).isEqualTo(-3);
    }


    @Test
    void max() {
        assertThat(Arr.of(1, 2, 3, -1, 0, 4).max()).isEqualTo(4);
    }

    @Test
    void max_WhenEmpty() {
        assertThat(Arr.empty().max()).isEqualTo(Long.MIN_VALUE);
    }

    @Test
    void arr_max() {
        assertThat(ARR.max()).isEqualTo(3);
    }

    @Test
    void where() {
        assertThat(Arr.of(1, 2, 3, -1, 0, 4).where(l -> l <= 0)).isEqualTo(Arr.of(-1, 0));
    }

    @Test
    void where_WhenEMpty() {
        assertThat(Arr.empty().where(l -> l <= 0)).isEqualTo(Arr.empty());
    }

    @Test
    void arr_where() {
        assertThat(ARR.where(l -> l % 2 == 0)).isEqualTo(Arr.of(-2, 0, 2));
    }

    @Test
    void reverse() {
        assertThat(Arr.of(1, 2, 3, -1, 0, 4).reverse()).isEqualTo(Arr.of(4, 0, -1, 3, 2, 1));
    }

    @Test
    void arr_reverse() {
        assertThat(ARR.reverse()).isEqualTo(Arr.of(3, 2, 1, 0, -1, -2, -3));
    }

    @Test
    void sort() {
        assertThat(Arr.of(1, 2, 0, 3, -1, 0, 4).sort()).isEqualTo(Arr.of(-1, 0, 0, 1, 2, 3, 4));
    }

    @Test
    void arr_sort() {
        assertThat(ARR.sort()).isEqualTo(ARR);
    }

    @Test
    void sortBy() {
        assertThat(Arr.of(1, 2, 0, 3, -1, 0, 4).sortBy(Comparator.comparingLong(l -> -l)))
                .isEqualTo(Arr.of(4, 3, 2, 1, 0, 0, -1));
    }

    @Test
    void arr_sortBy() {
        assertThat(ARR.sortBy(Comparator.comparingLong(Math::abs))).isEqualTo(Arr.of(0, 1, -1, 2, -2, 3, -3));
    }

    @Test
    void unique() {
        assertThat(Arr.of(1, 2, 0, 1, -1, 0, 4).unique()).isEqualTo(Arr.of(1, 2, 0, -1, 4));
    }

    @Test
    void unique_whenEmpty() {
        assertThat(Arr.empty().unique()).isEqualTo(Arr.empty());
    }

    @Test
    void arr_unique() {
        assertThat(ARR.unique()).isEqualTo(ARR);
    }

    @Test
    void first() {
        assertThat(Arr.of(1, 2, 0, 1, -1, 0, 4).first(2)).isEqualTo(Arr.of(1, 2));
    }

    @Test
    void first_WhenShorter() {
        assertThat(Arr.of(1, 2).first(3)).isEqualTo(Arr.of(1, 2));
    }

    @Test
    void last() {
        assertThat(Arr.of(1, 2, 0, 1, -1, 0, 4).last(2)).isEqualTo(Arr.of(0, 4));
    }

    @Test
    void last_WhenShorter() {
        assertThat(Arr.of(1, 2).last(3)).isEqualTo(Arr.of(1, 2));
    }

    @Test
    void skip() {
        assertThat(Arr.of(1, 2, 0, 1, -1, 0).skip(2)).isEqualTo(Arr.of(0, 1, -1, 0));
    }

    @Test
    void skip_WhenShorter() {
        assertThat(Arr.of(1, 2).skip(3)).isEqualTo(Arr.empty());
    }

    @Test
    void takeWhile() {
        assertThat(Arr.of(1, 2, 0, 1, -1, 0).takeWhile(l -> l > 0)).isEqualTo(Arr.of(1, 2));
    }

    @Test
    void takeWhile_Fail() {
        assertThat(Arr.of(1, 2).takeWhile(l -> l < 0)).isEqualTo(Arr.empty());
    }

    @Test
    void skipWhile() {
        assertThat(Arr.of(1, 2, 0, 1, -1, 0).skipWhile(l -> l > 0)).isEqualTo(Arr.of(0, 1, -1, 0));
    }

    @Test
    void skipWhile_Fail() {
        assertThat(Arr.of(1, 2).skipWhile(l -> l > 0)).isEqualTo(Arr.empty());
    }

    @Test
    void map() {
        assertThat(Arr.of(1, 2, 0, 1, -1, 0).map(l -> -l)).isEqualTo(Arr.of(-1, -2, 0, -1, 1, 0));
    }

    @Test
    void map_WhenEmpty() {
        assertThat(Arr.empty().map(l -> -l)).isEqualTo(Arr.empty());
    }

    @Test
    void mapWith() {
        assertThat(Arr.of(1, 2, 0, 1, -1, 0)
                .mapWith(Arr.of(1, 2, 3, 4, 5, 6), (a, b) -> b - a))
                .isEqualTo(Arr.of(0, 0, 3, 3, 6, 6));
    }

    @Test
    void mapWith_WhenShorter() {
        assertThat(Arr.of(1, 2, 0, 1, -1, 0)
                .mapWith(Arr.of(1, 2), (a, b) -> b - a))
                .isEqualTo(Arr.of(0, 0));
    }

    @Test
    void prodWith() {
        assertThat(Arr.of(1, 2, 3)
                .prodWith(Arr.of(-1, -2), (a, b) -> b * a))
                .isEqualTo(Arr.of(-1, -2, -2, -4, -3, -6));
    }

    @Test
    void prodUpperTriangleWith() {
        assertThat(Arr.of(1, 2, 3, 4)
                .prodUpperTriangleWith(Arr.of(1, 2, 3, 4), (a, b) -> b * a))
                .isEqualTo(Arr.of(2, 3, 4, 6, 8, 12));
    }

    @Test
    void flatMap() {
        assertThat(Arr.of(1, 2, 3, 4).flatMap(l -> Arr.of(l, -l)))
                .isEqualTo(Arr.of(1, -1, 2, -2, 3, -3, 4, -4));
    }

    @Test
    void flatMap_Removal() {
        assertThat(Arr.of(1, 2, 3, 4).flatMap(l -> l % 2 == 0 ? Arr.of(l) : Arr.empty()))
                .isEqualTo(Arr.of(2, 4));
    }

    @Test
    void equals() {
        assertThat(Arr.of(1, 2, 3, 4)).isEqualTo(Arr.of(1, 2, 3, 4));
    }

    @Test
    void equals_Fail() {
        assertThat(Arr.of(1, 2, 3, 4)).isNotEqualTo(Arr.of(1, 4, 2, 3));
    }

    @Test
    void toArray() {
        assertThat(Arr.of(1, 2, 3, 4).toArray()).isEqualTo(new long[]{1, 2, 3, 4});
    }

    @Test
    void toSet() {
        assertThat(Arr.of(1, 2, 3, 4).toSet()).isEqualTo(Set.of(1L, 2L, 3L, 4L));
    }


}
