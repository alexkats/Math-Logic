package com.alex.mathlogic.task8.logic;

/**
 * @author Alexey Katsman
 * @since 08.05.17
 */

class OrdinalGetter {

    private OrdinalGetter() {

    }

    private static final Ordinal ZERO = new Ordinal(0);
    private static final Ordinal ONE = new Ordinal(1);
    private static final Ordinal W = new Ordinal(new Ordinal(ONE, 1), 0);

    static Ordinal getZero() {
        return ZERO;
    }

    static Ordinal getOne() {
        return ONE;
    }

    static Ordinal getW() {
        return W;
    }

    static Ordinal getFirstN(Ordinal a, int n) {
        return n == 0 ? new Ordinal() : new Ordinal(a.getFirst(), getFirstN(a.getSecond(), n - 1));
    }

    static Ordinal getSecondN(Ordinal a, int n) {
        return n == 0 ? a : getSecondN(a.getSecond(), n - 1);
    }

    static Ordinal getFe(Ordinal a) {
        return a.isAtom() ? new Ordinal(0) : a.getFirst().getFirst();
    }

    static Ordinal getFc(Ordinal a) {
        return a.isAtom() ? a : a.getFirst().getSecond();
    }
}
