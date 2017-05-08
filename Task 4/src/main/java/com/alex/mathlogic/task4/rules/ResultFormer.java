package com.alex.mathlogic.task4.rules;

import com.alex.mathlogic.task4.Expression;
import com.alex.mathlogic.task4.error.Error;
import com.alex.mathlogic.task4.error.ExceptionWithCustomError;

import java.util.List;

/**
 * @author Alexey Katsman
 * @since 10.06.16
 */

class ResultFormer {

    static List<String> formResult(List<String> res, Error error, boolean matched, Expression assumptionToRemove,
        Expression expression) throws ExceptionWithCustomError
    {
        if (matched) {
            if (assumptionToRemove != null) {
                res.add(new Expression(assumptionToRemove, expression, "->").representation);
            }
            return res;
        } else {
            if (error.code != -1) {
                throw new ExceptionWithCustomError(error);
            } else {
                return null;
            }
        }
    }
}
