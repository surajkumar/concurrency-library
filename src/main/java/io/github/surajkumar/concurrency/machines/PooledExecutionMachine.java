package io.github.surajkumar.concurrency.machines;

import io.github.surajkumar.concurrency.exceptions.NoExecutionThreadAvailableException;
import io.github.surajkumar.concurrency.threads.ExecutionSettings;
import io.github.surajkumar.concurrency.threads.ExecutionThread;
import io.github.surajkumar.concurrency.promise.Promise;
import io.github.surajkumar.concurrency.pools.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PooledExecutionMachine implements ExecutionMachine {
    private static final Logger LOGGER = LoggerFactory.getLogger(PooledExecutionMachine.class);
    private final ThreadPool threadPool;

    public PooledExecutionMachine(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    public void execute(Promise<?> promise, ExecutionSettings executionSettings) {
        LOGGER.debug("Executing promise {}", promise);
        ExecutionThread executionThread = threadPool.borrow();
        if(executionThread != null) {
            executionSettings.setName("PooledThreadedExecution");
            executionThread.addWatcher(this);
            executionThread.queuePromise(promise, executionSettings);
        } else {
            throw new NoExecutionThreadAvailableException();
        }
    }

    @Override
    public ThreadPool getThreadPool() {
        return threadPool;
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
        LOGGER.debug("ExecutionThread Retired, Execution Metrics: {}", executionThread.getMetrics());
    }
}
