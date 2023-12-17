package io.github.surajkumar.concurrency.machines;

import io.github.surajkumar.concurrency.exceptions.NoExecutionThreadAvailableException;
import io.github.surajkumar.concurrency.pools.FixedThreadPool;
import io.github.surajkumar.concurrency.pools.Pool;
import io.github.surajkumar.concurrency.pools.PoolOptions;
import io.github.surajkumar.concurrency.pools.ThreadPool;
import io.github.surajkumar.concurrency.promise.Promise;
import io.github.surajkumar.concurrency.threads.ExecutionSettings;
import io.github.surajkumar.concurrency.threads.ExecutionThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SingleThreadedExecutionMachine class represents an execution machine that executes promises using a single
 * thread. It implements the ExecutionMachine interface.
 */
public class SingleThreadedExecutionMachine implements ExecutionMachine {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(SingleThreadedExecutionMachine.class);
    private final ThreadPool threadPool;

    public SingleThreadedExecutionMachine() {
        PoolOptions options =
                new PoolOptions().setWaitFor(true).setEnableScaling(false).setMaxCapacity(1);
        threadPool = new FixedThreadPool(new Pool(1, options));
    }

    @Override
    public void execute(Promise<?> promise, ExecutionSettings executionSettings) {
        LOGGER.debug("Executing promise {}", promise);
        ExecutionThread executionThread = threadPool.borrow();
        if (executionThread != null) {
            if (executionSettings.getName() == null) {
                executionSettings.setName("SingleThreadedExecution");
            }
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
        LOGGER.warn("{} has retired, spawning a new instance", executionThread);
        if (threadPool.isShutdown()) {
            LOGGER.warn("ThreadPool has been shutdown so cannot spawn a new instance");
        } else {
            threadPool.returnToPool(
                    ExecutionThread.createStarted("SingleExecutionThreadRespawned"));
        }
    }
}
