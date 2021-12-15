package com.rombe.playtox.test.task.transaction.account;

import com.rombe.playtox.test.task.db.account.AccountDatabase;
import com.rombe.playtox.test.task.entity.Account;
import com.rombe.playtox.test.task.transaction.account.exception.TransactionErrorCodes;
import com.rombe.playtox.test.task.transaction.account.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoneyTransaction extends AccountDatabaseTransaction {
    private static final Logger LOGGER = LoggerFactory.getLogger(MoneyTransaction.class);

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
        databaseChanges.add(() -> {
            var account = database.read(senderId);

            if (account == null) {
                LOGGER.error("Can't read account by id = {}", senderId);

                throw new TransactionException(TransactionErrorCodes.ACCOUNT_NOT_FOUND);
            }

            var newBalance = account.getMoney() - amountMoneyToSend;

            if (newBalance < 0) {
                LOGGER.error("Account = {} balance must be non-negative after money transaction. Send sum = {}", account, amountMoneyToSend);

                throw new TransactionException(TransactionErrorCodes.NEGATIVE_BALANCE);
            }

            database.update(senderId, new Account(senderId, newBalance));

            LOGGER.info("Sender {} balance successfully updated on new sum = {}", senderId, newBalance);
        });
    }

    private void prepareRecipient() {
        databaseChanges.add(() -> {
            var account = database.read(recipientId);

            if (account == null) {
                LOGGER.error("Can't read account by id = {}", recipientId);

                throw new TransactionException(TransactionErrorCodes.ACCOUNT_NOT_FOUND);
            }

            var newBalance = account.getMoney() + amountMoneyToSend;
            database.update(recipientId, new Account(recipientId, newBalance));

            LOGGER.info("Recipient {} balance successfully updated on new sum = {}", senderId, newBalance);
        });
    }
}
