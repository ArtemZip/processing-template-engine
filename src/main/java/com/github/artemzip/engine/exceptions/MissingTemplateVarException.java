package com.github.artemzip.engine.exceptions;

public class MissingTemplateVarException extends TemplateException {
    public MissingTemplateVarException() {
    }

    public MissingTemplateVarException(String message) {
        super(message);
    }

    public MissingTemplateVarException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingTemplateVarException(Throwable cause) {
        super(cause);
    }

    public MissingTemplateVarException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
