package io.github.surajkumar.concurrency.exceptions;

/**
 * The ExecutionMachineShutdownException class is a custom exception that is thrown when the
 * ExecutionMachine is shutdown. It extends the RuntimeException class, indicating that it is an
 * unchecked exception.
 */
public class ExecutionMachineShutdownException extends RuntimeException {

    public ExecutionMachineShutdownException() {
        super("ExecutionMachine is shutdown");
    }
}
