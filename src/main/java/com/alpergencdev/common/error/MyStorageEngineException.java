package com.alpergencdev.common.error;

public class MyStorageEngineException extends Exception {

    public MyStorageEngineException() {
        super();
    }

    public MyStorageEngineException(String message) {
        super(message);
    }

    public MyStorageEngineException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyStorageEngineException(Throwable cause) {
        super(cause);
    }

    protected MyStorageEngineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
