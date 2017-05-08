package com.alex.mathlogic.task8;

import com.alex.mathlogic.common.Tester;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

public class TaskEightTester {

    private static final String PATH = "res/task8";

    public static void main(String[] args) {
        Tester.testAll(PATH, new TaskEightSolver());
    }
}
