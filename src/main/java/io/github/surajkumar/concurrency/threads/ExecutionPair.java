package io.github.surajkumar.concurrency.threads;

import io.github.surajkumar.concurrency.promise.Promise;

/**
 * The ExecutionPair class represents a pair of a Promise and its corresponding ExecutionSettings.
 * It provides methods to retrieve the Promise and ExecutionSettings.
 */
public class ExecutionPair {
    private final Promise<?> promise;
    private final ExecutionSettings executionSettings;

    public ExecutionPair(Promise<?> promise, ExecutionSettings executionSettings) {
        this.promise = promise;
        this.executionSettings = executionSettings;
    }

    /**
     * Retrieves the Promise associated with this ExecutionPair.
     *
     * @return the Promise associated with this ExecutionPair
     */
    public Promise<?> getPromise() {
        return promise;
    }

    /**
     * Retrieves the ExecutionSettings associated with this ExecutionPair.
     *
     * @return the ExecutionSettings associated with this ExecutionPair
     */
    public ExecutionSettings getExecutionSettings() {
        return executionSettings;
    }
}
