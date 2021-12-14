package com.rombe.playtox.test.task.transaction.account.exception;

public enum TransactionErrorCodes {
    NEGATIVE_BALANCE("Account balance must be non-negative"),
    ACCOUNT_NOT_FOUND("Account with given ID not found");

    private final String errorMessage;

    TransactionErrorCodes(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
