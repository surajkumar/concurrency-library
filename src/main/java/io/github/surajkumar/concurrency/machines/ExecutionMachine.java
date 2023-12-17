package io.github.surajkumar.concurrency.machines;

import io.github.surajkumar.concurrency.pools.ThreadPool;
import io.github.surajkumar.concurrency.promise.Promise;
import io.github.surajkumar.concurrency.threads.ExecutionSettings;
import io.github.surajkumar.concurrency.threads.ExecutionThreadWatcher;

/**
 * The ExecutionMachine interface represents a machine that executes promises using different strategies.
 * It provides methods to execute promises, get the thread pool, and implements the ExecutionThreadWatcher interface
 * for monitoring promise execution.
 */
public interface ExecutionMachine extends ExecutionThreadWatcher {
    /**
     * Executes the given Promise using the provided ExecutionSettings.
     *
     * @param promise           the Promise to execute
     * @param executionSettings the ExecutionSettings to apply
     */
    void execute(Promise<?> promise, ExecutionSettings executionSettings);

    /**
     * Retrieves the thread pool associated with this ExecutionMachine.
     *
     * @return the thread pool
     */
    ThreadPool getThreadPool();
}
