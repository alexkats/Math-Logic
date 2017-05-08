package com.alex.mathlogic.task4.rules;

import com.alex.mathlogic.task4.Expression;
import com.alex.mathlogic.task4.ExpressionParser;
import com.alex.mathlogic.task4.error.Error;
import com.alex.mathlogic.task4.error.ExceptionWithCustomError;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexey Katsman
 * @since 10.06.16
 */

public class ForAllRule implements Rule {

    private final ExpressionParser parser;
    private final List<Expression> implToConj;
    private final List<Expression> conjToImpl;

    public ForAllRule(ExpressionParser parser, List<Expression> implToConj, List<Expression> conjToImpl) {
        this.parser = parser;
        this.implToConj = implToConj;
        this.conjToImpl = conjToImpl;
    }

    @Override
    public List<String> check(Expression expression, List<Expression> proved, Expression assumptionToRemove)
        throws ExceptionWithCustomError
    {
        List<Expression> variablesToSubstitute = new ArrayList<>();
        variablesToSubstitute.add(assumptionToRemove);
        Error error = new Error();
        error.code = -1;
        boolean matched = false;
        List<String> res = new ArrayList<>();
        if (expression.oper.equals("->") && expression.second.oper.equals("@")) {// rule for (φ) → ∀x(ψ)
            for (int i = proved.size() - 1; i >= 0; --i) {
                if (proved.get(i).first == null || proved.get(i).second == null) {
                    continue;
                }
                if (proved.get(i).first.representation.equals(expression.first.representation) &&
                    proved.get(i).second.representation.equals(expression.second.second.representation))
                {
                    if (expression.first.freeVariables
                        .contains(expression.second.first.representation))
                    { // x is free in φ
                        error.code = 2;
                        error.variable = expression.second.first.representation;
                        error.expression = expression.first;
                    } else if (assumptionToRemove != null &&
                        assumptionToRemove.freeVariables.contains(expression.second.first.representation))
                    {
                        error.code = 3;
                        error.variable = expression.second.first.representation;
                        error.expression = assumptionToRemove;
                    } else {
                        if (assumptionToRemove != null) {
                            variablesToSubstitute.clear();
                            variablesToSubstitute.add(assumptionToRemove);
                            variablesToSubstitute.add(expression.first);
                            variablesToSubstitute.add(expression.second.second);
                            for (Expression expression1 : implToConj) {
                                Expression tmp = new Expression(expression1);
                                res.add(tmp.substitute(variablesToSubstitute).representation);
                            }
                            res.add(parser.parse("1&2->3").substitute(variablesToSubstitute).representation);
                            variablesToSubstitute.set(2, expression.second);
                            res.add(parser.parse("1&2->3").substitute(variablesToSubstitute).representation);
                            for (Expression expression1 : conjToImpl) {
                                Expression tmp = new Expression(expression1);
                                res.add(tmp.substitute(variablesToSubstitute).representation);
                            }
                        }
                        matched = true;
                        break;
                    }
                }
            }
        }
        return ResultFormer.formResult(res, error, matched, assumptionToRemove, expression);
    }
}
