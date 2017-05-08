package com.alex.mathlogic.task4.axioms;

import com.alex.mathlogic.task4.Expression;

/**
 * @author Alexey Katsman
 * @since 10.06.16
 */

public class Axiom8 implements Axiom {

    public int check(Expression expression) {
        try {
            boolean operatorsMatches = expression.oper.equals("->");
            operatorsMatches &= expression.first.oper.equals("->");
            operatorsMatches &= expression.second.oper.equals("->");
            operatorsMatches &= expression.second.first.oper.equals("->");
            operatorsMatches &= expression.second.second.oper.equals("->");
            operatorsMatches &= expression.second.second.first.oper.equals("|");
            String a1 = expression.first.first.representation;
            String a2 = expression.second.second.first.first.representation;
            String b1 = expression.second.first.first.representation;
            String b2 = expression.second.second.first.second.representation;
            String c1 = expression.first.second.representation;
            String c2 = expression.second.first.second.representation;
            String c3 = expression.second.second.second.representation;

            if (operatorsMatches && a1.equals(a2) && b1.equals(b2) && c1.equals(c2) && c2.equals(c3)) {
                return 8;
            }
        } catch (Exception ignored) {
            return 0;
        }
        return 0;
    }
}
