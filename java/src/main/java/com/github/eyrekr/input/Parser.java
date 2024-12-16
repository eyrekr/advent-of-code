package com.github.eyrekr.input;

import com.github.eyrekr.immutable.Arr;
import org.apache.commons.lang3.Validate;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

public class Parser {
    private Arr<Token> tokens;

    Parser(final Arr<Token> tokens) {
        this.tokens = tokens;
    }

    public Parser ignoreAllWhitespace() {
        tokens = tokens.where(token -> token.type != Token.Type.Whitespace);
        return this;
    }

    public Parser ignoreWhitespace(final String text) {
        tokens = tokens.where(token -> token.type != Token.Type.Whitespace || !Objects.equals(text, token.text));
        return this;
    }

    public Parser ignoreAllSymbols() {
        tokens = tokens.where(token -> token.type != Token.Type.Symbol);
        return this;
    }

    public Parser ignoreSymbols(final String symbols) {
        tokens = tokens.where(token -> token.type != Token.Type.Whitespace || !symbols.contains(token.text));
        return this;
    }

    public Parser ignoreAllText() {
        tokens = tokens.where(token -> token.type != Token.Type.Text);
        return this;
    }

    public Parser ignoreText(final String text) {
        tokens = tokens.where(token -> token.type != Token.Type.Text || !Objects.equals(token.text, text));
        return this;
    }

    public Parser skip() {
        tokens = tokens.removeFirst();
        return this;
    }

    public Parser next(final Token.Type type) {
        tokens = tokens.skipWhile(token -> token.type != type);
        return this;
    }

    public Parser any(final Consumer<String> consumer) {
        consumer.accept(tokens.peek().text);
        tokens = tokens.removeFirst();
        return this;
    }

    public Parser text(final Consumer<String> consumer) {
        final var token = tokens.peek();
        Validate.isTrue(token.type == Token.Type.Text, "Text expected; was " + token.type + " " + token.text);
        consumer.accept(token.text);
        tokens = tokens.removeFirst();
        return this;
    }

    public Parser number(final LongConsumer consumer) {
        final var token = tokens.peek();
        Validate.isTrue(token.type == Token.Type.Number, "Number expected; was " + token.type + " " + token.text);
        consumer.accept(token.value);
        tokens = tokens.removeFirst();
        return this;
    }

    public Parser symbol(final Consumer<String> consumer) {
        final var token = tokens.peek();
        Validate.isTrue(token.type == Token.Type.Symbol, "Symbol expected; was " + token.type + " " + token.text);
        consumer.accept(token.text);
        tokens = tokens.removeFirst();
        return this;
    }


    public Token la(final int i) {
        return (0 <= i && i < tokens.length) ? tokens.at(i) : Token.END;
    }

}
