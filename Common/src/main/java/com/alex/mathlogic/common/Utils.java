package com.alex.mathlogic.common;

/**
 * @author Alexey Katsman
 * @since 26.10.16
 */

@SuppressWarnings({"unused", "WeakerAccess"})
public class Utils {

    public static String normalize(String s) {
        return s == null ? null : s.replaceAll(" ", "");
    }
}