package org.dev.shared.bankserver.exception;

public class IllegalAccountOperationException extends IllegalArgumentException {
    public IllegalAccountOperationException(String message) {
        super(message);
    }

    public IllegalAccountOperationException(String message, Exception cause) {
        super(message, cause);
    }
}
