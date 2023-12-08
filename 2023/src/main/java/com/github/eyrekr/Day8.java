package com.github.eyrekr;

import com.github.eyrekr.util.Mth;
import com.github.eyrekr.util.Seq;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * 1) 19241
 * 2) 9606140307013
 */
class Day8 extends AoC<Day8.Input> {

    public static void main(String[] args) {
        new Day8().run(8, Input::from);
    }

    record Input(String navigation, Map<String, Instruction> instructions) {
        static Input from(Seq<String> lines) {
            final String navigation = lines.value;
            final Map<String, Instruction> instructions = lines.tail.tail.map(Instruction::from).print("\n").toMap(Instruction::source);
            return new Input(navigation, instructions);
        }
    }

    record Instruction(String source, String left, String right) {
        static Instruction from(final String input) {
            final String[] data = StringUtils.split(input, " =(),");
            return new Instruction(data[0], data[1], data[2]);
        }
    }

    long star1(final Input input) {
        String current = "AAA";
        int step = 0;
        final int n = input.navigation.length();
        while (!current.equalsIgnoreCase("ZZZ")) {
            current = Objects.equals(input.navigation.charAt(step % n), 'L')
                    ? input.instructions.get(current).left
                    : input.instructions.get(current).right;
            step++;
        }
        return (long) step;
    }

    long star2(Input input) {
        long steps = Seq.fromIterable(input.instructions.keySet())
                .where(key -> key.endsWith("A"))
                .map(start -> steps(start, input.instructions, input.navigation))
                .reduce(1L, Mth::lcm);
        return steps;
    }

    static long steps(final String start, final Map<String, Instruction> instructions, final String navigation) {
        String current = start;
        long step = 0;
        final long n = navigation.length();
        while (!current.endsWith("Z")) {
            final int i = (int) (step % n);
            final boolean left = Objects.equals(navigation.charAt(i), 'L');
            current = left ? instructions.get(current).left : instructions.get(current).right;
            step++;
        }
        return step;
    }

}
