package io.github.surajkumar.concurrency.threads;

import io.github.surajkumar.concurrency.promise.Promise;

public interface ExecutionThreadWatcher {
    void onPromiseComplete(Promise<?> promise, ExecutionThread executionThread);
    void onPromiseRunning(Promise<?> promise, ExecutionThread executionThread);
    void onExecutionThreadRetirement(ExecutionThread executionThread);
}
