package com.alex.mathlogic.task4.axioms;

import com.alex.mathlogic.task4.Expression;

/**
 * @author Alexey Katsman
 * @since 10.06.16
 */

public class Axiom9 implements Axiom {

    public int check(Expression expression) {
        try {
            boolean operatorsMatches = expression.oper.equals("->");
            operatorsMatches &= expression.first.oper.equals("->");
            operatorsMatches &= expression.second.oper.equals("->");
            operatorsMatches &= expression.second.first.oper.equals("->");
            operatorsMatches &= expression.second.first.second.oper.equals("!");
            operatorsMatches &= expression.second.second.oper.equals("!");
            String a1 = expression.first.first.representation;
            String a2 = expression.second.first.first.representation;
            String a3 = expression.second.second.first.representation;
            String b1 = expression.first.second.representation;
            String b2 = expression.second.first.second.first.representation;

            if (operatorsMatches && a1.equals(a2) && a2.equals(a3) && b1.equals(b2)) {
                return 9;
            }
        } catch (Exception ignored) {
            return 0;
        }
        return 0;
    }
}
