package com.github.eyrekr;

import com.github.eyrekr.util.Mth;
import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * https://adventofcode.com/2023/day/20
 * 1) 899848294
 * 2)
 */
class D20 extends AoC {

    static final String BROADCASTER = "broadcaster";
    static final String TERMINAL = "rx";

    final Seq<Module> modules;
    final Map<String, Module> map;
    final Module broadcaster;

    D20(final String input) {
        super(input);
        modules = lines.mapWith(Seq.range(0, lines.length), Module::from);
        map = modules.indexBy(module -> module.name);
        broadcaster = map.get(BROADCASTER);

        // add the "output" modules so that we do not have to constantly deal with NPE
        modules.each(module -> module.out
                .where(out -> map.get(out) == null)
                .each(name -> map.put(name, new Module(Type.Output, -1, name, Seq.empty()))));
        // update activations of conjuctions modules (based on the number of inputs)
        modules.each(module -> module.out.map(map::get)
                .where(Objects::nonNull)
                .where(out -> out.type == Type.Conjunction)
                .each(conjunction -> conjunction.activation |= 1L << module.id));
    }

    long star1() {
        Pulses pulses = new Pulses(0, 0);
        for (int i = 0; i < 1000; i++) pulses = pulses.plus(run());
        return pulses.low * pulses.high;
    }

    Pulses run() {
        long numberOfLowPulses = 0, numerOfHighPulses = 0;
        final LinkedList<Pulse> pulsesToProcess = new LinkedList<>();
        pulsesToProcess.addFirst(new Pulse(null, broadcaster, 0));

        final Module secondLast = modules.firstWhere(module -> module.out.has(TERMINAL));
        Str.print("Second last = %s\n", secondLast);


        while (!pulsesToProcess.isEmpty()) {
            final Pulse pulse = pulsesToProcess.removeFirst();
            final Module module = pulse.target;
            final boolean low = pulse.value == 0;
            final boolean high = !low;

            if (low) numberOfLowPulses++;
            if (high) numerOfHighPulses++;

            final Consumer<Integer> emit = value -> module.out
                    .map(map::get)
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

        return new Pulses(numberOfLowPulses, numerOfHighPulses);
    }

    record Pulses(long low, long high) {
        Pulses plus(final Pulses that) {
            return new Pulses(low + that.low, high + that.high);
        }
    }


//    String encodeStateOfModules(final Seq<Module> modules) {
//        final long flips = modules.where(module -> module.type == Type.FlipFlop)
//                .reduce(0L, (acc, module) -> module.state != 0 ? (acc | (1L << module.id)) : acc);
//        final String cons = modules.where(module -> module.type == Type.Conjunction)
//                .map(module -> module.state)
//                .map(Long::toHexString)
//                .reduce("", (acc, state) -> acc + "." + state);
//        return Long.toHexString(flips) + cons;
//    }

    long star2() {

        final Module secondLast = modules.firstWhere(module -> module.out.has(TERMINAL));
        final Seq<Module> modulesWhoseCyclesWeWatch = modules.where(module -> module.out.has(secondLast.name));
        modulesWhoseCyclesWeWatch.print(" ", module -> module.name);
        final Map<Module, Long> cycleLength = new HashMap<>();

        for (long buttonPressCount = 1; ; buttonPressCount++) {


            final LinkedList<Pulse> pulsesToProcess = new LinkedList<>();
            pulsesToProcess.addFirst(new Pulse(null, broadcaster, 0));

            while (!pulsesToProcess.isEmpty()) {
                final Pulse pulse = pulsesToProcess.removeFirst();
                final Module module = pulse.target;
                final boolean low = pulse.value == 0;
                final boolean high = !low;

                if (modulesWhoseCyclesWeWatch.has(pulse.source) && high) { // sending HIGH to 2ND LAST = CYCLE WE WATCH
                    Str.print("@g%s@@ emits @rHIGH@@ at button press @c%d@@\n", pulse.source.name, buttonPressCount);
                    if (!cycleLength.containsKey(pulse.source)) {
                        cycleLength.put(pulse.source, buttonPressCount);
                    }
                    if (cycleLength.size() == modulesWhoseCyclesWeWatch.length) {
                        // all cycles collected => cycles will fire up when LCM
                        return Seq.fromIterable(cycleLength.values()).reduce(Mth::lcm);
                    }
                }

                final Consumer<Integer> emit = value -> module.out
                        .map(map::get)
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
                    state = (pulse.value == 0)
                            ? state & ~(1L << pulse.source.id)
                            : state | (1L << pulse.source.id);
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

        @Override
        public String toString() {
            return String.format("%3d %-12s %-20s %x|%x -> %s", id, type, name, state, activation, out);
        }
    }

    record Pulse(Module source, Module target, int value) {
    }

}