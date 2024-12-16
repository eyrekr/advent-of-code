package com.github.eyrekr.input;

public class Token {

    public enum Type {Text, Number, Symbol, Whitespace, End}

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
