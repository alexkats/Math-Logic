package com.alex.mathlogic.common.formula;

import com.alex.mathlogic.common.Pair;
import com.alex.mathlogic.common.node.Node;
import com.alex.mathlogic.common.node.Type;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

public class FormulaParser {

    private FormulaParser() {

    }

    private static int currentSymbol;

    public static Node parse(String formula) {
        currentSymbol = 0;
        return parseExpression(formula);
    }

    public static Pair<Node, Node> parseSupposes(String title, List<Node> supposes) {
        List<String> formulas = Arrays.asList(title.split(","));
        int n = formulas.size();
        String last = formulas.get(n - 1);
        int ind = last.indexOf('|');
        String alphaString = last.substring(0, ind);
        String betaString = last.substring(ind + 2);

        for (int i = 0; i < formulas.size() - 1; i++) {
            supposes.add(parse(formulas.get(i)));
        }

        return Pair.of(parse(alphaString), parse(betaString));
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
            result = new Node(constructVariableName(formula));
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
        char symbol = currentSymbol < formula.length() ? formula.charAt(currentSymbol) : 0;

        while (Character.isDigit(symbol)) {
            variableName.append(symbol);
            currentSymbol++;
            symbol = currentSymbol < formula.length() ? formula.charAt(currentSymbol) : 0;
        }

        return variableName.toString();
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
