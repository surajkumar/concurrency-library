package io.github.surajkumar.concurrency.machines;

import static org.junit.jupiter.api.Assertions.*;

import io.github.surajkumar.concurrency.promise.Promise;
import io.github.surajkumar.concurrency.threads.ExecutionSettings;

import org.junit.jupiter.api.Test;

public class SingleThreadedExecutionMachineTest {

    @Test
    void testExecuteWithPromiseAndSettings() {
        SingleThreadedExecutionMachine executionMachine = new SingleThreadedExecutionMachine();
        Promise<String> promise = new Promise<>(() -> "Hello, World!");
        ExecutionSettings executionSettings = new ExecutionSettings();
        assertDoesNotThrow(() -> executionMachine.execute(promise, executionSettings));
        assertFalse(promise.isFinished());
    }

    @Test
    void testExecuteWithNullSettings() {
        SingleThreadedExecutionMachine executionMachine = new SingleThreadedExecutionMachine();
        Promise<String> promise = new Promise<>(() -> "Hello, World!");
        assertThrows(NullPointerException.class, () -> executionMachine.execute(promise, null));
    }
}
