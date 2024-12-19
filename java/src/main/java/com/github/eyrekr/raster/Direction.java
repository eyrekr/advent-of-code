package com.github.eyrekr.raster;

public enum Direction {
    None(0, 0, '×', -1, 1),

    Up(0, -1, '↑', 1, 1 << 1),
    Down(0, +1, '↓', 6, 1 << 2),
    Left(-1, 0, '←', 3, 1 << 3),
    Right(+1, 0, '→', 4, 1 << 4),

    UpLeft(-1, -1, '↖', 0, 1 << 5),
    UpRight(+1, -1, '↗', 2, 1 << 6),
    DownLeft(-1, +1, '↙', 5, 1 << 7),
    DownRight(+1, +1, '↘', 7, 1 << 8);

    public final int dx, dy;
    public final char ch;
    public final int i8; // index in the neighbour in neighbours8 in this direction
    public final int flag;

    Direction(final int dx, final int dy, final char ch, final int i8, final int flag) {
        this.dx = dx;
        this.dy = dy;
        this.ch = ch;
        this.i8 = i8;
        this.flag = flag;
    }

    public boolean isOpposite(final Direction direction) {
        return switch (this) {
            case None -> false;
            case Up -> direction == Down;
            case Down -> direction == Up;
            case Left -> direction == Right;
            case Right -> direction == Left;
            case UpLeft -> direction == DownRight;
            case UpRight -> direction == DownLeft;
            case DownLeft -> direction == UpRight;
            case DownRight -> direction == UpLeft;
        };
    }

    public Direction opposite() {
        return switch (this) {
            case None -> None;
            case Up -> Down;
            case Down -> Up;
            case Left -> Right;
            case Right -> Left;
            case UpLeft -> DownRight;
            case UpRight -> DownLeft;
            case DownLeft -> UpRight;
            case DownRight -> UpLeft;

        };
    }

    public Direction turn90DegreesRight() {
        return switch (this) {
            case None -> None;
            case Up -> Right;
            case Down -> Left;
            case Left -> Up;
            case Right -> Down;
            case UpLeft -> UpRight;
            case UpRight -> DownRight;
            case DownLeft -> UpLeft;
            case DownRight -> DownLeft;
        };
    }

    public Direction turn90DegreesLeft() {
        return switch (this) {
            case None -> None;
            case Up -> Left;
            case Down -> Right;
            case Left -> Down;
            case Right -> Up;
            case UpLeft -> DownLeft;
            case UpRight -> UpLeft;
            case DownLeft -> DownRight;
            case DownRight -> UpRight;
        };
    }

    public static Direction fromChar(final char ch) {
        return switch (ch) {
            case 'U', 'u', '^', '↑' -> Direction.Up;
            case 'D', 'd', 'v', '↓' -> Direction.Down;
            case 'L', 'l', '<', '←' -> Direction.Left;
            case 'R', 'r', '>', '→' -> Direction.Right;
            default -> Direction.None;
        };
    }
}