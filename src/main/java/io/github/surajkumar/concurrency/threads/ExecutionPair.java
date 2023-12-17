package io.github.surajkumar.concurrency.threads;

import io.github.surajkumar.concurrency.promise.Promise;

public class ExecutionPair {
    private final Promise<?> promise;
    private final ExecutionSettings executionSettings;

    public ExecutionPair(Promise<?> promise, ExecutionSettings executionSettings) {
        this.promise = promise;
        this.executionSettings = executionSettings;
    }

    public Promise<?> getPromise() {
        return promise;
    }

    public ExecutionSettings getExecutionSettings() {
        return executionSettings;
    }
}
