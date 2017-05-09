package com.alex.mathlogic.task2;

import com.alex.mathlogic.common.Axioms;
import com.alex.mathlogic.common.Pair;
import com.alex.mathlogic.common.Solver;
import com.alex.mathlogic.common.Utils;
import com.alex.mathlogic.common.formula.FormulaChecker;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Alexey Katsman
 * @since 30.01.17
 */

public class TaskTwoSolver implements Solver {

    private static final List<Node> axioms = new ArrayList<>();

    static {
        Axioms.getAxioms().forEach(e -> axioms.add(FormulaParser.parse(Utils.normalize(e))));
    }

    private final List<Node> parsedFormulas = new ArrayList<>();
    private final List<Node> supposes = new ArrayList<>();

    private Node alpha;

    @Override
    public void solve(String inputFilename, String outputFilename) {
        parsedFormulas.clear();
        supposes.clear();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(inputFilename)));
             PrintWriter printWriter = new PrintWriter(new File(outputFilename), "UTF-8"))
        {

            String formula = bufferedReader.readLine();
            formula = Utils.normalize(formula);
            alpha = FormulaParser.parseSupposes(formula, supposes).getFirst();

            while (true) {
                formula = bufferedReader.readLine();
                formula = Utils.normalize(formula);

                if (formula == null || formula.isEmpty()) {
                    break;
                }

                resolve(formula, printWriter);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resolve(String formula, PrintWriter printWriter) throws IOException {
        Node parsedFormula = FormulaParser.parse(formula);
        parsedFormulas.add(parsedFormula);
        int axiomNumber = FormulaChecker.checkAxioms(parsedFormula, axioms);

        if (axiomNumber != 0 || FormulaChecker.checkSuppose(parsedFormula, supposes)) {
            printNode(parsedFormula, printWriter);
            Node rightSubNode = new Node(Type.IMPLICATION.getStringNotation(), alpha, parsedFormula);
            Node interNode = new Node(Type.IMPLICATION.getStringNotation(), parsedFormula, rightSubNode);
            printNode(interNode, printWriter);
        } else if (Objects.equals(parsedFormula, alpha)) {
            Node leftNode = new Node(Type.IMPLICATION.getStringNotation(), alpha,
                new Node(Type.IMPLICATION.getStringNotation(), alpha, alpha));
            Node middleNode = new Node(Type.IMPLICATION.getStringNotation(), alpha,
                new Node(Type.IMPLICATION.getStringNotation(),
                    new Node(Type.IMPLICATION.getStringNotation(), alpha, alpha), alpha));
            Node rightNode = new Node(Type.IMPLICATION.getStringNotation(), alpha, alpha);
            printNode(leftNode, printWriter);
            Node interNode = new Node(Type.IMPLICATION.getStringNotation(), leftNode,
                new Node(Type.IMPLICATION.getStringNotation(), middleNode, rightNode));
            printNode(interNode, printWriter);
            interNode = new Node(Type.IMPLICATION.getStringNotation(), middleNode, rightNode);
            printNode(interNode, printWriter);
            printNode(middleNode, printWriter);
        } else {
            Optional<Pair<Integer, Integer>> modusPonensNumbers =
                FormulaChecker.checkModusPonens(parsedFormula, parsedFormulas);
            Pair<Integer, Integer> realNumbers = modusPonensNumbers.orElseThrow(IllegalStateException::new);
            Node f1 = parsedFormulas.get(realNumbers.getFirst() - 1);
            Node leftNode = new Node(Type.IMPLICATION.getStringNotation(), alpha, f1);
            Node middleNode = new Node(Type.IMPLICATION.getStringNotation(), alpha,
                new Node(Type.IMPLICATION.getStringNotation(), f1, parsedFormula));
            Node rightNode = new Node(Type.IMPLICATION.getStringNotation(), alpha, parsedFormula);
            Node interNode = new Node(Type.IMPLICATION.getStringNotation(), leftNode,
                new Node(Type.IMPLICATION.getStringNotation(), middleNode, rightNode));
            printNode(interNode, printWriter);
            interNode = new Node(Type.IMPLICATION.getStringNotation(), middleNode, rightNode);
            printNode(interNode, printWriter);
        }

        Node interNode = new Node(Type.IMPLICATION.getStringNotation(), alpha, parsedFormula);
        printNode(interNode, printWriter);
    }

    private void printNode(Node node, PrintWriter printWriter) throws IOException {
        printWriter.println(node.getRecursiveStringNotation());
    }
}
