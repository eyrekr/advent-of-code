package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * https://adventofcode.com/2023/day/19
 * 1)
 * 2)
 */
class D19 extends AoC {

    final Map<String, Workflow> workflows;
    final Seq<XMAS> xmas;

    D19(final String input) {
        super(input);
        final var block = input.split("\n\n");
        workflows = Seq.ofLinesFromString(block[0]).map(Workflow::from).indexBy(Workflow::name);
        xmas = Seq.ofLinesFromString(block[1]).map(XMAS::from);
    }

    long star1() {
        workflows.values().forEach(System.out::println);
        xmas.print("\n");
        return xmas.where(x -> runThrough(workflows, x)).map(XMAS::value).reduce(Long::sum);
    }

    long star2() {
        return 0L;
    }

    boolean runThrough(final Map<String, Workflow> workflows, final XMAS xmas) {
        String nextWorkflowName = "in";
        while (true) {
            final Workflow workflow = workflows.get(nextWorkflowName);
            final Rule rule = workflow.apply(xmas);
            if (rule.accept) return true;
            if (rule.reject) return false;
            nextWorkflowName = rule.next;
        }
    }

    record Workflow(String name, Seq<Rule> rules) {
        static Workflow from(final String line) { //rfg{s<537:gd,x>2440:R,A}
            final var l = StringUtils.split(line, "{}");
            final var r = Seq.fromArray(StringUtils.split(l[1], ",")).map(Rule::from);
            return new Workflow(l[0], r);
        }

        Rule apply(final XMAS xmas) {
            return rules.firstWhere(rule -> rule.matches(xmas));
        }
    }

    record Rule(char property, char comparator, long value, String next, boolean accept, boolean reject) {
        static Rule from(final String line) { //s<537:gd  |  x>2440:R  |  A
            final var l = StringUtils.split(line, ":");
            if (l.length == 1) { // last on the line, no conditions
                return new Rule('*', '*', 0, l[0], "A".equals(l[0]), "R".equals(l[0]));
            } else {
                final long value = Str.longs(l[0]).value;
                return new Rule(l[0].charAt(0), l[0].charAt(1), value, l[1], "A".equals(l[1]), "R".equals(l[1]));
            }
        }

        boolean matches(final XMAS xmas) {
            if (comparator == '*') return true;
            final long lo, hi;
            if (comparator == '<') {
                lo = 0;
                hi = value;
            } else {
                lo = value + 1;
                hi = Long.MAX_VALUE;
            }
            return switch (property) {
                case 'x' -> xmas.x >= lo && xmas.x <= hi;
                case 'm' -> xmas.m >= lo && xmas.m <= hi;
                case 'a' -> xmas.a >= lo && xmas.a <= hi;
                case 's' -> xmas.s >= lo && xmas.s <= hi;
                default -> throw new IllegalStateException(xmas.toString());
            };
        }
    }

    record XMAS(long x, long m, long a, long s) {
        static XMAS from(final String line) { //{x=787,m=2655,a=1222,s=2876}
            final var l = Str.longs(line);
            return new XMAS(l.value, l.tail.value, l.tail.tail.value, l.tail.tail.tail.value);
        }

        long value() {
            return x + m + a + s;
        }
    }
}