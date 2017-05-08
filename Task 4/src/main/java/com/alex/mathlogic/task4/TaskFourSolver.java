package com.alex.mathlogic.task4;

import com.alex.mathlogic.common.Solver;
import com.alex.mathlogic.task4.axioms.Axiom;
import com.alex.mathlogic.task4.axioms.Axiom1;
import com.alex.mathlogic.task4.axioms.Axiom10;
import com.alex.mathlogic.task4.axioms.Axiom2;
import com.alex.mathlogic.task4.axioms.Axiom3;
import com.alex.mathlogic.task4.axioms.Axiom8;
import com.alex.mathlogic.task4.axioms.Axiom9;
import com.alex.mathlogic.task4.axioms.Axioms4And5;
import com.alex.mathlogic.task4.axioms.Axioms6And7;
import com.alex.mathlogic.task4.error.Error;
import com.alex.mathlogic.task4.error.ExceptionWithCustomError;
import com.alex.mathlogic.task4.rules.ExistsRule;
import com.alex.mathlogic.task4.rules.ForAllRule;
import com.alex.mathlogic.task4.rules.ModusPonens;
import com.alex.mathlogic.task4.rules.Rule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

class TaskFourSolver implements Solver {

    private ExpressionParser parser;

    private int lastErrorCode = 0;
    private int errorCode = 0;
    private Expression errTerm;
    private Expression errExpr;
    private String errVariable;

    private List<Expression> implToConj;
    private List<Expression> conjToImpl;
    private List<Expression> implChange;

    private ParseHelper helper = new ParseHelper();

    private List<Axiom> axioms;
    private List<Rule> rules;

    private Expression theta = null;
    private boolean correct = true;

