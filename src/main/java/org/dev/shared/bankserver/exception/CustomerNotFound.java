package org.dev.shared.bankserver.exception;

public class CustomerNotFound extends RuntimeException {

    public CustomerNotFound(String message) {
        super(message);
    }

}
