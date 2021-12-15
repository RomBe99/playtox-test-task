package com.rombe.playtox.test.task.looper;

import com.rombe.playtox.test.task.transaction.account.AccountTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MainLoop {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainLoop.class);

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
        } catch (RuntimeException e) {
            LOGGER.error("Something go wrong in main loop", e);
        } finally {
            loopStarted = false;
        }
    }
}
