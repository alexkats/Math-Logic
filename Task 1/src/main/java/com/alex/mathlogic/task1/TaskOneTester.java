package com.alex.mathlogic.task1;

import com.alex.mathlogic.common.Tester;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

public class TaskOneTester {

    private static final String PATH = "res/task1";

    public static void main(String[] args) {
        Tester.testAll(PATH, new TaskOneSolver());
    }
}
