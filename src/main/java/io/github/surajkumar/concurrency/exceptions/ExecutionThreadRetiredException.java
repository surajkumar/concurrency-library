package io.github.surajkumar.concurrency.exceptions;

public class ExecutionThreadRetiredException extends RuntimeException {

    public ExecutionThreadRetiredException() {
        super("ExecutionThread has retired");
    }
}
