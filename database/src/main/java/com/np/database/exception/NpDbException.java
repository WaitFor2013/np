package com.np.database.exception;

public class NpDbException extends RuntimeException {

    public NpDbException(){super();}

    public NpDbException(String message) {
        super(message);
    }

    public NpDbException(String message, Exception ex) {
        super(message,ex);
    }

    public NpDbException(String message, Throwable cause) {
        super(message, cause);
    }

    public NpDbException(Throwable cause) {
        super(cause);
    }

}
