package com.github.eyrekr;

import com.github.eyrekr.util.Mth;
import com.github.eyrekr.util.Seq;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 1) 19241
 * 2) 9606140307013
 */
class Day8 {

    static String SAMPLE = """
            RL
                        
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
            """;

    static String SAMPLE2 = """
            LLR
                        
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
            """;

    static String SAMPLE3 = """
            LR
                        
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
            """;

    public static void main(String[] args) throws Exception {
        //final Seq<String> lines = Seq.fromArray(SAMPLE3.split("\n"));
        final Seq<String> lines = Seq.fromArray(Files.readString(Path.of("src/main/resources/08.txt")).split("\n"));
        final String navigation = lines.value;
        final Map<String, Instruction> instructions = new HashMap<>();
        lines.tail.tail.map(Instruction::from).print("\n").each(instruction -> instructions.put(instruction.source, instruction));
        //System.out.println(star1(instructions, navigation));
        System.out.println(star2(instructions, navigation));
    }

    static int star1(final Map<String, Instruction> instructions, final String navigation) {
        String current = "AAA";
        int step = 0;
        final int n = navigation.length();
        while (!current.equalsIgnoreCase("ZZZ")) {
            current = Objects.equals(navigation.charAt(step % n), 'L')
                    ? instructions.get(current).left
                    : instructions.get(current).right;
            step++;
        }
        return step;
    }

    static long star2(final Map<String, Instruction> instructions, final String navigation) {
        long steps = Seq.fromIterable(instructions.keySet())
                .where(key -> key.endsWith("A"))
                .map(start-> steps(start, instructions, navigation))
                .reduce(1L, Mth::lcm);
        return steps;
    }

    static long steps(final String start, final Map<String, Instruction> instructions, final String navigation) {
        String current = start;
        long step = 0;
        final long n = navigation.length();
        while (!current.endsWith("Z")) {
            final int i =(int) (step %n);
            final boolean left = Objects.equals(navigation.charAt(i), 'L');
            current = left ? instructions.get(current).left : instructions.get(current).right;
            step++;
        }
        return step;
    }

    record Instruction(String source, String left, String right) {
        static Instruction from(final String input) {
            final String[] data = StringUtils.split(input, " =(),");
            return new Instruction(data[0], data[1], data[2]);
        }
    }
}
