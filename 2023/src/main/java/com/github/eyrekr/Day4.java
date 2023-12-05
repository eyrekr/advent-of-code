package com.github.eyrekr;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * <a href="https://adventofcode.com/2023/day/4">...</a>
 * 1) 26426
 * 2) 6227972
 */
class Day4 {

    public static void main(String[] args) throws Exception {
        final Card[] deck = Files.readAllLines(Path.of("src/main/resources/04.txt")).stream().map(Card::new).toArray(Card[]::new);

        for (int cardNumber = 0; cardNumber < deck.length; cardNumber++) {
            final Card card = deck[cardNumber];
            for (int d = 1; d <= card.matchingNumbers && cardNumber + d < deck.length; d++) {
                final Card cardYouWin = deck[cardNumber + d];
                cardYouWin.instances += card.instances; // you win as many times as you have this card
            }
        }

        System.out.printf("SUM POINTS = %d\nSUM INSTANCES = %d\n",
                Arrays.stream(deck).mapToLong(card -> card.points).sum(),
                Arrays.stream(deck).mapToLong(card -> card.instances).sum());
    }

    static class Card {
        final long matchingNumbers;
        final long points;
        long instances = 1;

        Card(final String line) {
            final var numbers = Seq.fromArray(StringUtils.split(line, ":|")).map(Str::longs);
            final var numbersYouHave = numbers.at(1);
            final var winningNumbers = numbers.at(2);
            this.matchingNumbers = numbersYouHave.filter(winningNumbers::has).length;
            this.points = matchingNumbers > 0 ? (long) Math.pow(2, matchingNumbers - 1) : 0;
        }
    }
}
