package com.alex.mathlogic.task4.error;

/**
 * @author Alexey Katsman
 * @since 10.06.16
 */

public class ExceptionWithCustomError extends Exception {

    private final Error error;

    public ExceptionWithCustomError(Error error) {
        this.error = error;
    }

    public Error getError() {
        return error;
    }
}
