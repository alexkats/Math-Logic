package com.alex.mathlogic.task8.logic;

/**
 * @author Alexey Katsman
 * @since 08.05.17
 */

public class OrdinalComparator {

    private OrdinalComparator() {

    }

    static int compareAtoms(long a, long b) {
        return Long.compare(a, b);
    }

    public static int compareOrdinals(Ordinal a, Ordinal b) {
        if (a.isAtom() && b.isAtom()) {
            return compareAtoms(a.getAtomValue(), b.getAtomValue());
        }

        if (a.isAtom()) {
            return -1;
        }

        if (b.isAtom()) {
            return 1;
        }

        int result = compareOrdinals(OrdinalGetter.getFe(a), OrdinalGetter.getFe(b));

        if (result == 0) {
            result = compareAtoms(OrdinalGetter.getFc(a).getAtomValue(), OrdinalGetter.getFc(b).getAtomValue());
        }

        if (result == 0) {
            result = compareOrdinals(a.getSecond(), b.getSecond());
        }

        return result;
    }
}
