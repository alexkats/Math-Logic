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

public class ExistsRule implements Rule {

    private final ExpressionParser parser;
    private final List<Expression> implChange;

    public ExistsRule(ExpressionParser parser, List<Expression> implChange) {
        this.parser = parser;
        this.implChange = implChange;
    }

    @Override
    public List<String> check(Expression expression, List<Expression> proved, Expression assumptionToRemove)
        throws ExceptionWithCustomError
    {
        List<Expression> variablesToSubstitute = new ArrayList<>();
        variablesToSubstitute.add(assumptionToRemove);
        Error error = new Error();
        error.code = -1;
        List<String> res = new ArrayList<>();
        boolean matched = false;
        if (expression.oper.equals("->") && expression.first.oper.equals("?")) {// rule for ∃x(ψ) → (φ)
            for (int i = proved.size() - 1; i >= 0; --i) {
                if (proved.get(i).first == null || proved.get(i).second == null) {
                    continue;
                }
                if (proved.get(i).first.representation.equals(expression.first.second.representation) &&
                    proved.get(i).second.representation.equals(expression.second.representation))
                {
                    if (expression.second.freeVariables
                        .contains(expression.first.first.representation))
                    { // x is free in φ
                        error.code = 2;
                        error.variable = expression.first.first.representation;
                        error.expression = expression.second;
                    } else if (assumptionToRemove != null
                        && assumptionToRemove.freeVariables.contains(expression.first.first.representation))
                    {
                        error.code = 3;
                        error.variable = expression.second.first.representation;
                        error.expression = assumptionToRemove;
                    } else {
                        if (assumptionToRemove != null) {
                            variablesToSubstitute.clear();
                            variablesToSubstitute.add(assumptionToRemove);
                            variablesToSubstitute.add(expression.first.second);
                            variablesToSubstitute.add(expression.second);
                            for (Expression expression1 : implChange) {
                                Expression tmp = new Expression(expression1);
                                res.add(tmp.substitute(variablesToSubstitute).representation);
                            }
                            res.add(parser.parse("2->1->3").substitute(variablesToSubstitute).representation);
                            variablesToSubstitute.clear();
                            variablesToSubstitute.add(expression.first);
                            variablesToSubstitute.add(assumptionToRemove);
                            variablesToSubstitute.add(expression.second);
                            res.add(parser.parse("1->2->3").substitute(variablesToSubstitute).representation);
                            for (Expression expression1 : implChange) {
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
