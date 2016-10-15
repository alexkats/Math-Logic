package com.alex.mathlogic.common;

/**
 * @author Alexey Katsman
 * @since 15.10.16
 */

@SuppressWarnings("WeakerAccess")
public class Axioms {

    private Axioms() {

    }

    public static final String AXIOM_1 = "A -> B -> A";
    public static final String AXIOM_2 = "(A -> B) -> (A -> B -> C) -> (A -> C)";
    public static final String AXIOM_3 = "A -> B -> A & B";
    public static final String AXIOM_4 = "A & B -> A";
    public static final String AXIOM_5 = "A & B -> B";
    public static final String AXIOM_6 = "A -> A | B";
    public static final String AXIOM_7 = "B -> A | B";
    public static final String AXIOM_8 = "(A -> C) -> (B -> C) -> (A | B -> C)";
    public static final String AXIOM_9 = "(A -> B) -> (A -> !B) -> !A";
    public static final String AXIOM_10 = "!!A -> A";
}
