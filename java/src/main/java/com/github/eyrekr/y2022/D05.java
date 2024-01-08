package com.github.eyrekr.y2022;

import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.mutable.Arr;

public class D05 {

    final Arr<Arr<String>> stacks;
    final Seq<Instruction> instructions;

    D05(final Seq<Seq<String>> stacks, final String input) {
        this.stacks = stacks.map(Seq::toMutableArr).toMutableArr();
        this.instructions = Seq.ofLinesFromString(input.split("\n\n")[1]).map(Instruction::from);
    }

    String star1() {
        for (final Instruction i : instructions) {
            for (int l = 0; l < i.n; l++) {
                final String crate = stacks.at(i.source).removeFirst(); // first = top
                stacks.at(i.target).addFirst(crate);
            }
        }
        return stacks.skip(1).map(Arr::getFirst).reduce(new StringBuilder(), StringBuilder::append).toString();
    }

    String star2() {
        for (final Instruction i : instructions) {
            final Arr<String> crates = stacks.at(i.source).removeFirst(i.n);
            stacks.at(i.target).addAllFirst(crates);

        }
        return stacks.skip(1).map(Arr::getFirst).reduce(new StringBuilder(), StringBuilder::append).toString();
    }

    record Instruction(int n, int source, int target) {
        static Instruction from(final String input) {
            final var l = Longs.fromString(input);
            return new Instruction((int) l.at(0), (int) l.at(1), (int) l.at(2));
        }
    }
}
