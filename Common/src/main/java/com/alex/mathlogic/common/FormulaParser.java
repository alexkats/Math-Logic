package com.alex.mathlogic.common;

import com.alex.mathlogic.common.node.Node;
import com.alex.mathlogic.common.node.Type;

import java.util.Objects;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

public class FormulaParser {

    private static int currentSymbol;

    public static Node parse(String formula) {
        currentSymbol = 0;
        return parseExpression(formula);
    }

    private static Node parseExpression(String formula) {
        Node result = parseDisjunction(formula);

        if (checkNextSubstring(formula, Type.IMPLICATION.getStringNotation())) {
            result = new Node(Type.IMPLICATION.getStringNotation(), result, parseExpression(formula));
        }

        return result;
    }

    private static Node parseDisjunction(String formula) {
        Node result = parseConjunction(formula);

        while (checkNextSubstring(formula, Type.OR.getStringNotation())) {
            result = new Node(Type.OR.getStringNotation(), result, parseConjunction(formula));
        }

        return result;
    }

    private static Node parseConjunction(String formula) {
        Node result = parseNegation(formula);

        while (checkNextSubstring(formula, Type.AND.getStringNotation())) {
            result = new Node(Type.AND.getStringNotation(), result, parseNegation(formula));
        }

        return result;
    }

    private static Node parseNegation(String formula) {
        Node result;
        char symbol = formula.charAt(currentSymbol);

        if (Character.isUpperCase(symbol)) {
            result = new Node(constructVariableName(formula), null, null);
        } else if (symbol == '!') {
            currentSymbol++;
            result = new Node(Type.NOT.getStringNotation(), null, parseNegation(formula));
        } else if (symbol == '(') {
            currentSymbol++;
            result = parseExpression(formula);
            checkClosingBracket(formula);
        } else {
            throw new IllegalStateException("Incorrect formula");
        }

        return result;
    }

    private static String constructVariableName(String formula) {
        StringBuilder variableName = new StringBuilder();
        variableName.append(formula.charAt(currentSymbol++));
        char symbol = formula.charAt(currentSymbol);

        while (Character.isDigit(symbol)) {
            variableName.append(symbol);
            currentSymbol++;
        }

        return variableName.toString();
    }

    private static boolean checkNextSubstring(String string, String substring) {
        if (currentSymbol < string.length() && Objects.equals(string.substring(currentSymbol, currentSymbol + substring.length()), substring)) {
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
