package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.Map;

/**
 * https://adventofcode.com/2023/day/19
 * 1) 350678
 * 2) 124831893423809
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
        return xmas.where(x -> runThrough(workflows, x)).map(XMAS::value).reduce(Long::sum);
    }

    long star2() {
        final LinkedList<State> queue = new LinkedList<>();
        queue.addFirst(new State(Interval.whole(), Interval.whole(), Interval.whole(), Interval.whole(), "in"));
        Seq<State> accepted = Seq.empty();
        while (!queue.isEmpty()) {
            State state = queue.remove();
            if ("A".equals(state.workflowName)) {
                accepted = accepted.addFirst(state);
            } else if ("R".equals(state.workflowName)) {
                // do nothing, the whole state is rejected
            } else {
                final Workflow workflow = workflows.get(state.workflowName);
                for (final Rule rule : workflow.rules) {
                    if (state == null) break;
                    switch (rule.comparator) {
                        case '*' -> { // this is the last state that applies to everything:    pqr | A | R
                            queue.addLast(new State(state.x, state.m, state.a, state.s, rule.nextWorkflowName));
                        }
                        case '<' -> { // s<100:pqr
                            switch (rule.property) {
                                case 'x' -> {
                                    final Interval below = state.x.intersect(1, rule.value - 1);
                                    if (below != null) {
                                        queue.addLast(new State(below, state.m, state.a, state.s, rule.nextWorkflowName));
                                    }
                                    final Interval above = state.x.intersect(rule.value, 4000);
                                    if (above != null) {
                                        state = new State(above, state.m, state.a, state.s, state.workflowName);
                                    } else {
                                        state = null;
                                    }
                                }
                                case 'm' -> {
                                    final Interval below = state.m.intersect(1, rule.value - 1);
                                    if (below != null) {
                                        queue.addLast(new State(state.x, below, state.a, state.s, rule.nextWorkflowName));
                                    }
                                    final Interval above = state.m.intersect(rule.value, 4000);
                                    if (above != null) {
                                        state = new State(state.x, above, state.a, state.s, state.workflowName);
                                    } else {
                                        state = null;
                                    }
                                }
                                case 'a' -> {
                                    final Interval below = state.a.intersect(1, rule.value - 1);
                                    if (below != null) {
                                        queue.addLast(new State(state.x, state.m, below, state.s, rule.nextWorkflowName));
                                    }
                                    final Interval above = state.a.intersect(rule.value, 4000);
                                    if (above != null) {
                                        state = new State(state.x, state.m, above, state.s, state.workflowName);
                                    } else {
                                        state = null;
                                    }
                                }
                                case 's' -> {
                                    final Interval below = state.s.intersect(1, rule.value - 1);
                                    if (below != null) {
                                        queue.addLast(new State(state.x, state.m, state.a, below, rule.nextWorkflowName));
                                    }
                                    final Interval above = state.s.intersect(rule.value, 4000);
                                    if (above != null) {
                                        state = new State(state.x, state.m, state.a, above, state.workflowName);
                                    } else {
                                        state = null;
                                    }
                                }
                            }
                        }
                        case '>' -> { // s>100:pqr
                            switch (rule.property) {
                                case 'x' -> {
                                    final Interval below = state.x.intersect(rule.value + 1, 4000);
                                    if (below != null) {
                                        queue.addLast(new State(below, state.m, state.a, state.s, rule.nextWorkflowName));
                                    }
                                    final Interval above = state.x.intersect(1, rule.value);
                                    if (above != null) {
                                        state = new State(above, state.m, state.a, state.s, state.workflowName);
                                    } else {
                                        state = null;
                                    }
                                }
                                case 'm' -> {
                                    final Interval below = state.m.intersect(rule.value + 1, 4000);
                                    if (below != null) {
                                        queue.addLast(new State(state.x, below, state.a, state.s, rule.nextWorkflowName));
                                    }
                                    final Interval above = state.m.intersect(0, rule.value);
                                    if (above != null) {
                                        state = new State(state.x, above, state.a, state.s, state.workflowName);
                                    } else {
                                        state = null;
                                    }
                                }
                                case 'a' -> {
                                    final Interval below = state.a.intersect(rule.value + 1, 4000);
                                    if (below != null) {
                                        queue.addLast(new State(state.x, state.m, below, state.s, rule.nextWorkflowName));
                                    }
                                    final Interval above = state.a.intersect(1, rule.value);
                                    if (above != null) {
                                        state = new State(state.x, state.m, above, state.s, state.workflowName);
                                    } else {
                                        state = null;
                                    }
                                }
                                case 's' -> {
                                    final Interval below = state.s.intersect(rule.value + 1, 4000);
                                    if (below != null) {
                                        queue.addLast(new State(state.x, state.m, state.a, below, rule.nextWorkflowName));
                                    }
                                    final Interval above = state.s.intersect(1, rule.value);
                                    if (above != null) {
                                        state = new State(state.x, state.m, state.a, above, state.workflowName);
                                    } else {
                                        state = null;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return accepted.map(State::combinations).reduce(Long::sum);
    }

    boolean runThrough(final Map<String, Workflow> workflows, final XMAS xmas) {
        String nextWorkflowName = "in";
        while (true) {
            final Workflow workflow = workflows.get(nextWorkflowName);
            final Rule rule = workflow.apply(xmas);
            if (rule.accept) return true;
            if (rule.reject) return false;
            nextWorkflowName = rule.nextWorkflowName;
        }
    }

    record Workflow(String name, Seq<Rule> rules) {
        static Workflow from(final String line) { //rfg{s<537:gd,x>2440:R,A}
            final var l = StringUtils.split(line, "{}");
            final var r = Seq.fromArray(StringUtils.split(l[1], ",")).map(Rule::from);
            return new Workflow(l[0], r);
        }

        Rule apply(final XMAS xmas) {
            return rules.firstWhere(rule -> rule.applicable(xmas));
        }
    }

    record Rule(char property, char comparator, int value, String nextWorkflowName, boolean accept, boolean reject) {
        static Rule from(final String line) { //s<537:gd  |  x>2440:R  |  A
            final var l = StringUtils.split(line, ":");
            if (l.length == 1) { // last on the line, no conditions
                return new Rule('*', '*', 0, l[0], "A".equals(l[0]), "R".equals(l[0]));
            } else {
                final int value = Str.longs(l[0]).value.intValue();
                return new Rule(l[0].charAt(0), l[0].charAt(1), value, l[1], "A".equals(l[1]), "R".equals(l[1]));
            }
        }

        boolean applicable(final XMAS xmas) {
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

    record State(Interval x, Interval m, Interval a, Interval s, String workflowName) {
        long combinations() {
            return x.size() * m.size() * a.size() * s.size();
        }
    }

    record Interval(int a, int b) {
        static Interval whole() {
            return new Interval(1, 4000);
        }

        Interval intersect(final int a, final int b) {
            final boolean overlap = (this.a <= a && a <= this.b) || (this.a <= b && b <= this.b);
            return overlap ? new Interval(Math.max(this.a, a), Math.min(this.b, b)) : null;
        }

        long size() {
            return b - a + 1; // interval 1--4 has size 4
        }
    }
}