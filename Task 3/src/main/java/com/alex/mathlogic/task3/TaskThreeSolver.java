package com.alex.mathlogic.task3;

import com.alex.mathlogic.common.Axioms;
import com.alex.mathlogic.common.Pair;
import com.alex.mathlogic.common.Solver;
import com.alex.mathlogic.common.Utils;
import com.alex.mathlogic.common.formula.FormulaChecker;
import com.alex.mathlogic.common.formula.FormulaGetter;
import com.alex.mathlogic.common.formula.FormulaParser;
import com.alex.mathlogic.common.node.Node;
import com.alex.mathlogic.common.node.Type;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Alexey Katsman
 * @since 30.01.17
 */

public class TaskThreeSolver implements Solver {

    private static final List<Node> axioms = new ArrayList<>();

    static {
        Axioms.getAxioms().forEach(e -> axioms.add(FormulaParser.parse(Utils.normalize(e))));
    }

    private List<Node> proof = new ArrayList<>();

    @Override
    public void solve(String inputFilename, String outputFilename) {
        proof.clear();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(inputFilename)));
             PrintWriter printWriter = new PrintWriter(new FileWriter(new File(outputFilename))))
        {

            String formula = bufferedReader.readLine();
            formula = Utils.normalize(formula);
            resolve(formula, printWriter);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resolve(String formula, PrintWriter printWriter) throws IOException {
        Node parsedFormula = FormulaParser.parse(formula);
        List<Map<String, Boolean>> varCases = new ArrayList<>();
        getAllVariableCases(varCases, parsedFormula);
        AtomicBoolean finish = new AtomicBoolean(false);

        varCases.forEach(varCase -> {
            if (finish.get()) {
                return;
            }

            if (!parsedFormula.evaluate(varCase)) {
                StringBuilder sb = new StringBuilder("Высказывание ложно при ");
                varCase.forEach((k, v) -> sb.append(String.format("%s=%s,", k, v ? "И" : "Л")));
                sb.deleteCharAt(sb.length() - 1);
                printWriter.println(sb.toString());
                finish.set(true);
            }
        });

        if (finish.get()) {
            return;
        }

        List<List<Node>> allProof = initInnerLists(varCases.size());
        List<List<Node>> allSupposes = initInnerLists(varCases.size());

        for (int i = 0; i < varCases.size(); i++) {
            getSupposes(varCases.get(i), allSupposes.get(i));
            makeDerivation(parsedFormula, allProof.get(i), varCases.get(i), true);
        }

        int varCnt = varCases.get(0).size();
        int supposesStep = 2;
        final int totalCnt = varCnt;

        while (varCnt != 0) {
            List<List<Node>> allNewProof = new ArrayList<>();

            for (int i = 0, currSupposesIndex = 0; i < allProof.size(); i += 2, currSupposesIndex += supposesStep) {
                List<Node> newProof = new ArrayList<>();
                int currParsedIndex = totalCnt - varCnt;
                exclude(allSupposes.get(currSupposesIndex), currParsedIndex + 1, totalCnt,
                    allSupposes.get(allSupposes.size() - 1).get(currParsedIndex), parsedFormula, allProof.get(i),
                    allProof.get(i + 1), newProof);
                allNewProof.add(newProof);
            }

            allProof = allNewProof;
            varCnt--;
            supposesStep *= 2;
        }

        proof = allProof.get(allProof.size() - 1);

        for (Node aProof : proof) {
            printNode(aProof, printWriter);
        }
    }

    private List<List<Node>> initInnerLists(int size) {
        List<List<Node>> result = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            result.add(new ArrayList<>());
        }

        return result;
    }

    private void printNode(Node node, PrintWriter printWriter) throws IOException {
        printWriter.println(node.getRecursiveStringNotation());
    }

    private void deduction(List<Node> parsedFormulas, List<Node> supposes, Node alpha, Node beta, List<Node> proof,
        int toP, int fromS, int toS)
    {
        if (!Objects.equals(parsedFormulas.get(toP - 1), beta)) {
            throw new IllegalStateException("Beta and last formula differ");
        }

        for (int i = 0; i < toP; i++) {
            Node formula = parsedFormulas.get(i);
            int axiomNumber = FormulaChecker.checkAxioms(formula, axioms);

            if (axiomNumber != 0 || FormulaChecker.checkSuppose(formula, supposes, fromS, toS)) {
                proof.add(formula);
                proof.add(FormulaGetter.getAxiom(axioms, 1, formula, alpha));
                proof.add(proof.get(proof.size() - 1).getRight());
            } else if (Objects.equals(formula, alpha)) {
                getAA(alpha, proof);
            } else {
                Optional<Pair<Integer, Integer>> modusPonensNumbers =
                    FormulaChecker.checkModusPonens(formula, parsedFormulas, i - 1, -1);

                Node first =
                    parsedFormulas.get(modusPonensNumbers.orElseThrow(IllegalStateException::new).getFirst() - 1);
                proof.add(FormulaGetter.getAxiom(axioms, 2, alpha, first, formula));
                proof.add(proof.get(proof.size() - 1).getRight());
                proof.add(proof.get(proof.size() - 1).getRight());
            }
        }
    }

    private void totalDeduction(List<Node> initialFormulas, List<Node> supposes, Node firstBeta, List<Node> proof) {
        Node beta = new Node(firstBeta.getStringNotation(), firstBeta.getLeft(), firstBeta.getRight());
        List<Node> formulas = new ArrayList<>(initialFormulas);

        for (int i = supposes.size() - 1; i > -1; i--) {
            List<Node> result = new ArrayList<>();
            deduction(formulas, supposes, supposes.get(i), beta, (i == 0 ? proof : result), formulas.size(), 0, i);
            formulas = result;
            beta = new Node(Type.IMPLICATION.getStringNotation(), supposes.get(i), beta);
        }
    }

    private void getContraPositive(Node a, Node b, List<Node> proof) {
        List<Node> formulas = new ArrayList<>();
        List<Node> supposes = new ArrayList<>();
        supposes.add(new Node(Type.IMPLICATION.getStringNotation(), a, b));
        supposes.add(getNotA(b));
        formulas.add(supposes.get(0));
        formulas.add(FormulaGetter.getAxiom(axioms, 9, a, b));
        formulas.add(formulas.get(formulas.size() - 1).getRight());
        formulas.add(FormulaGetter.getAxiom(axioms, 1, getNotA(b), a));
        formulas.add(supposes.get(1));
        formulas.add(new Node(Type.IMPLICATION.getStringNotation(), a, getNotA(b)));
        formulas.add(getNotA(a));
        totalDeduction(formulas, supposes, getNotA(a), proof);
    }

    private void getThird(Node a, List<Node> proof) {
        proof.add(FormulaGetter.getAxiom(axioms, 6, a, getNotA(a)));
        getContraPositive(a, new Node(Type.OR.getStringNotation(), a, getNotA(a)), proof);
        proof.add(proof.get(proof.size() - 1).getRight());
        proof.add(FormulaGetter.getAxiom(axioms, 7, a, getNotA(a)));
        getContraPositive(getNotA(a), new Node(Type.OR.getStringNotation(), a, getNotA(a)), proof);
        proof.add(proof.get(proof.size() - 1).getRight());
        proof.add(FormulaGetter
            .getAxiom(axioms, 9, getNotA(new Node(Type.OR.getStringNotation(), a, getNotA(a))), getNotA(a)));
        proof.add(proof.get(proof.size() - 1).getRight());
        proof.add(proof.get(proof.size() - 1).getRight());
        proof.add(FormulaGetter.getAxiom(axioms, 10, new Node(Type.OR.getStringNotation(), a, getNotA(a))));
        proof.add(proof.get(proof.size() - 1).getRight());
    }

    private void getAllVariableCases(List<Map<String, Boolean>> cases, Node formula) {
        Map<String, Boolean> vars = new HashMap<>();
        formula.getVariables(vars);
        List<String> varNames = new ArrayList<>(vars.keySet());

        for (int i = 0; i < (1 << vars.size()); i++) {
            Map<String, Boolean> curr = new HashMap<>();
            cases.add(curr);
            formula.getVariables(curr);

            for (int j = 0; j < varNames.size(); j++) {
                curr.put(varNames.get(j), ((i & (1 << j)) != 0));
            }
        }
    }

    private void getSupposes(Map<String, Boolean> vars, List<Node> supposes) {
        vars.forEach((k, v) -> {
            Node tmp = new Node(k);
            supposes.add(v ? tmp : getNotA(tmp));
        });
    }

    private void implFF(Node a, Node b, List<Node> proof) {
        List<Node> formulas = new ArrayList<>();
        List<Node> supposes = new ArrayList<>();
        formulas.add(getNotA(a));
        formulas.add(getNotA(b));
        formulas.add(a);
        formulas.add(FormulaGetter.getAxiom(axioms, 1, a, getNotA(b)));
        formulas.add(formulas.get(formulas.size() - 1).getRight());
        formulas.add(FormulaGetter.getAxiom(axioms, 1, getNotA(a), getNotA(b)));
        formulas.add(formulas.get(formulas.size() - 1).getRight());
        formulas.add(FormulaGetter.getAxiom(axioms, 9, getNotA(b), a));
        formulas.add(formulas.get(formulas.size() - 1).getRight());
        formulas.add(formulas.get(formulas.size() - 1).getRight());
        formulas.add(FormulaGetter.getAxiom(axioms, 10, b));
        formulas.add(formulas.get(formulas.size() - 1).getRight());
        supposes.add(getNotA(a));
        supposes.add(getNotA(b));
        deduction(formulas, supposes, a, b, proof, formulas.size(), 0, supposes.size());
    }

    private void makeDerivation(Node formula, List<Node> proof, Map<String, Boolean> vars, boolean isFirst) {
        if (isFirst) {
            formula.evaluate(vars);
        }

        if (Utils.isVariable(formula.getStringNotation()) || FormulaChecker.checkAxioms(formula, axioms) != 0) {
            proof.add(formula.getCurrentValue() ? formula : getNotA(formula));
            return;
        }

        Node left = formula.getLeft();
        Node right = formula.getRight();

        if (Objects.equals(formula.getStringNotation(), Type.OR.getStringNotation())) {
            if (left.getCurrentValue()) {
                makeDerivation(left, proof, vars, false);

                if (!Utils.isVariable(left.getStringNotation())) {
                    proof.add(left);
                }

                proof.add(FormulaGetter.getAxiom(axioms, 6, left, right));
                proof.add(proof.get(proof.size() - 1).getRight());
            } else if (right.getCurrentValue()) {
                makeDerivation(right, proof, vars, false);

                if (!Utils.isVariable(right.getStringNotation())) {
                    proof.add(right);
                }

                proof.add(FormulaGetter.getAxiom(axioms, 7, left, right));
                proof.add(proof.get(proof.size() - 1).getRight());
            } else {
                makeDerivation(getNotA(left), proof, vars, false);
                makeDerivation(getNotA(right), proof, vars, false);

                if (!Utils.isVariable(left.getStringNotation())) {
                    proof.add(getNotA(left));
                }

                if (!Utils.isVariable(right.getStringNotation())) {
                    proof.add(getNotA(right));
                }

                proof.add(FormulaGetter.getAxiom(axioms, 1, getNotA(left), formula));
                proof.add(proof.get(proof.size() - 1).getRight());
                getAA(left, proof);
                implFF(right, left, proof);
                proof.add(FormulaGetter.getAxiom(axioms, 8, left, right, left));
                proof.add(proof.get(proof.size() - 1).getRight());
                proof.add(proof.get(proof.size() - 1).getRight());
                proof.add(FormulaGetter.getAxiom(axioms, 9, formula, left));
                proof.add(proof.get(proof.size() - 1).getRight());
                proof.add(proof.get(proof.size() - 1).getRight());
            }
        } else if (Objects.equals(formula.getStringNotation(), Type.AND.getStringNotation())) {
            if (left.getCurrentValue() && right.getCurrentValue()) {
                makeDerivation(left, proof, vars, false);
                makeDerivation(right, proof, vars, false);

                if (!Utils.isVariable(left.getStringNotation())) {
                    proof.add(left);
                }

                if (!Utils.isVariable(right.getStringNotation())) {
                    proof.add(right);
                }

                proof.add(FormulaGetter.getAxiom(axioms, 3, left, right));
                proof.add(proof.get(proof.size() - 1).getRight());
                proof.add(proof.get(proof.size() - 1).getRight());
            } else if (left.getCurrentValue() && !right.getCurrentValue()) {
                makeDerivation(getNotA(right), proof, vars, false);
                proof.add(FormulaGetter.getAxiom(axioms, 5, left, right));

                if (!Utils.isVariable(right.getStringNotation())) {
                    proof.add(getNotA(right));
                }

                proof.add(FormulaGetter
                    .getAxiom(axioms, 1, getNotA(right), new Node(Type.AND.getStringNotation(), left, right)));
                proof.add(proof.get(proof.size() - 1).getRight());
                proof.add(FormulaGetter
                    .getAxiom(axioms, 9, new Node(Type.AND.getStringNotation(), left, right), right));
                proof.add(proof.get(proof.size() - 1).getRight());
                proof.add(proof.get(proof.size() - 1).getRight());
            } else {
                makeDerivation(getNotA(left), proof, vars, false);
                proof.add(FormulaGetter.getAxiom(axioms, 4, left, right));

                if (!Utils.isVariable(left.getStringNotation())) {
                    proof.add(getNotA(left));
                }

                proof.add(FormulaGetter
                    .getAxiom(axioms, 1, getNotA(left), new Node(Type.AND.getStringNotation(), left, right)));
                proof.add(proof.get(proof.size() - 1).getRight());
                proof.add(FormulaGetter
                    .getAxiom(axioms, 9, new Node(Type.AND.getStringNotation(), left, right), left));
                proof.add(proof.get(proof.size() - 1).getRight());
                proof.add(proof.get(proof.size() - 1).getRight());
            }
        } else if (Objects.equals(formula.getStringNotation(), Type.IMPLICATION.getStringNotation())) {
            if (right.getCurrentValue()) {
                makeDerivation(right, proof, vars, false);

                if (!Utils.isVariable(right.getStringNotation())) {
                    proof.add(right);
                }

                proof.add(FormulaGetter.getAxiom(axioms, 1, right, left));
                proof.add(proof.get(proof.size() - 1).getRight());
            } else if (!left.getCurrentValue() && !right.getCurrentValue()) {
                makeDerivation(getNotA(left), proof, vars, false);
                makeDerivation(getNotA(right), proof, vars, false);
                implFF(left, right, proof);
            } else if (left.getCurrentValue() && !right.getCurrentValue()) {
                makeDerivation(left, proof, vars, false);
                makeDerivation(getNotA(right), proof, vars, false);

                if (!Utils.isVariable(left.getStringNotation())) {
                    proof.add(left);
                }

                if (!Utils.isVariable(right.getStringNotation())) {
                    proof.add(getNotA(right));
                }

                proof.add(FormulaGetter.getAxiom(axioms, 1, getNotA(right), formula));
                proof.add(proof.get(proof.size() - 1).getRight());
                List<Node> formulas = new ArrayList<>();
                List<Node> supposes = new ArrayList<>();
                supposes.add(left);
                supposes.add(getNotA(right));
                formulas.add(left);
                formulas.add(formula);
                formulas.add(right);
                deduction(formulas, supposes, formula, right, proof, formulas.size(), 0, supposes.size());
                proof.add(FormulaGetter.getAxiom(axioms, 9, formula, right));
                proof.add(proof.get(proof.size() - 1).getRight());
                proof.add(proof.get(proof.size() - 1).getRight());
            }
        } else if (Objects.equals(formula.getStringNotation(), Type.NOT.getStringNotation())) {
            if (Objects.equals(right.getStringNotation(), Type.NOT.getStringNotation())) {
                Node subRight = right.getRight();

                if (subRight.getCurrentValue()) {
                    makeDerivation(subRight, proof, vars, false);

                    if (!Utils.isVariable(subRight.getStringNotation())) {
                        proof.add(subRight);
                    }

                    proof.add(FormulaGetter.getAxiom(axioms, 1, subRight, getNotA(subRight)));
                    proof.add(proof.get(proof.size() - 1).getRight());
                    getAA(getNotA(subRight), proof);
                    proof.add(FormulaGetter.getAxiom(axioms, 9, getNotA(subRight), subRight));
                    proof.add(proof.get(proof.size() - 1).getRight());
                    proof.add(proof.get(proof.size() - 1).getRight());
                } else {
                    makeDerivation(getNotA(subRight), proof, vars, false);

                    if (!Utils.isVariable(subRight.getStringNotation())) {
                        proof.add(getNotA(subRight));
                    }
                }
            } else {
                makeDerivation(right, proof, vars, false);
            }
        }
    }

    private void exclude(List<Node> supposes, int from, int to, Node p, Node a, List<Node> formulas1,
        List<Node> formulas2, List<Node> proof)
    {
        deduction(formulas1, supposes, getNotA(p), a, proof, formulas1.size(), from, to);
        deduction(formulas2, supposes, p, a, proof, formulas2.size(), from, to);
        getThird(p, proof);
        proof.add(FormulaGetter.getAxiom(axioms, 8, p, getNotA(p), a));
        proof.add(proof.get(proof.size() - 1).getRight());
        proof.add(proof.get(proof.size() - 1).getRight());
        proof.add(proof.get(proof.size() - 1).getRight());
    }

    private void getAA(Node a, List<Node> proof) {
        proof.add(FormulaGetter.getAxiom(axioms, 1, a, a));
        proof.add(FormulaGetter.getAxiom(axioms, 1, a, new Node(Type.IMPLICATION.getStringNotation(), a, a)));
        proof.add(FormulaGetter.getAxiom(axioms, 2, a, new Node(Type.IMPLICATION.getStringNotation(), a, a), a));
        proof.add(proof.get(proof.size() - 1).getRight());
        proof.add(proof.get(proof.size() - 1).getRight());
    }

    private Node getNotA(Node a) {
        return new Node(Type.NOT.getStringNotation(), null, a);
    }
}
