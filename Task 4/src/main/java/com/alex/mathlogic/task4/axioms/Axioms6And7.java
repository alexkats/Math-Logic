package com.alex.mathlogic.task4.axioms;

import com.alex.mathlogic.task4.Expression;

/**
 * @author Alexey Katsman
 * @since 10.06.16
 */

public class Axioms6And7 implements Axiom {

    public int check(Expression expression) {
        try {
            boolean operatorsMatches = expression.oper.equals("->");
            operatorsMatches &= expression.second.oper.equals("|");
            String a1 = expression.first.representation;
            String a2 = expression.second.first.representation;
            String b = expression.second.second.representation;

            if (operatorsMatches && (a1.equals(a2) || a1.equals(b))) {
                return 6;
            }
        } catch (Exception ignored) {
            return 0;
        }
        return 0;
    }
}
