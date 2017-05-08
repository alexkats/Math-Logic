package com.alex.mathlogic.task8.logic;

/**
 * @author Alexey Katsman
 * @since 08.05.17
 */

class OrdinalUtils {

    private OrdinalUtils() {

    }

    static Ordinal append(Ordinal a, Ordinal b) {
        return a.isAtom() ? b : new Ordinal(a.getFirst(), append(a.getSecond(), b));
    }
}
