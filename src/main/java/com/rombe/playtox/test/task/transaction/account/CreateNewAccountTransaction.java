package com.rombe.playtox.test.task.transaction.account;

import com.rombe.playtox.test.task.db.account.AccountDatabase;
import com.rombe.playtox.test.task.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateNewAccountTransaction extends AccountDatabaseTransaction {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateNewAccountTransaction.class);

    private final Account newAccount;

    public CreateNewAccountTransaction(AccountDatabase database, Account newAccount) {
        super(database);

        this.newAccount = newAccount;
    }

    @Override
    public void execute() {
        databaseChanges.add(() -> {
            LOGGER.info("Creating new account = {}", newAccount);

            database.create(newAccount.getId(), newAccount);

            LOGGER.info("New account = {} successfully created", newAccount);
        });
    }
}
