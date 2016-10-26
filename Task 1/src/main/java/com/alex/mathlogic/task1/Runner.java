package com.alex.mathlogic.task1;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

public class Runner {

    private static final String INPUT_FILENAME = "res/task1/task1.in";
    private static final String OUTPUT_FILENAME = "res/task1/task1.out";

    public static void main(String[] args) {
        new Solver().solve(INPUT_FILENAME, OUTPUT_FILENAME);
    }
}
