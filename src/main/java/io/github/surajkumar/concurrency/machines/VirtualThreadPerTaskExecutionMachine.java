package io.github.surajkumar.concurrency.machines;

import io.github.surajkumar.concurrency.metrics.PromiseMetrics;
import io.github.surajkumar.concurrency.pools.ThreadPool;
import io.github.surajkumar.concurrency.promise.Promise;
import io.github.surajkumar.concurrency.threads.ExecutionPair;
import io.github.surajkumar.concurrency.threads.ExecutionSettings;
import io.github.surajkumar.concurrency.threads.ExecutionThread;
import io.github.surajkumar.concurrency.threads.ExecutionVirtualThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The VirtualThreadPerTaskExecutionMachine class represents an execution machine that creates a
 * virtual thread for each task execution. It implements the ExecutionMachine interface and provides
 * methods to execute promises, manage thread pool, and handle promise events.
 */
public class VirtualThreadPerTaskExecutionMachine implements ExecutionMachine {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(VirtualThreadPerTaskExecutionMachine.class);

    /**
     * The VirtualThreadPerTaskExecutionMachine class represents an execution machine that creates a
     * virtual thread for each task execution. It implements the ExecutionMachine interface and
     * provides methods to execute promises, manage thread pool, and handle promise events.
     */
    public VirtualThreadPerTaskExecutionMachine() {}

    @Override
    public void execute(Promise<?> promise, ExecutionSettings executionSettings) {
        ExecutionVirtualThread executionThread =
                new ExecutionVirtualThread(new ExecutionPair(promise, executionSettings));
        executionThread.addWatcher(this);
        Thread.ofVirtual().start(executionThread);
    }

    @Override
    public ThreadPool threadPool() {
        return null;
    }

    @Override
    public void onPromiseComplete(Promise<?> promise, ExecutionThread executionThread) {
        executionThread.setRunning(false);
        LOGGER.debug("Promised Completed, Execution Metrics: {}", executionThread.getMetrics());
    }

    @Override
    public void onPromiseRunning(Promise<?> promise, ExecutionThread executionThread) {
        PromiseMetrics metrics = promise.getMetrics();
        LOGGER.debug("Promise started: {}", metrics.getStart());
        LOGGER.debug("Memory usage: {}", metrics.getMemoryUsage());
    }

    @Override
    public void onExecutionThreadRetirement(ExecutionThread executionThread) {
        LOGGER.debug(
                "ExecutionThread Retired, Execution Metrics: {}", executionThread.getMetrics());
    }
}
