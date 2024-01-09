package com.github.eyrekr.y2022;

import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.math.Algebra;
import com.github.eyrekr.raster.Direction;
import com.github.eyrekr.raster.P;

import java.util.Arrays;
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
            for (int step = 0; step < move.steps; step++) {
                final P previous = head;
                head = head.translate(move.direction.dx, move.direction.dy);
                if (abs(head.x - tail.x) > 1 || abs(head.y - tail.y) > 1) tail = previous;
                tails.add(tail);
            }
        return tails.size();
    }

    long star2() {
        final P[] rope = new P[10];
        Arrays.fill(rope, P.O);
        final Set<P> tails = new HashSet<>();
        tails.add(P.O);
        for (final Move move : moves)
            for (int step = 0; step < move.steps; step++) {
                rope[0] = rope[0].translate(move.direction.dx, move.direction.dy);
                for (int i = 1; i < rope.length; i++) {
                    long dx = rope[i - 1].x - rope[i].x, dy = rope[i - 1].y - rope[i].y;
                    if (abs(dx) > 1 || abs(dy) > 1) rope[i] = rope[i].translate(Algebra.sgn(dx), Algebra.sgn(dy));
                    tails.add(rope[9]);
                }
            }
        return tails.size();
    }

    record Move(Direction direction, int steps) {
        static Move from(final String input) {
            return new Move(Direction.fromChar(input.charAt(0)), Integer.parseInt(input.substring(2)));
        }
    }

}
