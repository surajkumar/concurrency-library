package io.github.surajkumar.concurrency.threads;

import io.github.surajkumar.concurrency.promise.Promise;

/**
 * The ExecutionThreadWatcher interface provides callbacks for monitoring the execution of promises
 * in an ExecutionThread.
 */
public interface ExecutionThreadWatcher {
    /**
     * Executes the provided callback method when a promise is completed.
     *
     * @param promise the promise that has completed
     * @param executionThread the execution thread in which the promise was executed
     */
    void onPromiseComplete(Promise<?> promise, ExecutionThread executionThread);

    /**
     * Executes the provided callback method when a promise is running.
     *
     * @param promise the promise that is currently running
     * @param executionThread the execution thread in which the promise is being executed
     */
    void onPromiseRunning(Promise<?> promise, ExecutionThread executionThread);

    /**
     * Notifies the registered watchers that the execution thread is retiring.
     *
     * @param executionThread the execution thread that is retiring
     */
    void onExecutionThreadRetirement(ExecutionThread executionThread);
}
