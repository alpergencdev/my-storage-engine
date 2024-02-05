package com.alpergencdev.common.error;

public class MyStorageEngineRuntimeException extends RuntimeException {
    public MyStorageEngineRuntimeException() {
        super();
    }

    public MyStorageEngineRuntimeException(String message) {
        super(message);
    }

    public MyStorageEngineRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyStorageEngineRuntimeException(Throwable cause) {
        super(cause);
    }

    protected MyStorageEngineRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
