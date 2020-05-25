package com.np.design.exception;

public class NpException extends RuntimeException {

    public NpException() {
        super();
    }

    public NpException(String message) {
        super(message);
    }

    public NpException(String message, Exception ex) {
        super(message, ex);
    }

    public NpException(String message, Throwable cause) {
        super(message, cause);
    }

    public NpException(Throwable cause) {
        super(cause);
    }
}
