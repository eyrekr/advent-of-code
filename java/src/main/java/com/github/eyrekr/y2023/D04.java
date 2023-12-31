package com.github.eyrekr.y2023;

import com.github.eyrekr.immutable.Seq;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * https://adventofcode.com/2023/day/4
 * 1) 26426
 * 2) 6227972
 */
class D04 {

    final Card[] deck;

    D04(final String input) {
        deck = Seq.ofLinesFromString(input).map(Card::new).toArray(Card[]::new);
    }

    long star1() {
        return Arrays.stream(deck).mapToLong(card -> card.points).sum();
    }

    long star2() {
        for (int cardNumber = 0; cardNumber < deck.length; cardNumber++) {
            final Card card = deck[cardNumber];
            for (int d = 1; d <= card.matchingNumbers && cardNumber + d < deck.length; d++) {
                final Card cardYouWin = deck[cardNumber + d];
                cardYouWin.instances += card.instances; // you win as many times as you have this card
            }
        }
        return Arrays.stream(deck).mapToLong(card -> card.instances).sum();
    }

    static class Card {
        final long matchingNumbers;
        final long points;
        long instances = 1;

        Card(final String line) {
            final var numbers = Seq.fromArray(StringUtils.split(line, ":|")).map(Seq::ofNumbersFromString);
            final var numbersYouHave = numbers.at(1);
            final var winningNumbers = numbers.at(2);
            this.matchingNumbers = numbersYouHave.where(winningNumbers::has).length;
            this.points = matchingNumbers > 0 ? (long) Math.pow(2, matchingNumbers - 1) : 0;
        }
    }
}
