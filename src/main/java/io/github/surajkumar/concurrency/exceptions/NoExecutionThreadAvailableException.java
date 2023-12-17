package io.github.surajkumar.concurrency.exceptions;

/**
 * NoExecutionThreadAvailableException is a custom exception that extends RuntimeException.
 * It is thrown when there is no ExecutionThread available in the pool.
 */
public class NoExecutionThreadAvailableException extends RuntimeException {
    public NoExecutionThreadAvailableException() {
        super("No ExecutionThread available in pool");
    }
}
