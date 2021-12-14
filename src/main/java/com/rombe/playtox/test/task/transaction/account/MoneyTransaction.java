package com.rombe.playtox.test.task.transaction.account;

import com.rombe.playtox.test.task.db.account.AccountDatabase;
import com.rombe.playtox.test.task.entity.Account;
import com.rombe.playtox.test.task.transaction.account.exception.TransactionErrorCodes;
import com.rombe.playtox.test.task.transaction.account.exception.TransactionException;

public class MoneyTransaction extends AccountDatabaseTransaction {
    private final String senderId;
    private final int amountMoneyToSend;
    private final String recipientId;

    public MoneyTransaction(AccountDatabase database, String senderId, int amountMoneyToSend, String recipientId) {
        super(database);

        this.senderId = senderId;
        this.amountMoneyToSend = amountMoneyToSend;
        this.recipientId = recipientId;
    }

    @Override
    public void execute() {
        prepareSender();
        prepareRecipient();
    }

    private void prepareSender() {
        var account = database.read(senderId);

        if (account == null) {
            throw new TransactionException(TransactionErrorCodes.ACCOUNT_NOT_FOUND);
        }

        var newBalance = account.getMoney() - amountMoneyToSend;

        if (newBalance < 0) {
            throw new TransactionException(TransactionErrorCodes.NEGATIVE_BALANCE);
        }

        databaseChanges.add(() -> database.update(senderId, new Account(senderId, newBalance)));
    }

    private void prepareRecipient() {
        var account = database.read(recipientId);

        if (account == null) {
            throw new TransactionException(TransactionErrorCodes.ACCOUNT_NOT_FOUND);
        }

        var newBalance = account.getMoney() + amountMoneyToSend;

        databaseChanges.add(() -> database.update(recipientId, new Account(recipientId, newBalance)));
    }
}
