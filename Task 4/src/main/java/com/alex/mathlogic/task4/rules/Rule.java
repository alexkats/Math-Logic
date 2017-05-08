package com.alex.mathlogic.task4.rules;

import com.alex.mathlogic.task4.Expression;
import com.alex.mathlogic.task4.error.ExceptionWithCustomError;

import java.util.List;

/**
 * @author Alexey Katsman
 * @since 10.06.16
 */

public interface Rule {

    List<String> check(Expression expression, List<Expression> proved, Expression assumptionToRemove)
        throws ExceptionWithCustomError;
}
