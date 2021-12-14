package com.rombe.playtox.test.task.db.account;

import com.rombe.playtox.test.task.db.Database;
import com.rombe.playtox.test.task.db.account.exception.AccountDatabaseException;
import com.rombe.playtox.test.task.db.account.exception.ErrorCodes;
import com.rombe.playtox.test.task.entity.Account;

import java.util.HashMap;
import java.util.Map;

public class AccountDatabase implements Database<String, Account> {
    private final Map<String, Account> accounts = new HashMap<>();

    @Override
    public String create(String key, Account value) {
        if (accounts.containsKey(key)) {
            throw new AccountDatabaseException(ErrorCodes.ACCOUNT_ALREADY_EXIST);
        }

        accounts.put(key, value);

        return key;
    }

    @Override
    public Account read(String key) {
        return accounts.get(key);
    }

    @Override
    public Account update(String key, Account newValue) {
        if (!accounts.containsKey(key)) {
            throw new AccountDatabaseException(ErrorCodes.ACCOUNT_NOT_EXIST);
        }

        return accounts.put(key, newValue);
    }

    @Override
    public Account delete(String key) {
        return accounts.remove(key);
    }
}
