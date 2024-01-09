package com.github.eyrekr.y2022;

import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.math.Algebra;
import com.github.eyrekr.raster.Direction;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;

public class D09 {

    static final P O = new P(0, 0);
    final Arr<Move> moves;

    D09(final String input) {
        moves = Arr.ofLinesFromString(input).map(Move::from);
    }

    long star1() {
        P head = O, tail = O;
        final Set<P> tails = new HashSet<>();
        for (final Move move : moves)
            for (int step = 0; step < move.steps; step++) {
                final P previous = head;
                head = head.move(move.direction);
                if (abs(head.x - tail.x) > 1 || abs(head.y - tail.y) > 1) tail = previous;
                tails.add(tail);
            }
        return tails.size();
    }

    long star2() {
        final P[] rope = new P[10];
        Arrays.fill(rope, O);
        final Set<P> tails = new HashSet<>();
        for (final Move move : moves)
            for (int step = 0; step < move.steps; step++) {
                rope[0] = rope[0].move(move.direction);
                for (int i = 1; i < rope.length; i++) rope[i] = rope[i].moveTo(rope[i - 1]);
                tails.add(rope[9]);
            }
        return tails.size();
    }

    record Move(Direction direction, int steps) {
        static Move from(final String input) {
            return new Move(Direction.fromChar(input.charAt(0)), Integer.parseInt(input.substring(2)));
        }
    }

    record P(int x, int y) {
        P move(final Direction d) {
            return new P(x + d.dx, y + d.dy);
        }

        P moveTo(final P h) {
            int dx = h.x - x, dy = h.y - y;
            return (abs(dx) > 1 || abs(dy) > 1) ? new P(x + Algebra.sgn(dx), y + Algebra.sgn(dy)) : this;
        }
    }

}
