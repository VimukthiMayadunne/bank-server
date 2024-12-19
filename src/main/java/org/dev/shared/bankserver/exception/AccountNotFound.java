package org.dev.shared.bankserver.exception;

public class AccountNotFound extends RuntimeException {

    public AccountNotFound(String message) {
        super(message);
    }

}
