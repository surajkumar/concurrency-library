package io.github.surajkumar.concurrency.exceptions;

/**
 * The ExecutionThreadRetiredException class represents an exception that is thrown when the
 * execution thread is retired. It is a subclass of RuntimeException.
 */
public class ExecutionThreadRetiredException extends RuntimeException {

    /**
     * The ExecutionThreadRetiredException class represents an exception that is thrown when the
     * execution thread is retired. It is a subclass of RuntimeException.
     */
    public ExecutionThreadRetiredException() {
        super("ExecutionThread has retired");
    }
}
