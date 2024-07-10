package io.github.surajkumar.concurrency.machines;

import io.github.surajkumar.concurrency.exceptions.NoExecutionThreadAvailableException;
import io.github.surajkumar.concurrency.pools.ThreadPool;
import io.github.surajkumar.concurrency.promise.Promise;
import io.github.surajkumar.concurrency.threads.ExecutionSettings;
import io.github.surajkumar.concurrency.threads.ExecutionThread;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The PooledExecutionMachine class implements the ExecutionMachine interface. It uses a ThreadPool
 * to execute Promise instances.
 *
 * @param threadPool The ThreadPool to use for this machine
 */
public record PooledExecutionMachine(ThreadPool threadPool) implements ExecutionMachine {
    private static final Logger LOGGER = LogManager.getLogger(PooledExecutionMachine.class);

    @Override
    public void execute(Promise<?> promise, ExecutionSettings executionSettings) {
        LOGGER.debug("Executing promise {}", promise);
        ExecutionThread executionThread = threadPool.borrow();
        if (executionThread != null) {
            executionSettings.setName("PooledThreadedExecution");
            executionThread.addWatcher(this);
            executionThread.queuePromise(promise, executionSettings);
        } else {
            throw new NoExecutionThreadAvailableException();
        }
    }

    @Override
    public void onPromiseComplete(Promise<?> promise, ExecutionThread executionThread) {
        executionThread.removeWatcher(this);
        threadPool.returnToPool(executionThread);
        LOGGER.debug("Promised Completed, Execution Metrics: {}", executionThread.getMetrics());
    }

    @Override
    public void onPromiseRunning(Promise<?> promise, ExecutionThread executionThread) {
        LOGGER.debug("Promise started: {}", promise.getMetrics());
    }

    @Override
    public void onExecutionThreadRetirement(ExecutionThread executionThread) {
        executionThread.removeWatcher(this);
        LOGGER.debug(
                "ExecutionThread Retired, Execution Metrics: {}", executionThread.getMetrics());
    }
}
