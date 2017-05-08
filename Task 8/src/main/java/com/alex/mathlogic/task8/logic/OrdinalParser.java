package com.alex.mathlogic.task8.logic;

import java.util.Objects;

/**
 * @author Alexey Katsman
 * @since 08.05.17
 */

public class OrdinalParser {

    private OrdinalParser() {

    }

    private static int currentSymbol;

    public static Ordinal parse(String ord) {
        currentSymbol = 0;
        return parseExpression(ord);
    }

    private static Ordinal parseExpression(String ord) {
        Ordinal result = parseAdd(ord);

        while (checkNextSubstring(ord, "+")) {
            Ordinal result2 = parseAdd(ord);
            result = OrdinalEvaluator.add(result, result2);
        }

        return result;
    }

    private static Ordinal parseAdd(String ord) {
        Ordinal result = parseMul(ord);

        while (checkNextSubstring(ord, "*")) {
            Ordinal result2 = parseMul(ord);
            result = OrdinalEvaluator.mul(result, result2);
        }

        return result;
    }

    private static Ordinal parseMul(String ord) {
        Ordinal result = parseTerm(ord);

        while (checkNextSubstring(ord, "^")) {
            Ordinal result2 = parseMul(ord);
            result = OrdinalEvaluator.exp(result, result2);
        }

        return result;
    }

    private static Ordinal parseTerm(String ord) {
        Ordinal result;
        char symbol = ord.charAt(currentSymbol);

        if (symbol == 'w') {
            currentSymbol++;
            result = OrdinalGetter.getW();
        } else if (symbol == '(') {
            currentSymbol++;
            result = parseExpression(ord);
            checkClosingBracket(ord);
        } else {
            result = new Ordinal(constructNumber(ord));
        }

        return result;
    }

    private static long constructNumber(String ord) {
        char symbol = ord.charAt(currentSymbol);
        long result = 0;

        while (Character.isDigit(symbol)) {
            result *= 10;
            result += Character.digit(symbol, 10);
            currentSymbol++;
            symbol = currentSymbol < ord.length() ? ord.charAt(currentSymbol) : 0;
        }

        return result;
    }

    private static boolean checkNextSubstring(String string, String substring) {
        if (currentSymbol + substring.length() <= string.length()
            && Objects.equals(string.substring(currentSymbol, currentSymbol + substring.length()), substring))
        {
            currentSymbol += substring.length();
            return true;
        }

        return false;
    }

    private static void checkClosingBracket(String formula) {
        if (currentSymbol >= formula.length() || formula.charAt(currentSymbol) != ')') {
            throw new IllegalStateException("Incorrect formula! No closing bracket");
        }

        currentSymbol++;
    }
}
