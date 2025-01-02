package com.github.eyrekr.y2024;

import com.github.eyrekr.Aoc;
import com.github.eyrekr.immutable.Arr;
import com.github.eyrekr.immutable.Longs;

import java.util.Arrays;

/*
  The tree looks like this:

  *******************************
  *                             *
  *                             *
  *                             *
  *                             *
  *              *              *
  *             ***             *
  *            *****            *
  *           *******           *
  *          *********          *
  *            *****            *
  *           *******           *
  *          *********          *
  *         ***********         *
  *        *************        *
  *          *********          *
  *         ***********         *
  *        *************        *
  *       ***************       *
  *      *****************      *
  *        *************        *
  *       ***************       *
  *      *****************      *
  *     *******************     *
  *    *********************    *
  *             ***             *
  *             ***             *
  *             ***             *
  *                             *
  *                             *
  *                             *
  *                             *
  *******************************

 */
class D14 extends Aoc {

    interface Space {
        long Width = 101L;
        long Height = 103L;
        byte Empty = 0;
        byte Robot = '*';
        int AlignedRobots = "*******************************".length();
    }

    final Arr<Robot> robots;

    D14(final String input) {
        robots = Arr.ofLinesFromString(input).map(Robot::fromString);
    }

    @Override
    public long star1() {
        final var f = robots
                .map(robot -> robot.move(100L))
                .map(Robot::quadrant)
                .reduce(new int[5], (count, quadrant) -> {
                    count[quadrant] = count[quadrant] + 1;
                    return count;
                });
        return f[1] * f[2] * f[3] * f[4];
    }

    @Override
    public long star2() {
        final int n = (int) (Space.Width * Space.Height);
        final byte[] buffer = new byte[n];

        long step = 0;
        while (true) {
            Arrays.fill(buffer, Space.Empty);

            final long seconds = step;
            robots.map(robot -> robot.move(seconds)).each(robot -> {
                final int x = (int) (robot.y * Space.Width + robot.x);
                buffer[x] = Space.Robot;
            });

            if (robotsAligned(buffer)) return step;
            step++;
        }
    }

    record Robot(long x, long y, long dx, long dy) {
        static Robot fromString(final String input) {
            final var l = Longs.fromString(input);
            return new Robot(l.at(0), l.at(1), l.at(2), l.at(3));
        }

        Robot move(final long seconds) {
            return new Robot(mod(x + seconds * dx, Space.Width), mod(y + seconds * dy, Space.Height), dx, dy);
        }

        int quadrant() {
            final long qx = x - Space.Width / 2, qy = y - Space.Height / 2;
            if (qx > 0 && qy < 0) return 1;
            if (qx < 0 && qy < 0) return 2;
            if (qx < 0 && qy > 0) return 3;
            if (qx > 0 && qy > 0) return 4;
            return 0;
        }
    }

    /**
     * @return a % k in the range (0, k - 1)
     */
    static long mod(final long a, final long k) {
        final long mod = a % k;
        return mod < 0 ? mod + k : mod;
    }

    static boolean robotsAligned(final byte[] bytes) {
        int run = 0;
        for (int i = 0; i < bytes.length; i++)
            if (bytes[i] == Space.Robot) {
                if (++run >= Space.AlignedRobots) return true;
            } else run = 0;
        return false;
    }
}