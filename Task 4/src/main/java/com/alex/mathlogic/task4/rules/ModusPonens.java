package com.alex.mathlogic.task4.rules;

import com.alex.mathlogic.task4.Expression;
import com.alex.mathlogic.task4.ExpressionParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexey Katsman
 * @since 10.06.16
 */

public class ModusPonens implements Rule {

    private final ExpressionParser parser;

    public ModusPonens(ExpressionParser parser) {
        this.parser = parser;
    }

    @Override
    public List<String> check(Expression expression, List<Expression> proved, Expression assumptionToRemove) {
        List<String> res = new ArrayList<>();
        String left;
        boolean matched = false;
        List<Expression> variablesToSubstitute = new ArrayList<>();
        variablesToSubstitute.add(assumptionToRemove);
        for (int i = proved.size() - 2; i >= 0 && !matched; --i) {
            if (proved.get(i).first == null || proved.get(i).second == null) {
                continue;
            }
            left = proved.get(i).first.representation;
            if (proved.get(i).oper.equals("->") &&
                proved.get(i).second.representation.equals(expression.representation))
            {
                for (int j = proved.size() - 1; j >= 0; --j) {
                    if (proved.get(j).representation.equals(left)) {
                        variablesToSubstitute.add(proved.get(i).first);
                        variablesToSubstitute.add(proved.get(i));
                        variablesToSubstitute.add(expression);

                        if (assumptionToRemove != null) {
                            res.add(parser.parse("(1->2)->((1->3)->(1->4))")
                                .substitute(variablesToSubstitute).representation);
                            res.add(parser.parse("((1->3)->(1->4))").substitute(variablesToSubstitute).representation);
                        } else {
                            res.add(expression.representation);
                        }
                        matched = true;
                        break;
                    }
                }
            }
        }
        if (matched) {
            if (assumptionToRemove != null) {
                res.add(new Expression(assumptionToRemove, expression, "->").representation);
            }
            return res;
        } else {
            return null;
        }
    }
}
