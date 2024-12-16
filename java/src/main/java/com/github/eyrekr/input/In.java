package com.github.eyrekr.input;

import com.github.eyrekr.immutable.Arr;
import org.apache.commons.lang3.Validate;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

public final class In {

    public static Arr<Token> tokenize(final String input) {
        return Arr.fromIterator(new Tokenizer(input));
    }

    public static Parser parse(final String input) {
        return new Parser(tokenize(input));
    }
}

