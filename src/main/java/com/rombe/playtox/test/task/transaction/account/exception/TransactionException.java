package com.rombe.playtox.test.task.transaction.account.exception;

public class TransactionException extends RuntimeException {
    private final TransactionErrorCodes errorCode;

    public TransactionException(TransactionErrorCodes errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return errorCode.getErrorMessage();
    }
}
