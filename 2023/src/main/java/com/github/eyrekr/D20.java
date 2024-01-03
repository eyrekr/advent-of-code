package com.github.eyrekr;

import com.github.eyrekr.math.Algebra;
import com.github.eyrekr.immutable.Seq;
import com.github.eyrekr.output.Out;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * https://adventofcode.com/2023/day/20
 * 1) 899848294
 * 2) 247454898168563
 */
class D20 extends AoC {

    static final String BROADCASTER = "broadcaster";
    static final String TERMINAL = "rx";

    final Seq<Module> modules;
    final Map<String, Module> moduleByName;
    final Module broadcaster;

    D20(final String input) {
        super(input);
        modules = lines.mapWith(Seq.range(0, lines.length), Module::from);
        moduleByName = modules.indexBy(module -> module.name);
        broadcaster = moduleByName.get(BROADCASTER);

        // add the "output" modules so that we do not have to constantly deal with NPE
        modules.each(module -> module.out
                .where(out -> moduleByName.get(out) == null)
                .each(name -> moduleByName.put(name, new Module(Type.Output, -1, name, Seq.empty()))));
        // update activations of CONJUNCTIONS modules based on the modules that feed them
        modules.each(module -> module.out.map(moduleByName::get)
                .where(Objects::nonNull)
                .where(out -> out.type == Type.Conjunction)
                .each(conjunction -> conjunction.activation |= 1L << module.id));
    }

    long star1() {
        long numberOfLowPulses = 0, numerOfHighPulses = 0;

        for (int i = 0; i < 1000; i++) {
            final LinkedList<Pulse> pulsesToProcess = new LinkedList<>();
            pulsesToProcess.addFirst(new Pulse(null, broadcaster, 0));

            while (!pulsesToProcess.isEmpty()) {
                final Pulse pulse = pulsesToProcess.removeFirst();
                final Module module = pulse.target;
                final boolean low = pulse.value == 0;
                final boolean high = !low;

                if (low) numberOfLowPulses++;
                if (high) numerOfHighPulses++;

                final Consumer<Integer> emitPulse = value -> module.out
                        .map(moduleByName::get)
                        .map(nextTarget -> new Pulse(module, nextTarget, value))
                        .each(pulsesToProcess::addLast);

                switch (module.type) { // SIGNAL LOGIC
                    case Broadcaster -> emitPulse.accept(0);
                    case FlipFlop -> {
                        if (low) {
                            emitPulse.accept(module.isActivated() ? 0 : 1);
                            module.update(pulse);
                        }
                    }
                    case Conjunction -> {
                        module.update(pulse);
                        emitPulse.accept(module.isActivated() ? 0 : 1);
                    }
                }
            }
        }

        return numberOfLowPulses * numerOfHighPulses;
    }


    long star2() {
        final Module secondLast = modules.firstWhere(module -> module.out.has(TERMINAL)).get();
        final Seq<Module> modulesWhoseCyclesWeWatch = modules.where(module -> module.out.has(secondLast.name));
        final Map<Module, Long> cycleLength = new HashMap<>();

        for (long buttonPressCount = 1; ; buttonPressCount++) {

            final LinkedList<Pulse> pulsesToProcess = new LinkedList<>();
            pulsesToProcess.addFirst(new Pulse(null, broadcaster, 0));

            while (!pulsesToProcess.isEmpty()) {
                final Pulse pulse = pulsesToProcess.removeFirst();
                final Module module = pulse.target;
                final boolean low = pulse.value == 0;
                final boolean high = !low;

                if (high && module == secondLast) { // SENDING HIGH TO THE 2ND LAST => WATCH THE CYCLE
                    if (!cycleLength.containsKey(pulse.source)) {
                        Out.print("@c%s@@ --high-> @c%s@@ during button press @r#%d@@\n", pulse.source.name, module.name, buttonPressCount);
                        cycleLength.put(pulse.source, buttonPressCount);
                    }
                    if (cycleLength.size() == modulesWhoseCyclesWeWatch.length) {
                        // all cycles collected => cycles will fire up 1st time together when LCM
                        return Seq.fromIterable(cycleLength.values()).reduce(Algebra::lcm).get();
                    }
                }

                final Consumer<Integer> emit = value -> module.out
                        .map(moduleByName::get)
                        .map(nextTarget -> new Pulse(module, nextTarget, value))
                        .each(pulsesToProcess::addLast);

                switch (module.type) { // SIGNAL LOGIC
                    case Broadcaster -> emit.accept(0);
                    case FlipFlop -> {
                        if (low) {
                            emit.accept(module.isActivated() ? 0 : 1);
                            module.update(pulse);
                        }
                    }
                    case Conjunction -> {
                        module.update(pulse);
                        emit.accept(module.isActivated() ? 0 : 1);
                    }
                }
            }
        }
    }


    enum Type {
        FlipFlop, Conjunction, Broadcaster, Output;

        static Type from(final String input) {
            if (BROADCASTER.equals(input)) return Broadcaster;
            if (input.startsWith("%")) return FlipFlop;
            if (input.startsWith("&")) return Conjunction;
            return Output;
        }
    }

    static final class Module {
        final Type type;
        final int id;
        final String name;
        final Seq<String> out;
        long state = 0L;
        long activation = 0L;

        private Module(final Type type, final int id, final String name, final Seq<String> out) {
            this.type = type;
            this.id = id;
            this.name = name;
            this.out = out;
        }

        boolean isActivated() {
            return switch (type) {
                case FlipFlop -> state != 0;
                case Conjunction -> state == activation;
                default -> true;
            };
        }

        Module update(final Pulse pulse) {
            switch (type) {
                case FlipFlop -> {
                    if (pulse.value == 0) {
                        state = state != 0 ? 0 : 1;
                    }
                }
                case Conjunction -> {
                    state = (pulse.value == 0) ? state & ~(1L << pulse.source.id) : state | (1L << pulse.source.id);
                }
                default -> throw new IllegalStateException("Cannot update " + type);
            }
            return this;
        }

        static Module from(final String input, final int id) { //%a -> inv, con
            final var block = StringUtils.split(input, "->");
            block[0] = block[0].trim();
            final Type type = Type.from(block[0]);
            return new Module(
                    type,
                    id,
                    switch (type) {
                        case FlipFlop, Conjunction -> StringUtils.substring(block[0], 1);
                        case Broadcaster, Output -> block[0];
                    },
                    Seq.fromArray(StringUtils.split(block[1], ", ")));
        }
    }

    record Pulse(Module source, Module target, int value) {
    }

}