    public void solve(String inputFilename, String outputFilename) {
        parser = null;
        lastErrorCode = 0;
        errorCode = 0;
        errTerm = null;
        errExpr = null;
        errVariable = null;
        implToConj = null;
        conjToImpl = null;
        implChange = null;
        helper = new ParseHelper();
        axioms = null;
        rules = null;
        theta = null;
        correct = true;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(inputFilename)));
             PrintWriter printWriter = new PrintWriter(new FileWriter(new File(outputFilename))))
        {
            String s;
            Expression expr;
            Expression assumptionToRemove = null;
            Expression bettaExpr;
            List<Expression> assumptions = new ArrayList<>();
            List<String> answer = new ArrayList<>();
            parser = new ExpressionParser();
            initProve();
            initAxioms();
            initRules();
            s = bufferedReader.readLine();
            while (s == null || s.equals("")) {
                s = bufferedReader.readLine();
            }

            s = s.replace(" ", "");

            if (s.contains("|-")) {
                int balance = 0;
                for (int i = 0; i < s.indexOf("|-"); ++i) {
                    if (s.charAt(i) == '(') {
                        balance++;
                    } else if (s.charAt(i) == ')') {
                        balance--;
                    }
                    if (s.charAt(i) == ',' && balance == 0) {
                        expr = parser.parse(s.substring(0, i));
                        assumptions.add(expr);
                        s = s.substring(i + 1);
                    }
                }

                if (s.indexOf("|-") == 0) {
                    assumptionToRemove = null;
                } else {
                    assumptionToRemove = parser.parse(s.substring(0, s.indexOf("|-")));
                }
                s = s.substring(s.indexOf("|-") + 2);
                bettaExpr = parser.parse(s);
                StringBuilder firstLine = new StringBuilder();
                for (int i = 0; i < assumptions.size() - 1; ++i) {
                    firstLine.append(assumptions.get(i).representation).append(",");
                }
                if (assumptions.size() > 0) {
                    firstLine.append(assumptions.get(assumptions.size() - 1).representation);
                }
                firstLine.append("|-");
                if (assumptionToRemove != null) {
                    firstLine.append(assumptionToRemove.representation).append("->");
                }
                firstLine.append(bettaExpr.representation);
                answer.add(firstLine.toString());
            }
            List<String> deductionRes = deduction(assumptions, assumptionToRemove, bufferedReader, printWriter);
            if (deductionRes != null) {
                answer.addAll(deductionRes);
                for (String anAnswer : answer) {
                    printWriter.println(anAnswer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> deduction(List<Expression> assumptions, Expression assumptionToRemove,
        BufferedReader bufferedReader, PrintWriter printWriter)
    {
        List<String> res = new ArrayList<>();
        Expression expr;
        List<Expression> proved = new ArrayList<>();
        String s;
        int lineNumber = -1;
        try {
            while (true) {
                s = bufferedReader.readLine();
                s = s.replace(" ", "");
                if (s.equals("")) {
                    continue;
                }
                lineNumber++;
                expr = parser.parse(s);
                proved.add(expr);
                int axiom = checkAxiom(expr);
                boolean isAxiomOrAssumption = axiom > 0;

                for (Expression assumption : assumptions) {
                    if (assumption.representation.equals(expr.representation)) {
                        isAxiomOrAssumption = true;
                        break;
                    }
                }

                List<Expression> variablesToSubstitute = new ArrayList<>();
                if (isAxiomOrAssumption) {
                    res.add(expr.representation);
                    if (assumptionToRemove != null) {
                        variablesToSubstitute.add(expr);
                        variablesToSubstitute.add(assumptionToRemove);
                        res.add(parser.parse("1->(2->1)").substitute(variablesToSubstitute).representation);
                    }
                    addExprIfNotNull(assumptionToRemove, expr, res);
                    continue;
                }

                if (assumptionToRemove != null && assumptionToRemove.representation.equals(expr.representation)) {
                    variablesToSubstitute.clear();
                    variablesToSubstitute.add(expr);
                    Expression eTmp = new Expression(expr, expr, "->");
                    variablesToSubstitute.add(eTmp);
                    res.add(parser.parse("1->2").substitute(variablesToSubstitute).representation);
                    res.add(parser.parse("(1->2)->(1->(2->1))->2").substitute(variablesToSubstitute).representation);
                    res.add(parser.parse("(1->(2->1))->2").substitute(variablesToSubstitute).representation);
                    res.add(parser.parse("1->(2->1)").substitute(variablesToSubstitute).representation);
                    addExprIfNotNull(assumptionToRemove, expr, res);
                    continue;
                }

                boolean matched = false;
                for (Rule rule : rules) {
                    try {
                        List<String> ruleResult = rule.check(expr, proved, assumptionToRemove);
                        if (ruleResult != null) {
                            res.addAll(ruleResult);
                            matched = true;
                            break;
                        }
                    } catch (ExceptionWithCustomError e) {
                        Error error = e.getError();
                        errorCode = error.code;
                        errVariable = error.variable;
                        errExpr = error.expression;
                        break;
                    }
                }
                if (matched) {
                    continue;
                }

                printWriter.print("Вывод некорректен начиная с формулы номер " + (lineNumber + 1));
                switch (errorCode) {
                    case 1:
                        printWriter.print(
                            ": терм " + errTerm.representation + " не свободен для подстановки в формулу " +
                                errExpr.representation + " вместо переменной " + errVariable + ".");
                        break;
                    case 2:
                        printWriter.print(
                            ": переменная " + errVariable + " входит свободно в формулу " + errExpr.representation +
                                ".");
                        break;
                    case 3:
                        printWriter.print(": используется правило с квантором по переменной " + errVariable +
                            ", входящей свободно в допущение " + errExpr.representation + ".");
                        break;
                }
                return null;
            }
        } catch (Exception ignored) {
        }

        return res;
    }

    private void addExprIfNotNull(Expression assumptionToRemove, Expression expr, List<String> res) {
        if (assumptionToRemove != null) {
            res.add(new Expression(assumptionToRemove, expr, "->").representation);
        }
    }


    private int checkAxiom(Expression expr) {
        boolean operatorsMatches;

        for (Axiom axiom : axioms) {
            int result = axiom.check(expr);
            if (result != 0) {
                return result;
            }
        }

        if (expr.representation.equals("((a=b)->(a'=b'))")) {
            return 13;
        }
        if (expr.representation.equals("((a=b)->((a=c)->(b=c)))")) {
            return 14;
        }
        if (expr.representation.equals("((a'=b')->(a=b))")) {
            return 15;
        }
        if (expr.representation.equals("!(a'=0)")) {
            return 16;
        }
        if (expr.representation.equals("((a+b')=(a+b)')")) {
            return 17;
        }
        if (expr.representation.equals("((a+0)=a)")) {
            return 18;
        }
        if (expr.representation.equals("((a*0)=0)")) {
            return 19;
        }
        if (expr.representation.equals("((a*b')=((a*b)+a))")) {
            return 20;
        }

        // induction
        try {
            operatorsMatches = expr.oper.equals("->");
            operatorsMatches &= expr.first.oper.equals("&");
            operatorsMatches &= expr.first.second.oper.equals("@");
            operatorsMatches &= expr.first.second.second.oper.equals("->");
            Expression psi0 = expr.first.first;
            Expression psi1 = expr.first.second.second.first;
            Expression psi2 = expr.first.second.second.second;
            Expression psi3 = expr.second;
            String x = expr.first.second.first.representation;
            if (operatorsMatches && psi1.representation.equals(psi3.representation) &&
                substituteAndCompare(psi1, psi0, x)
                && theta.representation.equals("0") && substituteAndCompare(psi1, psi2, x) &&
                theta.representation.equals(x + "'"))
            {
                return 21;
            }

        } catch (Exception ignored) {
        }
        // axiom forall
        try {
            lastErrorCode = 0;
            operatorsMatches = expr.oper.equals("->");
            operatorsMatches &= expr.first.oper.equals("@");
            substituteAndCompare(expr.first.second, expr.second, expr.first.first.representation);
            if (operatorsMatches && correct) {// check substitution error
                return 11;
            }
            if (lastErrorCode == 1) {
                errorCode = 1;
                errVariable = expr.first.first.representation;
                errExpr = expr.first.second;
                errTerm = theta;
            }

        } catch (Exception ignored) {
        }
        // axiom exists
        try {
            lastErrorCode = 0;
            operatorsMatches = expr.oper.equals("->");
            operatorsMatches &= expr.second.oper.equals("?");
            substituteAndCompare(expr.second.second, expr.first, expr.second.first.representation);
            if (operatorsMatches && correct) {// check substitution error
                return 12;
            }
            if (lastErrorCode == 1) {
                errorCode = lastErrorCode;
                errVariable = expr.first.first.representation;
                errExpr = expr.first.second;
                errTerm = theta;
            }

        } catch (Exception ignored) {
        }

        return 0;
    }

    private boolean substituteAndCompare(Expression a, Expression b, String x) {
        theta = null;
        correct = true;

        try {
            dfs(a, b, x);
        } catch (Exception e) {
            correct = false;
        }
        if (correct && (theta != null || a.representation.equals(b.representation))) {
            return true;
        }
        correct = false;
        return false;
    }

    private void dfs(Expression a, Expression b, String x) {
        if (!a.freeVariables.contains(x)) {
            return;
        }
        if (a.first != null) {
            if (a.first.freeVariables.contains(x)) {
                dfs(a.first, b.first, x);
            } else if (!a.first.representation.equals(b.first.representation)) {
                throw new RuntimeException();
            }

            if (a.second != null) {
                if (a.second.freeVariables.contains(x)) {
                    dfs(a.second, b.second, x);
                } else if (!a.second.representation.equals(b.second.representation)) {
                    throw new RuntimeException();
                }
            }
            if (a.oper.equals("@") || a.oper.equals("?")) {
                if (!b.freeVariables.containsAll(theta.freeVariables)) {
                    lastErrorCode = 1;
                    throw new RuntimeException();
                }
            }
        }

        if (a.terms != null) {
            for (int i = 0; i < a.terms.size(); ++i) {
                if (a.terms.get(i).freeVariables.contains(x)) {
                    dfs(a.terms.get(i), b.terms.get(i), x);
                } else if (!a.terms.get(i).representation.equals(b.terms.get(i).representation)) {
                    throw new RuntimeException();
                }

            }
        }

        if (a.oper.equals(x) && a.terms == null) {
            if (theta == null) {
                theta = b;
            } else if (!theta.representation.equals(b.representation)) {
                correct = false;
                throw new RuntimeException();
            }

        }
    }

    private void initProve() {
        implToConj = helper.parse(ExpressionConstants.IMPL_TO_CONJ);
        conjToImpl = helper.parse(ExpressionConstants.CONJ_TO_IMPL);
        implChange = helper.parse(ExpressionConstants.IMPL_CHANGE);
    }

    private void initAxioms() {
        axioms = new ArrayList<>();
        axioms.add(new Axiom1());
        axioms.add(new Axiom2());
        axioms.add(new Axiom3());
        axioms.add(new Axioms4And5());
        axioms.add(new Axioms6And7());
        axioms.add(new Axiom8());
        axioms.add(new Axiom9());
        axioms.add(new Axiom10());
    }

    private void initRules() {
        rules = new ArrayList<>();
        rules.add(new ModusPonens(parser));
        rules.add(new ForAllRule(parser, implToConj, conjToImpl));
        rules.add(new ExistsRule(parser, implChange));
    }
}
