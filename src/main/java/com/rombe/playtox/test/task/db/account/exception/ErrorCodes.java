package com.rombe.playtox.test.task.db.account.exception;

public enum ErrorCodes {
    ACCOUNT_ALREADY_EXIST("Account with same id already exist"),
    ACCOUNT_NOT_EXIST("Account with given id doesn't exist");
    private final String message;

    ErrorCodes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
