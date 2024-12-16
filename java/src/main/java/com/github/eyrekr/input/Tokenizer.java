package com.github.eyrekr.input;

import java.util.Iterator;

public class Tokenizer implements Iterator<Token> {
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
    public Token next() {
        if (i >= a.length) return Token.END;
        final char la = a[i];
        if (Character.isDigit(la)) return number();
        if (Character.isLetter(la)) return text();
        if (Character.isWhitespace(la)) return whitespace();
        return symbol();
    }

    private Token number() {
        final StringBuilder b = new StringBuilder();
        while (i < a.length && Character.isDigit(a[i])) b.append(a[i++]);
        final String text = b.toString();
        return new Token(Token.Type.Number, text, Long.parseLong(text));
    }

    private Token text() {
        final StringBuilder b = new StringBuilder();
        while (i < a.length && Character.isLetter(a[i])) b.append(a[i++]);
        final String text = b.toString();
        return new Token(Token.Type.Text, text, 0L);
    }

    private Token whitespace() {
        final StringBuilder b = new StringBuilder();
        while (i < a.length && Character.isWhitespace(a[i])) b.append(a[i++]);
        final String text = b.toString();
        return new Token(Token.Type.Whitespace, text, 0L);
    }

    private Token symbol() {
        final String text = "" + a[i++];
        return new Token(Token.Type.Symbol, text, 0L);
    }
}
