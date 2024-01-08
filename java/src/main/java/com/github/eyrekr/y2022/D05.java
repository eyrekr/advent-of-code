package com.github.eyrekr.y2022;

import com.github.eyrekr.immutable.Longs;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.output.Out;

import java.util.Arrays;

public class D05 {

    final Seq<String>[] stacks;
    final Seq<Instruction> instructions;

    D05(final Seq<String>[] stacks, final String input) {
        this.stacks = Arrays.copyOf(stacks, stacks.length);
        this.instructions = Seq.ofLinesFromString(input.split("\n\n")[1]).map(Instruction::from);
    }

    String star1() {
        Out.print("INITIAL STATE\n");
        print();

        for (final Instruction i : instructions) {
            Out.print("Move @C%d@@ from @C%d@@ to @C%d@@\n", i.n, i.source, i.target);
            for (int l = 0; l < i.n; l++) {
                stacks[i.target] = stacks[i.target].addFirst(stacks[i.source].value);
                stacks[i.source] = stacks[i.source].tail;
            }
            print();
        }

        String r = "";
        for (final Seq<String> stack : stacks) {
            if (stack.value != null) r += stack.value;
        }
        return r;
    }

    String star2() {
        return "";
    }

    void print() {
        for (int l = 1; l < stacks.length; l++) {
            Out.print(" %d  ", l);
            stacks[l].reverse().print();
        }
    }

    record Instruction(int n, int source, int target) {
        static Instruction from(final String input) {
            final var l = Longs.fromString(input);
            return new Instruction((int) l.at(0), (int) l.at(1), (int) l.at(2));
        }
    }
}
