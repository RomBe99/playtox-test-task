package com.rombe.playtox.test.task.looper;

import com.rombe.playtox.test.task.transaction.account.AccountTransactionManager;

import java.util.List;

public class MainLoop {
    private final List<Thread> threads;
    private boolean loopStarted = false;
    private final AccountTransactionManager transactionManager;

    public MainLoop(AccountTransactionManager transactionManager, List<Thread> threads) {
        this.transactionManager = transactionManager;
        this.threads = threads;
    }

    public void startLoop() {
        if (loopStarted) {
            return;
        }

        loopStarted = true;

        try {
            var threadsNotStarted = true;

            while (transactionManager.hasTransactions() || transactionManager.hasFreeTransactions()) {
                if (threadsNotStarted) {

                    for (var thread : threads) {
                        thread.start();
                    }

                    threadsNotStarted = false;
                }

                transactionManager.beginNextTransaction();
            }
        } finally {
            loopStarted = false;
        }
    }
}
