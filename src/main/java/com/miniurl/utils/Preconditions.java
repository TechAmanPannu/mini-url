package com.miniurl.utils;


import com.miniurl.exception.IllegalArgException;
import com.miniurl.exception.enums.ErrorCode;

public final class Preconditions {

    private Preconditions(){}

    public static void checkArgument(boolean expression) {

        if (expression)
            throw new IllegalArgException();
    }

    public static void checkArgument(boolean expression, String message) {
        checkArgument(expression, null, message);
    }

    public static void checkArgument(boolean expression, ErrorCode errorCode, String message) {
        checkArgument(expression, errorCode, message, null);
    }

    public static void checkArgument(boolean expression, ErrorCode errorCode, String message, Object info) {

        if (expression)
            throw new IllegalArgException(errorCode, String.valueOf(message), info);
    }
}
