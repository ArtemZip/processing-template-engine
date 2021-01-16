package com.github.artemzip.engine.exceptions;

public class UseOperatorException extends TemplateException {
    public UseOperatorException() {
        super();
    }

    public UseOperatorException(String message) {
        super(message);
    }

    public UseOperatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public UseOperatorException(Throwable cause) {
        super(cause);
    }

    protected UseOperatorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
