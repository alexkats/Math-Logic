package com.alex.mathlogic.task3;

/**
 * @author Alexey Katsman
 * @since 30.01.17
 */

public class Runner {

    private static final String INPUT_FILENAME = "res/task3/custom.in";
    private static final String OUTPUT_FILENAME = "res/task3/custom.out";

    public static void main(String[] args) {
        new TaskThreeSolver().solve(INPUT_FILENAME, OUTPUT_FILENAME);
    }
}
