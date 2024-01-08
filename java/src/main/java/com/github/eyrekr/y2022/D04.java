package com.github.eyrekr.y2022;

import com.github.eyrekr.immutable.Int;
import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.immutable.Seq;
import org.apache.commons.lang3.StringUtils;

public class D04 {

    final Seq<AssignmentPair> assignmentPairs;

    D04(final String input) {
        assignmentPairs = Seq.ofLinesFromString(input).map(AssignmentPair::from);
    }

    long star1() {
        return assignmentPairs.where(AssignmentPair::oneContainsAnother).length;
    }

    long star2() {
        return assignmentPairs.where(AssignmentPair::overlaps).length;
    }

    record AssignmentPair(Int i1, Int i2) {
        static AssignmentPair from(final String input) {
            final var l = Longs.fromStringArray(StringUtils.split(input, "-,"));
            return new AssignmentPair(new Int(l.at(0), l.at(1)), new Int(l.at(2), l.at(3)));
        }

        boolean oneContainsAnother() {
            return i1.contains(i2) || i2.contains(i1);
        }

        boolean overlaps() {
            return i1.overlaps(i2);
        }
    }
}
