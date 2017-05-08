package com.alex.mathlogic.task4;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Alexey Katsman
 * @since 10.06.16
 */

public class Expression {
    public String representation;
    public String oper;
    String rest;
    boolean inBraces = false;
    public Expression first, second;
    List<Expression> terms;
    public Set<String> freeVariables;

    public Expression() {
        freeVariables = new HashSet<>();
    }

    public Expression(Expression first, Expression second, String oper) {
        if (first != null) {
            freeVariables = new HashSet<>();
            this.first = new Expression(first);
            this.representation = first.representation;
            this.inBraces = first.inBraces;
            this.oper = oper;
            if (second != null) {
                this.second = new Expression(second);
                this.representation = "(" + this.first.representation + oper + this.second.representation + ")";
                this.inBraces = true;
            } else {
                this.representation = oper + this.representation;
            }
        }

    }

    public Expression(Expression other) {
        freeVariables = new HashSet<>(other.freeVariables);
        this.representation = other.representation;
        this.oper = other.oper;
        this.inBraces = other.inBraces;
        if (other.first != null) {
            this.first = new Expression(other.first);
        }
        if (other.second != null) {
            this.second = new Expression(other.second);
        }
    }

    public Expression substitute(List<Expression> vec) {
        if (first != null) {
            first.substitute(vec);
            this.representation = first.representation;
            if (second != null) {
                second.substitute(vec);
                this.representation += oper + second.representation;
                if (inBraces) {
                    this.representation = "(" + this.representation + ")";
                }
            } else {
                this.representation = oper + first.representation;
            }
        } else {
            Expression expr = vec.get(Integer.parseInt(this.representation) - 1);
            this.first = expr.first;
            this.second = expr.second;
            this.oper = expr.oper;
            this.representation = expr.representation;
            this.inBraces = expr.inBraces;
        }
        return this;
    }
}
