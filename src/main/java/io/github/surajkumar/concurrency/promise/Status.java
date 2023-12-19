package io.github.surajkumar.concurrency.promise;

/**
 * The Status enum represents the different statuses that a Promise can have.
 */
public enum Status {
    /**
     * Represents the status of a promise that has not been started yet.
     */
    NOT_STARTED,
    /**
     * Represents the status of a promise that has successfully completed its execution.
     */
    FINISHED,
    /**
     * The RUNNING constant represents the status of a promise that is currently running.
     */
    RUNNING,
    /**
     * The ERROR constant represents the status of a promise that encountered an error while running.
     */
    ERROR
}
