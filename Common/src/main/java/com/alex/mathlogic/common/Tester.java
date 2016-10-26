package com.alex.mathlogic.common;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

public class Tester {

    private static final String END_INPUT_TEST_FILE = ".in";
    private static final String END_OUTPUT_TEST_FILE = ".out";

    public static void testAll(String stringPath, Solver solver) {
        final FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!file.toString().endsWith(END_INPUT_TEST_FILE)) {
                    return FileVisitResult.CONTINUE;
                }

                String stringFilename = file.toString().substring(0, file.toString().length() - 3);
                String inputFilename = stringFilename + END_INPUT_TEST_FILE;
                String outputFilename = stringFilename + END_OUTPUT_TEST_FILE;
                solver.solve(inputFilename, outputFilename);
                return FileVisitResult.CONTINUE;
            }
        };

        Path path = Paths.get(stringPath);

        try {
            Files.walkFileTree(path, visitor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
