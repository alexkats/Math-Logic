package com.alex.mathlogic.common.node;

/**
 * @author Alexey Katsman
 * @since 15.10.16
 */

public enum Type {
    AND("&"),
    OR("|"),
    NOT("!"),
    IMPLICATION("->");

    private String stringNotation;

    Type(String stringNotation) {
        this.stringNotation = stringNotation;
    }

    public String getStringNotation() {
        return stringNotation;
    }
}
