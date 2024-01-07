package com.github.eyrekr.input;

import com.github.eyrekr.immutable.Seq;
import org.apache.commons.lang3.Validate;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.LongConsumer;

public final class In {

    public static Seq<Token> tokenize(final String input) {
        return Seq.fromIterator(new Tokenizer(input));
    }

    public static Parser parse(final String input) {
        return new Parser(tokenize(input));
    }

    //rfg{s<537:gd,x>2440:R,A}
    public enum Type {Text, Number, Symbol, Whitespace, End}

    public static class Token {
        public static final Token END = new Token(Type.End, "", 0L);
        public final Type type;
        public final String text;
        public final long value;

        Token(final Type type, final String text, final long value) {
            this.type = type;
            this.text = text;
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("%-20s %s", text, type);
        }
    }

    public static void main(String[] args) {
        final var p = parse("rfg{s<537:gd,x>2440:R,A}");

        class Rule {
            String ch;
            String symbol;
            long value;
            String next;
        }
        class Workflow {
            String name;
            Seq<Rule> rules;
        }

        var wf = new Workflow();
        p.text(t -> wf.name = t).ignoreAllWhitespace().ignoreSymbols("{},");
    }
}

class Tokenizer implements Iterator<In.Token> {
    private final char[] a;
    private int i = 0;

    Tokenizer(final String string) {
        this.a = string.toCharArray();
    }

    @Override
    public boolean hasNext() {
        return i < a.length;
    }

    @Override
    public In.Token next() {
        if (i >= a.length) return In.Token.END;
        final char la = a[i];
        if (Character.isDigit(la)) return number();
        if (Character.isLetter(la)) return text();
        if (Character.isWhitespace(la)) return whitespace();
        return symbol();
    }

    private In.Token number() {
        final StringBuilder b = new StringBuilder();
        while (i < a.length && Character.isDigit(a[i])) b.append(a[i++]);
        final String text = b.toString();
        return new In.Token(In.Type.Number, text, Long.parseLong(text));
    }

    private In.Token text() {
        final StringBuilder b = new StringBuilder();
        while (i < a.length && Character.isLetter(a[i])) b.append(a[i++]);
        final String text = b.toString();
        return new In.Token(In.Type.Text, text, 0L);
    }

    private In.Token whitespace() {
        final StringBuilder b = new StringBuilder();
        while (i < a.length && Character.isWhitespace(a[i])) b.append(a[i++]);
        final String text = b.toString();
        return new In.Token(In.Type.Whitespace, text, 0L);
    }

    private In.Token symbol() {
        final String text = "" + a[i++];
        return new In.Token(In.Type.Symbol, text, 0L);
    }
}

class Parser {
    private Seq<In.Token> tokens;

    Parser(final Seq<In.Token> tokens) {
        this.tokens = tokens;
    }

    Parser ignoreAllWhitespace() {
        tokens = tokens.where(token -> token.type != In.Type.Whitespace);
        return this;
    }

    Parser ignoreWhitespace(final String text) {
        tokens = tokens.where(token -> token.type != In.Type.Whitespace || !Objects.equals(text, token.text));
        return this;
    }

    Parser ignoreAllSymbols() {
        tokens = tokens.where(token -> token.type != In.Type.Symbol);
        return this;
    }

    Parser ignoreSymbols(final String symbols) {
        tokens = tokens.where(token -> token.type != In.Type.Whitespace || !symbols.contains(token.text));
        return this;
    }

    Parser ignoreAllText() {
        tokens = tokens.where(token -> token.type != In.Type.Text);
        return this;
    }

    Parser ignoreText(final String text) {
        tokens = tokens.where(token -> token.type != In.Type.Text || !Objects.equals(token.text, text));
        return this;
    }

    Parser skip() {
        tokens = tokens.tail;
        return this;
    }

    Parser next(final In.Type type) {
        tokens = tokens.skipWhile(token -> token.type != type);
        return this;
    }

    Parser any(final Consumer<String> consumer) {
        consumer.accept(tokens.value.text);
        tokens = tokens.tail;
        return this;
    }

    Parser text(final Consumer<String> consumer) {
        Validate.isTrue(tokens.value.type == In.Type.Text, "Text expected; was " + tokens.value.type + " " + tokens.value.text);
        consumer.accept(tokens.value.text);
        tokens = tokens.tail;
        return this;
    }

    Parser number(final LongConsumer consumer) {
        Validate.isTrue(tokens.value.type == In.Type.Number, "Number expected; was " + tokens.value.type + " " + tokens.value.text);
        consumer.accept(tokens.value.value);
        tokens = tokens.tail;
        return this;
    }

    Parser symbol(final Consumer<String> consumer) {
        Validate.isTrue(tokens.value.type == In.Type.Symbol, "Symbol expected; was " + tokens.value.type + " " + tokens.value.text);
        consumer.accept(tokens.value.text);
        tokens = tokens.tail;
        return this;
    }


    In.Token la(final int i) {
        return (0 <= i && i < tokens.length) ? tokens.at(i) : In.Token.END;
    }

}
