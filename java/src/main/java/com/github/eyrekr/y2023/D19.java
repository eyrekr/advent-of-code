package com.github.eyrekr.y2023;

import com.github.eyrekr.immutable.Seq;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * https://adventofcode.com/2023/day/19
 * 1) 350678
 * 2) 124831893423809
 */
class D19 {

    static final int MIN = 1;
    static final int MAX = 4_000;
    static final String ACCEPT = "A";
    static final String REJECT = "R";
    static final char TERMINAL = '*';
    static final String INITIAL_WORKFLOW_NAME = "in";


    final Map<String, Workflow> workflows;
    final Seq<XMAS> xmas;

    D19(final String input) {
        final var block = input.split("\n\n");
        workflows = Seq.ofLinesFromString(block[0]).map(Workflow::from).indexBy(Workflow::name);
        xmas = Seq.ofLinesFromString(block[1]).map(XMAS::from);
    }

    long star1() {
        return xmas.where(x -> runThrough(workflows, x)).map(XMAS::value).reduce(Long::sum).get();
    }

    long star2() {
        final LinkedList<State> queue = new LinkedList<>();
        queue.addFirst(new State(
                ImmutableMap.of(
                        'x', Interval.whole(),
                        'm', Interval.whole(),
                        'a', Interval.whole(),
                        's', Interval.whole()),
                "in"));
        Seq<State> accepted = Seq.empty();

        while (!queue.isEmpty()) {
            State state = queue.remove();
            if (ACCEPT.equalsIgnoreCase(state.workflowName)) {
                accepted = accepted.addFirst(state);
            } else if (REJECT.equalsIgnoreCase(state.workflowName)) {
                // do nothing, the whole state is rejected
            } else {
                final Workflow workflow = workflows.get(state.workflowName);
                for (final Rule rule : workflow.rules) {
                    if (state == null) break;
                    switch (rule.comparator) {
                        case TERMINAL -> { // this is the last state that applies to everything:    pqr | A | R
                            queue.addLast(state.withWorkflow(rule.nextWorkflowName));
                        }
                        case '<' -> { // s<100:pqr
                            final Interval interval = state.intervals.get(rule.property);
                            final Interval below = interval.intersect(MIN, rule.value - 1);
                            if (below != null) {
                                queue.addLast(state.withInterval(rule.property, below).withWorkflow(rule.nextWorkflowName));
                            }
                            final Interval aboveOrEqual = interval.intersect(rule.value, MAX);
                            if (aboveOrEqual != null) {
                                state = state.withInterval(rule.property, aboveOrEqual);
                            } else {
                                state = null;
                            }
                        }
                        case '>' -> { // s>100:pqr
                            final Interval interval = state.intervals.get(rule.property);
                            final Interval above = interval.intersect(rule.value + 1, MAX);
                            if (above != null) {
                                queue.addLast(state.withInterval(rule.property, above).withWorkflow(rule.nextWorkflowName));
                            }
                            final Interval belowOrEqual = interval.intersect(MIN, rule.value);
                            if (belowOrEqual != null) {
                                state = state.withInterval(rule.property, belowOrEqual);
                            } else {
                                state = null;
                            }
                        }
                    }
                }
            }
        }
        return accepted.map(State::combinations).reduce(Long::sum).get();
    }

    boolean runThrough(final Map<String, Workflow> workflows, final XMAS xmas) {
        String nextWorkflowName = INITIAL_WORKFLOW_NAME;
        while (true) {
            final Workflow workflow = workflows.get(nextWorkflowName);
            final Rule rule = workflow.apply(xmas);
            if (ACCEPT.equalsIgnoreCase(rule.nextWorkflowName)) return true;
            if (REJECT.equalsIgnoreCase(rule.nextWorkflowName)) return false;
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
            return rules.firstWhere(rule -> rule.applicable(xmas)).get();
        }
    }

    record Rule(char property, char comparator, int value, String nextWorkflowName) {
        static Rule from(final String line) { //s<537:gd  |  x>2440:R  |  A
            final var l = StringUtils.split(line, ":");
            if (l.length == 1) { // last on the line, no conditions
                return new Rule(TERMINAL, TERMINAL, 0, l[0]);
            } else {
                final int value = Seq.ofNumbersFromString(l[0]).value.intValue();
                return new Rule(l[0].charAt(0), l[0].charAt(1), value, l[1]);
            }
        }

        boolean applicable(final XMAS xmas) {
            if (comparator == TERMINAL) return true;
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
            final var l = Seq.ofNumbersFromString(line);
            return new XMAS(l.value, l.tail.value, l.tail.tail.value, l.tail.tail.tail.value);
        }

        long value() {
            return x + m + a + s;
        }
    }

    record State(ImmutableMap<Character, Interval> intervals, String workflowName) {
        long combinations() {
            return intervals.values().stream().mapToLong(Interval::size).reduce(1L, (a, b) -> a * b);
        }

        State withWorkflow(final String nextWorkflowName) {
            return new State(intervals, nextWorkflowName);
        }

        State withInterval(final char property, final Interval interval) {
            final Map<Character, Interval> map = new HashMap<>(intervals);
            map.put(property, interval);
            return new State(ImmutableMap.copyOf(map), workflowName);
        }
    }

    record Interval(int a, int b) {
        static Interval whole() {
            return new Interval(MIN, MAX);
        }

        Interval intersect(final int a, final int b) {
            final boolean overlap = (this.a <= a && a <= this.b) || (this.a <= b && b <= this.b);
            return overlap ? new Interval(Math.max(this.a, a), Math.min(this.b, b)) : null;
        }

        int size() {
            return b - a + 1;
        }
    }
}