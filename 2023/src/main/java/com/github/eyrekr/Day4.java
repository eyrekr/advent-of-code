package com.github.eyrekr;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * <a href="https://adventofcode.com/2023/day/4">...</a>
 * 1) 26426
 * 2) 6227972
 */
class Day4 {

    public static void main(String[] args) throws Exception {
        final Card[] deck = Files.readAllLines(Path.of("src/main/resources/04.txt")).stream().map(Card::from).toArray(Card[]::new);

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

        Card(final Collection<String> winningNumbers, final Collection<String> numbersYouHave) {
            this.matchingNumbers = numbersYouHave.stream().filter(winningNumbers::contains).count();
            this.points = matchingNumbers > 0 ? (long) Math.pow(2, matchingNumbers - 1) : 0;
        }

        static Card from(final String line) {
            final String ticket = StringUtils.substringAfter(line, ":");
            final String[] numbers = StringUtils.split(ticket, '|');
            final Set<String> winningNumbers = Set.of(StringUtils.split(numbers[0]));
            final List<String> numbersYouHave = List.of(StringUtils.split(numbers[1]));

            return new Card(winningNumbers, numbersYouHave);
        }
    }
}
