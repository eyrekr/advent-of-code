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
    DownRight(+1, +1, '↘', 7, 1 << 8),

    LeftUp(-1, -1, '↖', 0, 1 << 9),
    RightUp(+1, -1, '↗', 2, 1 << 10),
    LeftDown(-1, +1, '↙', 5, 1 << 11),
    RightDown(+1, +1, '↘', 7, 1 << 12);

    public final int dx, dy;
    public final char ch;
    public final int i8; // index in the neighbour in neighbours8 in this direction
    public final int code;

    Direction(final int dx, final int dy, final char ch, final int i8, final int code) {
        this.dx = dx;
        this.dy = dy;
        this.ch = ch;
        this.i8 = i8;
        this.code = code;
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

            case LeftUp -> direction == RightDown;
            case RightUp -> direction == LeftDown;
            case LeftDown -> direction == RightUp;
            case RightDown -> direction == LeftUp;
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

            case LeftUp -> RightDown;
            case RightUp -> LeftDown;
            case LeftDown -> RightUp;
            case RightDown -> LeftUp;
        };
    }

    public Direction inverse() {
        return switch (this) {
            case None -> None;
            case Up -> Down;
            case Down -> Up;
            case Left -> Right;
            case Right -> Left;

            case UpLeft -> RightDown;
            case UpRight -> LeftDown;
            case DownLeft -> RightUp;
            case DownRight -> LeftUp;

            case LeftUp -> DownRight;
            case RightUp -> DownLeft;
            case LeftDown -> UpRight;
            case RightDown -> UpLeft;
        };
    }

    public Direction turn90DegreesRight() {
        return switch (this) {
            case None -> None;
            case Up -> Right;
            case Down -> Left;
            case Left -> Up;
            case Right -> Down;

            case UpLeft -> RightUp;
            case UpRight -> RightDown;
            case DownLeft -> LeftUp;
            case DownRight -> LeftDown;

            case LeftUp -> UpRight;
            case RightUp -> DownRight;
            case LeftDown -> LeftUp;
            case RightDown -> DownLeft;
        };
    }

    public Direction turn90DegreesLeft() {
        return switch (this) {
            case None -> None;
            case Up -> Left;
            case Down -> Right;
            case Left -> Down;
            case Right -> Up;

            case UpLeft -> LeftDown;
            case UpRight -> LeftUp;
            case DownLeft -> RightDown;
            case DownRight -> RightUp;

            case LeftUp -> DownLeft;
            case RightUp -> UpLeft;
            case LeftDown -> DownRight;
            case RightDown -> UpRight;
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