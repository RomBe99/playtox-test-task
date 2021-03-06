package com.rombe.playtox.test.task;

import com.rombe.playtox.test.task.db.account.AccountDatabase;
import com.rombe.playtox.test.task.entity.Account;
import com.rombe.playtox.test.task.looper.MainLoop;
import com.rombe.playtox.test.task.transaction.account.AccountTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Demo {
    private static final Logger LOGGER = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) {

        var database = new AccountDatabase();
        final var accountsAmount = 4;
        final int transactionsAmount = 30;
        var transactionManager = new AccountTransactionManager(database, accountsAmount + transactionsAmount);

        List<String> userIds = new ArrayList<>(accountsAmount);

        for (int i = 0; i < accountsAmount; i++) {
            var id = UUID.randomUUID().toString();

            transactionManager.registerCreateNewAccountTransaction(new Account(id, 10000));
            userIds.add(id);
        }

        final var threadsAmount = 4;
        List<Thread> threads = new ArrayList<>(threadsAmount);

        for (var i = 0; i < threadsAmount; i++) {
            threads.add(new Thread(() -> {
                var isReady = true;

                while (isReady) {
                    final var money = ThreadLocalRandom.current().nextInt(0, 10001);
                    final var senderId = userIds.get(ThreadLocalRandom.current().nextInt(0, userIds.size()));
                    final var recipientId = userIds.get(ThreadLocalRandom.current().nextInt(0, userIds.size()));

                    isReady = transactionManager.registerMoneyTransaction(senderId, money, recipientId);

                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 2000));
                    } catch (InterruptedException e) {
                        LOGGER.error("Something go wrong when thread = {} is sleep", Thread.currentThread().getName(), e);
                    }
                }
            }));
        }

        new MainLoop(transactionManager, threads)
                .startLoop();
    }
}