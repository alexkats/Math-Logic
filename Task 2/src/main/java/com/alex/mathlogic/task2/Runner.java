package com.alex.mathlogic.task2;

/**
 * @author Alexey Katsman
 * @since 30.01.17
 */

public class Runner {

    private static final String INPUT_FILENAME = "res/task2/custom.in";
    private static final String OUTPUT_FILENAME = "res/task2/custom.out";

    public static void main(String[] args) {
        new TaskTwoSolver().solve(INPUT_FILENAME, OUTPUT_FILENAME);
    }
}
