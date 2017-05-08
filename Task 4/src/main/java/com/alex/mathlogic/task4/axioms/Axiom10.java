package com.alex.mathlogic.task4.axioms;

import com.alex.mathlogic.task4.Expression;

/**
 * @author Alexey Katsman
 * @since 10.06.16
 */
public class Axiom10 implements Axiom {

    public int check(Expression expression) {
        try {
            String a = expression.first.first.first.representation;
            String b = expression.second.representation;
            if (expression.first.oper.equals("!") && expression.first.first.oper.equals("!") && a.equals(b)) {
                return 10;
            }
        } catch (Exception ignored) {
            return 0;
        }
        return 0;
    }
}
