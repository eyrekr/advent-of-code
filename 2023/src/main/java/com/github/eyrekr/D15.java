package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import org.apache.commons.lang3.StringUtils;

/**
 * https://adventofcode.com/2023/day/15
 * 1) 515495
 * 2)
 */
class D15 extends AoC {

    final Seq<String> words;

    D15(final String input) {
        super(input);
        words = Seq.fromArray(StringUtils.split(input, ','));
    }

    long star1() {
        return words.map(word->Seq.ofCharactersFromString(word).reduce(0, (hash, ch)->(hash+ch)*17 % 256)).reduce(Integer::sum);
    }

    long star2() {
        return 0L;
    }
}