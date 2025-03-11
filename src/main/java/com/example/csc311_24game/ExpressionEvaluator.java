package com.example.csc311_24game;

public class ExpressionEvaluator {
    private final String input;
    private int pos = -1, ch;

    public ExpressionEvaluator(String input) { this.input = input; }

    public double parse() {
        nextChar();
        double x = parseExpression();
        if (pos < input.length()) throw new RuntimeException("Unexpected: " + (char) ch);
        return x;
    }

    private void nextChar() { ch = (++pos < input.length()) ? input.charAt(pos) : -1; }

    private boolean eat(int charToEat) {
        while (ch == ' ') nextChar();
        if (ch == charToEat) { nextChar(); return true; }
        return false;
    }

    private double parseExpression() {
        double x = parseTerm();
        for (; ; ) {
            if (eat('+')) x += parseTerm();
            else if (eat('-')) x -= parseTerm();
            else return x;
        }
    }

    private double parseTerm() {
        double x = parseFactor();
        for (; ; ) {
            if (eat('*')) x *= parseFactor();
            else if (eat('/')) x /= parseFactor();
            else return x;
        }
    }

    private double parseFactor() {
        if (eat('+')) return parseFactor();
        if (eat('-')) return -parseFactor();
        double x;
        int startPos = this.pos;
        if (eat('(')) { x = parseExpression(); eat(')'); }
        else if ((ch >= '0' && ch <= '9')) {
            while ((ch >= '0' && ch <= '9')) nextChar();
            x = Double.parseDouble(input.substring(startPos, this.pos));
        } else throw new RuntimeException("Unexpected: " + (char) ch);
        return x;
    }
}
