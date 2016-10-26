package com.alex.mathlogic.task1;

import com.alex.mathlogic.common.Axioms;
import com.alex.mathlogic.common.Pair;
import com.alex.mathlogic.common.Solver;
import com.alex.mathlogic.common.Utils;
import com.alex.mathlogic.common.formula.FormulaChecker;
import com.alex.mathlogic.common.formula.FormulaParser;
import com.alex.mathlogic.common.node.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

class TaskOneSolver implements Solver {

    private int formulaNumber;
    private static final List<Node> axioms = new ArrayList<>();

    static {
        Axioms.getAxioms().forEach(e -> axioms.add(FormulaParser.parse(Utils.normalize(e))));
    }

    private final List<Node> parsedFormulas = new ArrayList<>();

    public void solve(String inputFilename, String outputFilename) {
        parsedFormulas.clear();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(inputFilename)));
             PrintWriter printWriter = new PrintWriter(new FileWriter(new File(outputFilename)))) {

            formulaNumber = 0;

            while (true) {
                String formula = bufferedReader.readLine();
                formula = Utils.normalize(formula);

                if (formula == null || formula.isEmpty()) {
                    break;
                }

                formulaNumber++;

                if (resolve(formula, printWriter)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean resolve(String formula, PrintWriter printWriter) throws IOException {
        printWriter.printf(Locale.US, "(%d) %s ", formulaNumber, formula);
        Node parsedFormula = FormulaParser.parse(formula);
        parsedFormulas.add(parsedFormula);
        int axiomNumber = FormulaChecker.checkAxioms(parsedFormula, axioms);
        boolean end = false;

        if (axiomNumber == 0) {
            Optional<Pair<Integer, Integer>> modusPonensNumbers = FormulaChecker.checkModusPonens(parsedFormula, parsedFormulas);
            modusPonensNumbers.ifPresent(e -> printWriter.printf(Locale.US, "(M.P. %d, %d)\n", e.getFirst(), e.getSecond()));

            if (!modusPonensNumbers.isPresent()) {
                printWriter.printf(Locale.US, "(Не доказано)\n");
                end = true;
            }
        } else {
            printWriter.printf(Locale.US, "(Сх. акс. %d)\n", axiomNumber);
        }

        return end;
    }
}
