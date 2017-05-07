package com.alex.mathlogic.common.formula;

import com.alex.mathlogic.common.node.Node;

import java.util.List;

/**
 * @author Alexey Katsman
 * @since 07.05.17
 */

public class FormulaGetter {

    private FormulaGetter() {

    }

    @SuppressWarnings("SameParameterValue")
    public static Node getAxiom(List<Node> axioms, int number, Node a) {
        return getAxiom(axioms, number, a, null, null);
    }

    public static Node getAxiom(List<Node> axioms, int number, Node a, Node b) {
        return getAxiom(axioms, number, a, b, null);
    }

    public static Node getAxiom(List<Node> axioms, int number, Node a, Node b, Node c) {
        return getTemplateFormula(axioms.get(number - 1), a, b, c);
    }

    private static Node getTemplateFormula(Node formula, Node a, Node b, Node c) {
        if (formula == null) {
            return null;
        }

        switch (formula.getStringNotation()) {
            case "A":
                return a;
            case "B":
                return b;
            case "C":
                return c;
        }

        return new Node(formula.getStringNotation(), getTemplateFormula(formula.getLeft(), a, b, c),
            getTemplateFormula(formula.getRight(), a, b, c));
    }
}
