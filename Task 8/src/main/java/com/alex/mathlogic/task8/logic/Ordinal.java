package com.alex.mathlogic.task8.logic;

import java.util.Objects;

/**
 * @author Alexey Katsman
 * @since 08.05.17
 */

public class Ordinal {

    private Ordinal f;
    private Ordinal s;
    private long atomValue;

    Ordinal() {

    }

    Ordinal(long atomValue) {
        this.atomValue = atomValue;
    }

    Ordinal(Ordinal f, long s) {
        this(f, new Ordinal(s));
    }

    Ordinal(Ordinal f, Ordinal s) {
        this.f = f;
        this.s = s;
    }

    Ordinal getFirst() {
        return f;
    }

    Ordinal getSecond() {
        return s;
    }

    long getAtomValue() {
        return atomValue;
    }

    boolean isAtom() {
        return f == null && s == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(f, s, atomValue);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Ordinal other = (Ordinal) obj;

        return f.equals(other.f) && s.equals(other.s) && atomValue == other.atomValue;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public String toString() {
        if (isAtom()) {
            return String.valueOf(atomValue);
        }

        return "w^(" + f.f.toString() + ")*" + f.s.toString() + "+" + s.toString();
    }
}
