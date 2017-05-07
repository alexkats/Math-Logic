package com.alex.mathlogic.common.formula;

import com.alex.mathlogic.common.Pair;
import com.alex.mathlogic.common.Utils;
import com.alex.mathlogic.common.node.Node;
import com.alex.mathlogic.common.node.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

public class FormulaChecker {

    private FormulaChecker() {

    }

    public static int checkAxioms(Node formula, List<Node> axioms) {
        int result = 0;

        for (int i = 0; i < axioms.size(); i++) {
            if (checkEqualFormulas(formula, axioms.get(i))) {
                result = i + 1;
                break;
            }
        }

        return result;
    }

    public static boolean checkSuppose(Node formula, List<Node> supposes) {
        return checkSuppose(formula, supposes, 0, supposes.size());
    }

    public static boolean checkSuppose(Node formula, List<Node> supposes, int from, int to) {
        boolean found = false;

        for (int i = from; i < to; i++) {
            if (Objects.equals(formula, supposes.get(i))) {
                found = true;
                break;
            }
        }

        return found;
    }

    public static Optional<Pair<Integer, Integer>> checkModusPonens(Node formula, List<Node> parsedFormulas) {
        return checkModusPonens(formula, parsedFormulas, parsedFormulas.size() - 2, -1);
    }

    public static Optional<Pair<Integer, Integer>> checkModusPonens(Node formula, List<Node> parsedFormulas, int from,
        int to)
    {
        for (int i = from; i > to; i--) {
            Node whole = parsedFormulas.get(i);

            if (whole == null) {
                continue;
            }

            Node rightPart = whole.getRight();

            if (Objects.equals(whole.getStringNotation(), Type.IMPLICATION.getStringNotation()) &&
                Objects.equals(rightPart, formula))
            {
                for (int j = 0; j < from + 1; j++) {
                    Node leftPart = parsedFormulas.get(j);

                    if (Objects.equals(leftPart, whole.getLeft())) {
                        return Optional.of(Pair.of(j + 1, i + 1));
                    }
                }
            }
        }

        return Optional.empty();
    }

    @SuppressWarnings("WeakerAccess")
    public static boolean checkEqualFormulas(Node formula, Node pattern) {
        Map<String, List<Node>> variables = new HashMap<>();

        if (!fillVariablesAndCheckEquality(variables, formula, pattern)) {
            return false;
        }

        boolean result = true;

        for (List<Node> nodes : variables.values()) {
            for (Node node : nodes) {
                if (!Objects.equals(node, nodes.get(0))) {
                    result = false;
                    break;
                }
            }

            if (!result) {
                break;
            }
        }

        return result;
    }

    private static boolean fillVariablesAndCheckEquality(Map<String, List<Node>> variables, Node formula,
        Node pattern)
    {
        if (formula == null && pattern == null) {
            return true;
        }

        if (formula == null || pattern == null) {
            return false;
        }

        if (formula == pattern) {
            return true;
        }

        String stringNotation = pattern.getStringNotation();

        if (Utils.isVariable(stringNotation)) {
            if (variables.containsKey(stringNotation)) {
                variables.get(stringNotation).add(formula);
            } else {
                List<Node> value = new ArrayList<>();
                value.add(formula);
                variables.put(stringNotation, value);
            }

            return true;
        }

        return Objects.equals(stringNotation, formula.getStringNotation())
            && fillVariablesAndCheckEquality(variables, formula.getLeft(), pattern.getLeft())
            && fillVariablesAndCheckEquality(variables, formula.getRight(), pattern.getRight());
    }
}
