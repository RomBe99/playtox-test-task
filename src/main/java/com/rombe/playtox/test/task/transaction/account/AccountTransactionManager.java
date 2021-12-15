package com.rombe.playtox.test.task.transaction.account;

import com.rombe.playtox.test.task.db.account.AccountDatabase;
import com.rombe.playtox.test.task.entity.Account;
import com.rombe.playtox.test.task.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class AccountTransactionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountTransactionManager.class);

    private final AtomicInteger transactionsCount;
    private final BlockingQueue<Transaction> transactions = new LinkedBlockingQueue<>();
    private final AccountDatabase accountDatabase;

    public AccountTransactionManager(AccountDatabase accountDatabase, int transactionAmount) {
        this.accountDatabase = accountDatabase;
        this.transactionsCount = new AtomicInteger(transactionAmount);
    }

    public void beginNextTransaction() {
        try {
            final var transaction = transactions.poll();

            if (transaction != null) {
                transaction.execute();
                transaction.commit();
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public boolean registerMoneyTransaction(String senderId, int amountMoneyToSend, String recipientId) {
        return registerNewTransaction(new MoneyTransaction(accountDatabase, senderId, amountMoneyToSend, recipientId));
    }

    public boolean registerCreateNewAccountTransaction(Account newAccount) {
        return registerNewTransaction(new CreateNewAccountTransaction(accountDatabase, newAccount));
    }

    public boolean hasTransactions() {
        return !transactions.isEmpty();
    }

    public boolean hasFreeTransactions() {
        return transactionsCount.get() > 0;
    }

    private boolean registerNewTransaction(Transaction transaction) {
        if (!hasFreeTransactions()) {
            return false;
        }

        transactions.add(transaction);
        transactionsCount.decrementAndGet();

        return true;
    }
}
