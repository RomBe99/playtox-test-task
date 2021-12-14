package com.rombe.playtox.test.task.transaction.account;

import com.rombe.playtox.test.task.db.account.AccountDatabase;
import com.rombe.playtox.test.task.entity.Account;

public class CreateNewAccountTransaction extends AccountDatabaseTransaction {
    private final Account newAccount;

    public CreateNewAccountTransaction(AccountDatabase database, Account newAccount) {
        super(database);

        this.newAccount = newAccount;
    }

    @Override
    public void execute() {
        databaseChanges.add(() -> database.create(newAccount.getId(), newAccount));
    }
}
