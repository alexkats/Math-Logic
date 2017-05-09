package com.alex.mathlogic.task8;

import com.alex.mathlogic.common.Solver;
import com.alex.mathlogic.common.Utils;
import com.alex.mathlogic.task8.logic.Ordinal;
import com.alex.mathlogic.task8.logic.OrdinalComparator;
import com.alex.mathlogic.task8.logic.OrdinalParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

class TaskEightSolver implements Solver {

    public void solve(String inputFilename, String outputFilename) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(inputFilename)));
             PrintWriter printWriter = new PrintWriter(new File(outputFilename), "UTF-8"))
        {

            String s = bufferedReader.readLine();
            s = Utils.normalize(s);
            String[] ss = s.split("=");
            printWriter.println(check(ss[0], ss[1]) ? "Равны" : "Не равны");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean check(String s1, String s2) {
        Ordinal o1 = OrdinalParser.parse(s1);
        Ordinal o2 = OrdinalParser.parse(s2);
        return OrdinalComparator.compareOrdinals(o1, o2) == 0;
    }
}
