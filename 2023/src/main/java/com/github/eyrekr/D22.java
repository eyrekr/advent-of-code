package com.github.eyrekr;

import com.github.eyrekr.util.Seq;
import com.github.eyrekr.util.Str;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * https://adventofcode.com/2023/day/22
 * 1) 418
 * 2) 70702
 */
class D22 extends AoC {
    static final int GROUND = 0;

    final Seq<Brick> bricks;
    final Brick bounds;

    D22(final String input) {
        super(input);
        this.bricks = lines.carryMap(new AtomicInteger(GROUND), Brick::from).sortedBy(Brick::z0);
        this.bounds = bricks.reduce(
                new Brick(0, 0, 0, 0, 0, 0, 0),
                (volume, brick) -> new Brick(0,
                        Math.min(volume.x0, brick.x0), Math.min(volume.y0, brick.y0), Math.min(volume.z0, brick.z0),
                        Math.max(volume.x1, brick.x1), Math.max(volume.y1, brick.y1), Math.max(volume.z1, brick.z1)));
    }

    @Override
    long star1() {
        final int[][] zbuffer = new int[bounds.x1 + 1][bounds.y1 + 1];
        final int[][] brickOnTop = new int[bounds.x1 + 1][bounds.y1 + 1];

        Seq<Integer> soleSupportBrick = Seq.empty();
        for (final Brick brick : bricks) {
            int z = Integer.MIN_VALUE;
            Seq<Integer> supportedBy = Seq.empty();
            for (int x = brick.x0; x <= brick.x1; x++) {
                for (int y = brick.y0; y <= brick.y1; y++) {
                    if (zbuffer[x][y] == z) {
                        supportedBy = supportedBy.addFirst(brickOnTop[x][y]);
                    } else if (zbuffer[x][y] > z) {
                        z = zbuffer[x][y];
                        supportedBy = Seq.of(brickOnTop[x][y]);
                    }
                }
            }

            final int z1 = z + (brick.z1 - brick.z0) + 1;
            for (int x = brick.x0; x <= brick.x1; x++) {
                for (int y = brick.y0; y <= brick.y1; y++) {
                    zbuffer[x][y] = z1;
                    brickOnTop[x][y] = brick.id;
                }
            }

            // if this brick is only supported by one other brick, that brick cannot be safely disintegrated
            supportedBy = supportedBy.unique().removeFirst(GROUND);
            if (supportedBy.length == 1) soleSupportBrick = soleSupportBrick.addFirst(supportedBy.value);
        }

        return bricks.length - soleSupportBrick.unique().length;
    }


    @Override
    long star2() {
        final int[][] zbuffer = new int[bounds.x1 + 1][bounds.y1 + 1];
        final int[][] brickOnTop = new int[bounds.x1 + 1][bounds.y1 + 1];

        final HashMultimap<Integer, Integer> above = HashMultimap.create();
        final HashMultimap<Integer, Integer> under = HashMultimap.create();
        for (final Brick brick : bricks) {
            int z = Integer.MIN_VALUE;
            Seq<Integer> supportedBy = Seq.empty();
            for (int x = brick.x0; x <= brick.x1; x++) {
                for (int y = brick.y0; y <= brick.y1; y++) {
                    if (zbuffer[x][y] == z) {
                        supportedBy = supportedBy.addFirst(brickOnTop[x][y]);
                    } else if (zbuffer[x][y] > z) {
                        z = zbuffer[x][y];
                        supportedBy = Seq.of(brickOnTop[x][y]);
                    }
                }
            }

            final int z1 = z + (brick.z1 - brick.z0) + 1;
            for (int x = brick.x0; x <= brick.x1; x++) {
                for (int y = brick.y0; y <= brick.y1; y++) {
                    zbuffer[x][y] = z1;
                    brickOnTop[x][y] = brick.id;
                }
            }

            supportedBy.unique().removeFirst(GROUND).each(support -> {
                above.put(support, brick.id);
                under.put(brick.id, support);
            });
        }

        final Map<Integer, Brick> index = bricks.indexBy(Brick::id);
        return bricks.map(brick -> score(brick, index, above, under)).reduce(Integer::sum);
    }

    int score(final Brick brick, final Map<Integer, Brick> index, final Multimap<Integer, Integer> above, final Multimap<Integer, Integer> under) {
        final Set<Integer> fallen = new HashSet<>();
        final PriorityQueue<Brick> queue = new PriorityQueue<>(Comparator.comparingInt(Brick::z0));
        final Set<Integer> enqueued = new HashSet<>();
        queue.add(brick);
        enqueued.add(brick.id);

        while (isNotEmpty(queue)) {
            final Brick current = queue.poll();
            final boolean allUnderThisBrickHaveFallen = brick == current || fallen.containsAll(under.get(current.id));
            if (allUnderThisBrickHaveFallen) {
                fallen.add(current.id);
                above.get(current.id).stream()
                        .filter(i -> !enqueued.contains(i))
                        .forEach(a -> {
                            enqueued.add(a);
                            queue.add(index.get(a));
                        });
            }
        }
        return fallen.size() - 1;
    }

    record Brick(int id, int x0, int y0, int z0, int x1, int y1, int z1) {
        static Brick from(final AtomicInteger generator, final String input) {
            final var n = Str.longs(input).map(Long::intValue);
            return new Brick(generator.incrementAndGet(), n.at(0), n.at(1), n.at(2), n.at(3), n.at(4), n.at(5));
        }
    }
}
