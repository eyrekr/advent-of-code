package com.github.eyrekr.common;

public record Numbered<E>(int i, E value) implements Indexed {
}
