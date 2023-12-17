package io.github.surajkumar.concurrency.exceptions;

public class NoExecutionThreadAvailableException extends RuntimeException {
    public NoExecutionThreadAvailableException() {
        super("No ExecutionThread available in pool");
    }
}
