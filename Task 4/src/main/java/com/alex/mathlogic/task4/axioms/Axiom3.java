package com.alex.mathlogic.task4.axioms;

import com.alex.mathlogic.task4.Expression;

/**
 * @author Alexey Katsman
 * @since 10.06.16
 */

public class Axiom3 implements Axiom {

    public int check(Expression expression) {
        try {
            boolean operatorsMatches = expression.oper.equals("->");
            operatorsMatches &= expression.second.oper.equals("->");
            operatorsMatches &= expression.second.second.oper.equals("&");

            String a1 = expression.first.representation;
            String a2 = expression.second.second.first.representation;
            String b1 = expression.second.first.representation;
            String b2 = expression.second.second.second.representation;

            if (operatorsMatches && a1.equals(a2) && b1.equals(b2)) {
                return 3;
            }
        } catch (Exception ignored) {
            return 0;
        }
        return 0;
    }
}
