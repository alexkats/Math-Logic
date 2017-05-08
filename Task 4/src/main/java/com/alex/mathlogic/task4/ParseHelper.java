package com.alex.mathlogic.task4;

import java.util.ArrayList;

/**
 * @author Alexey Katsman
 * @since 10.06.16
 */

class ParseHelper {

    private final ExpressionParser parser;

    ParseHelper() {
        parser = new ExpressionParser();
    }

    ArrayList<Expression> parse(String[] strings) {
        ArrayList<Expression> res = new ArrayList<>();
        for (String string : strings) {
            res.add(parser.parse(string));
        }
        return res;
    }


}
