package com.alex.mathlogic.common.math;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

public class HashCodeHelper {

    private static final List<Long> primes = new ArrayList<>();
    private static final int QUANTITY = 5_000;
    private static final long CONST_31 = 31L;

    static {
        primes.add(1L);
        primes.add(CONST_31);

        for (int i = 2; i <= QUANTITY; i++) {
            primes.add(primes.get(i - 1) * CONST_31);
        }
    }

    private HashCodeHelper() {

    }

    public static long getPrime31(int num) {
        return primes.get(num);
    }
}
