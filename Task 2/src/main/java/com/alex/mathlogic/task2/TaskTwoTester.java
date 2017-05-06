package com.alex.mathlogic.task2;

import com.alex.mathlogic.common.Tester;

/**
 * @author Alexey Katsman
 * @since 30.01.17
 */

public class TaskTwoTester {

    private static final String PATH = "res/task2";

    public static void main(String[] args) {
        Tester.testAll(PATH, new TaskTwoSolver());
    }
}
