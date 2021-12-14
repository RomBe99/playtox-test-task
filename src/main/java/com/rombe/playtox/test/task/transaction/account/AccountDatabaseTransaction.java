package com.rombe.playtox.test.task.transaction.account;

import com.rombe.playtox.test.task.db.Database;
import com.rombe.playtox.test.task.db.account.AccountDatabase;
import com.rombe.playtox.test.task.entity.Account;
import com.rombe.playtox.test.task.transaction.Transaction;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AccountDatabaseTransaction implements Transaction {

    protected final Database<String, Account> database;
    protected final Queue<Runnable> databaseChanges = new LinkedList<>();

    public AccountDatabaseTransaction(AccountDatabase database) {
        this.database = database;
    }

    @Override
    public void commit() {
        while (!databaseChanges.isEmpty()) {
            databaseChanges.poll().run();
        }
    }

}
