package com.rombe.playtox.test.task.db.account.exception;

public class AccountDatabaseException extends RuntimeException {
    private final ErrorCodes errorCode;

    public AccountDatabaseException(ErrorCodes errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }
}
