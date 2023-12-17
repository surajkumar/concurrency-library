package io.github.surajkumar.concurrency.exceptions;

public class ExecutionMachineShutdownException extends RuntimeException {

    public ExecutionMachineShutdownException() {
        super("ExecutionMachine is shutdown");
    }
}
