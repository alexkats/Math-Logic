package com.alex.mathlogic.task8.logic;

/**
 * @author Alexey Katsman
 * @since 08.05.17
 */

class OrdinalEvaluator {

    private OrdinalEvaluator() {

    }

    static Ordinal add(Ordinal a, Ordinal b) {
        if (a.isAtom() && b.isAtom()) {
            return new Ordinal(a.getAtomValue() + b.getAtomValue());
        }

        int cmp = OrdinalComparator.compareOrdinals(OrdinalGetter.getFe(a), OrdinalGetter.getFe(b));
        Ordinal result;

        if (cmp == -1) {
            result = b;
        } else if (cmp == 1) {
            result = new Ordinal(new Ordinal(OrdinalGetter.getFe(a), OrdinalGetter.getFc(a)), add(a.getSecond(), b));
        } else {
            result = new Ordinal(new Ordinal(OrdinalGetter.getFe(a),
                new Ordinal(OrdinalGetter.getFc(a).getAtomValue() + OrdinalGetter.getFc(b).getAtomValue())),
                b.getSecond());
        }

        return result;
    }

    private static Ordinal addN(Ordinal a, Ordinal b, int n) {
        return OrdinalUtils.append(OrdinalGetter.getFirstN(a, n), add(OrdinalGetter.getSecondN(a, n), b));
    }

    private static Ordinal sub(Ordinal a, Ordinal b) {
        long aVal = a.getAtomValue();
        long bVal = b.getAtomValue();

        if (a.isAtom() && b.isAtom() && aVal <= bVal) {
            return new Ordinal(0);
        }

        if (a.isAtom() && b.isAtom()) {
            return new Ordinal(aVal - bVal);
        }

        int cmp = OrdinalComparator.compareOrdinals(OrdinalGetter.getFe(a), OrdinalGetter.getFe(b));

        if (cmp == -1) {
            return new Ordinal(0);
        }

        if (cmp == 1) {
            return a;
        }

        cmp = OrdinalComparator
            .compareAtoms(OrdinalGetter.getFc(a).getAtomValue(), OrdinalGetter.getFc(b).getAtomValue());

        if (cmp == -1) {
            return new Ordinal(0);
        }

        if (cmp == 1) {
            return new Ordinal(new Ordinal(OrdinalGetter.getFe(a),
                OrdinalGetter.getFc(a).getAtomValue() - OrdinalGetter.getFc(b).getAtomValue()), a.getSecond());
        }

        return sub(a.getSecond(), b.getSecond());
    }

    static Ordinal mul(Ordinal a, Ordinal b) {
        return mulN(a, b, 0);
    }

    private static int c(Ordinal a, Ordinal b) {
        if (OrdinalComparator.compareOrdinals(OrdinalGetter.getFe(b), OrdinalGetter.getFe(a)) == -1) {
            return 1 + c(a.getSecond(), b);
        }

        return 0;
    }

    private static int cN(Ordinal a, Ordinal b, int n) {
        return n + c(OrdinalGetter.getSecondN(a, n), b);
    }

    private static Ordinal mulN(Ordinal a, Ordinal b, int n) {
        long aVal = a.getAtomValue();
        long bVal = b.getAtomValue();

        if (a.isAtom() && aVal == 0 || b.isAtom() && bVal == 0) {
            return new Ordinal(0);
        }

        if (a.isAtom() && b.isAtom()) {
            return new Ordinal(aVal * bVal);
        }

        if (b.isAtom()) {
            return new Ordinal(
                new Ordinal(OrdinalGetter.getFe(a), new Ordinal(OrdinalGetter.getFc(a).getAtomValue() * bVal)),
                a.getSecond());
        }

        int m = cN(OrdinalGetter.getFe(a), OrdinalGetter.getFe(b), n);
        return new Ordinal(new Ordinal(addN(OrdinalGetter.getFe(a), OrdinalGetter.getFe(b), m), OrdinalGetter.getFc(b)),
            mulN(a, b.getSecond(), m));
    }

    static Ordinal exp(Ordinal a, Ordinal b) {
        Ordinal zero = OrdinalGetter.getZero();
        Ordinal one = OrdinalGetter.getOne();
        long aVal = a.getAtomValue();
        long bVal = b.getAtomValue();

        if (OrdinalComparator.compareOrdinals(a, one) == 0 || OrdinalComparator.compareOrdinals(b, zero) == 0) {
            return one;
        }

        if (OrdinalComparator.compareOrdinals(a, zero) == 0) {
            return zero;
        }

        if (a.isAtom() && b.isAtom()) {
            return new Ordinal(expw(aVal, bVal));
        }

        if (a.isAtom()) {
            return exp1(aVal, b);
        }

        if (b.isAtom()) {
            return exp3(a, bVal);
        }

        return exp4(a, b);
    }

    private static long expw(long a, long b) {
        long result = 1;

        while (b != 0) {
            if ((b & 1) != 0) {
                result *= a;
            }

            a *= a;
            b /= 2;
        }

        return result;
    }

    private static Ordinal exp1(long p, Ordinal a) {
        if (OrdinalComparator.compareOrdinals(OrdinalGetter.getFe(a), OrdinalGetter.getOne()) == 0) {
            return new Ordinal(new Ordinal(OrdinalGetter.getFc(a), expw(p, a.getSecond().getAtomValue())), 0);
        }

        if (a.getSecond().isAtom()) {
            return new Ordinal(new Ordinal(
                new Ordinal(new Ordinal(sub(OrdinalGetter.getFe(a), OrdinalGetter.getOne()), OrdinalGetter.getFc(a)),
                    OrdinalGetter.getZero()), expw(p, a.getSecond().getAtomValue())), OrdinalGetter.getZero());
        }

        Ordinal c = exp1(p, a.getSecond());
        return new Ordinal(new Ordinal(
            new Ordinal(new Ordinal(sub(OrdinalGetter.getFe(a), OrdinalGetter.getOne()), OrdinalGetter.getOne()),
                OrdinalGetter.getFe(c)), OrdinalGetter.getFc(c)), OrdinalGetter.getZero());
    }

    private static Ordinal exp2(Ordinal a, long q) {
        if (q == 1) {
            return a;
        }

        return mul(new Ordinal(new Ordinal(mul(OrdinalGetter.getFe(a), new Ordinal(q - 1)), 1), 0), a);
    }

    private static boolean limitN(Ordinal a) {
        if (a.isAtom()) {
            return a.getAtomValue() == 0;
        }

        return limitN(a.getSecond());
    }

    private static Ordinal limitPart(Ordinal a) {
        if (a.isAtom()) {
            return OrdinalGetter.getZero();
        }

        return new Ordinal(a.getFirst(), limitPart(a.getSecond()));
    }

    private static long natPart(Ordinal a) {
        if (a.isAtom()) {
            return a.getAtomValue();
        }

        return natPart(a.getSecond());
    }

    private static Ordinal exp3(Ordinal a, long q) {
        if (q == 0) {
            return OrdinalGetter.getOne();
        }

        if (q == 1) {
            return a;
        }

        if (limitN(a)) {
            return exp2(a, q);
        }

        return mul(exp3(a, q - 1), a);
    }

    private static Ordinal exp4(Ordinal a, Ordinal b) {
        return mul(new Ordinal(new Ordinal(mul(OrdinalGetter.getFe(a), limitPart(b)), OrdinalGetter.getOne()),
            OrdinalGetter.getZero()), exp3(a, natPart(b)));
    }
}
