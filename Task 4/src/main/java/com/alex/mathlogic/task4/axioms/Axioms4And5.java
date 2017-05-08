package com.alex.mathlogic.task4.axioms;

import com.alex.mathlogic.task4.Expression;

/**
 * @author Alexey Katsman
 * @since 10.06.16
 */

public class Axioms4And5 implements Axiom {

    public int check(Expression expression) {
        try {
            boolean operatorsMatches = expression.first.oper.equals("&");
            operatorsMatches &= expression.oper.equals("->");
            String a1 = expression.first.first.representation;
            String a2 = expression.second.representation;
            String b = expression.first.second.representation;
            if (operatorsMatches && (a1.equals(a2) || b.equals(a2))) {
                return 4;
            }
        } catch (Exception ignored) {
            return 0;
        }
        return 0;
    }
}
