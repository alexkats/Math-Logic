package com.alex.mathlogic.task3;

import com.alex.mathlogic.common.Tester;

/**
 * @author Alexey Katsman
 * @since 30.01.17
 */

public class TaskThreeTester {

    private static final String PATH = "res/task3";

    public static void main(String[] args) {
        Tester.testAll(PATH, new TaskThreeSolver());
    }
}
