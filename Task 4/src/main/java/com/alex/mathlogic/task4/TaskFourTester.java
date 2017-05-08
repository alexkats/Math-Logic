package com.alex.mathlogic.task4;

import com.alex.mathlogic.common.Tester;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

public class TaskFourTester {

    private static final String PATH = "res/task4";

    public static void main(String[] args) {
        Tester.testAll(PATH, new TaskFourSolver());
    }
}
