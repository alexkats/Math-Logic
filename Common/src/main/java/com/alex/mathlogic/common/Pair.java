package com.alex.mathlogic.common;

import java.util.Objects;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

public class Pair<A, B> {

    private final A first;
    private final B second;

    @SuppressWarnings("WeakerAccess")
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Pair<?, ?> other = (Pair<?, ?>) obj;

        return Objects.equals(first, other.first) && Objects.equals(second, other.second);
    }
}
