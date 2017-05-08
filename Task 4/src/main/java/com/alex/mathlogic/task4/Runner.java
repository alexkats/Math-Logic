package com.alex.mathlogic.task4;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

public class Runner {

    private static final String INPUT_FILENAME = "res/task4/custom.in";
    private static final String OUTPUT_FILENAME = "res/task4/custom.out";

    public static void main(String[] args) {
        new TaskFourSolver().solve(INPUT_FILENAME, OUTPUT_FILENAME);
    }
}
