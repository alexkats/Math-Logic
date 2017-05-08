package com.alex.mathlogic.task4.axioms;

import com.alex.mathlogic.task4.Expression;

/**
 * @author Alexey Katsman
 * @since 10.06.16
 */
public class Axiom1 implements Axiom {

    public int check(Expression expression) {
        try {
            String a = expression.first.representation;
            String b = expression.second.second.representation;
            if (expression.second.oper.equals("->") && a.equals(b)) {
                return 1;
            }
        } catch (Exception ignored) {
            return 0;
        }
        return 0;
    }
}
