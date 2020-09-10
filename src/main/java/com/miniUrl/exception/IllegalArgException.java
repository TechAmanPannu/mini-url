package com.miniUrl.exception;


import com.miniUrl.exception.enums.ErrorCode;

public class IllegalArgException extends AbstractRuntimeException {

    public IllegalArgException() {
        super();
    }

    public IllegalArgException(String message) {
        super(message);
    }

    public IllegalArgException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalArgException(ErrorCode error) {
        super(error);
    }

    public IllegalArgException(ErrorCode error, String msg) {
        super(error, msg);
    }

    public IllegalArgException(ErrorCode error, String msg, Object info) {
        super(error, msg, info);
    }
}
