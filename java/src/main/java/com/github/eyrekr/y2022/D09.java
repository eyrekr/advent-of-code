package com.github.eyrekr.y2022;

import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.raster.Direction;
import com.github.eyrekr.raster.P;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;

public class D09 {

    final Arr<Move> moves;

    D09(final String input) {
        moves = Arr.ofLinesFromString(input).map(Move::from);
    }

    long star1() {
        P head = P.O, tail = P.O;
        final Set<P> tails = new HashSet<>();
        tails.add(tail);
        for (final Move move : moves)
            for (int i = 0; i < move.steps; i++) {
                final P previous = head;
                head = head.translate(move.direction.dx, move.direction.dy);
                if (abs(head.x - tail.x) > 1 || abs(head.y - tail.y) > 1) tail = previous;
                tails.add(tail);
            }
        return tails.size();
    }

    long star2() {
        return 0L;
    }

    record Move(Direction direction, int steps) {
        static Move from(final String input) {
            return new Move(Direction.fromChar(input.charAt(0)), Integer.parseInt(input.substring(2)));
        }
    }
}
