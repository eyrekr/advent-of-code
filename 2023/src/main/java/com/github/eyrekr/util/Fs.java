package com.github.eyrekr.util;

import java.nio.file.Files;
import java.nio.file.Path;

public final class Fs {

    public static String mainResource(final String name) {
        try {
            return Files.readString(Path.of(String.format("src/main/resources/" + name)));
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static String testResource(final String name) {
        try {
            return Files.readString(Path.of(String.format("src/test/resources/" + name)));
        } catch (final Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
