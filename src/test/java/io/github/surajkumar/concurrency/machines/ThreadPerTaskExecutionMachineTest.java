package io.github.surajkumar.concurrency.machines;

import static org.junit.jupiter.api.Assertions.*;

import io.github.surajkumar.concurrency.promise.Promise;
import io.github.surajkumar.concurrency.threads.ExecutionSettings;
import io.github.surajkumar.concurrency.utils.ThreadUtils;

import org.junit.jupiter.api.Test;

/**
 * Test for ThreadPerTaskExecutionMachine class's methods. This class is responsible for executing
 * tasks in separate threads. It includes a crucial method "execute" which accepts a promise and
 * execution settings as parameters.
 */
public class ThreadPerTaskExecutionMachineTest {

    @Test
    public void testExecution() {
        ThreadPerTaskExecutionMachine machine = new ThreadPerTaskExecutionMachine();
        ExecutionSettings settings =
                new ExecutionSettings()
                        .setName("Test")
                        .setDelayBetween(1000)
                        .setRepeat(1)
                        .setInitialStartDelay(0)
                        .setRepeatIndefinitely(false);
        Promise<Boolean> promise =
                new Promise<>(
                        () -> {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                fail(e.getMessage());
                            }
                            return true;
                        },
                        result -> {
                            // success handler
                            assertTrue(result, "Task not executed successfully");
                        },
                        exception -> {
                            // failure handler
                            fail(exception.getMessage());
                        });

        machine.execute(promise, settings);

        int count = 0;
        for (Thread thread : ThreadUtils.getThreads(Thread.currentThread().getThreadGroup())) {
            if (thread.getName().equals("ExecutionThread")) {
                count++;
            }
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertEquals(1, count);
        assertTrue(promise.isFinished(), "Promise did not finish successfully");
    }

    @Test
    public void testUsesMultipleThreads() {
        ThreadPerTaskExecutionMachine machine = new ThreadPerTaskExecutionMachine();
        ExecutionSettings settings =
                new ExecutionSettings()
                        .setName("Test")
                        .setDelayBetween(1000)
                        .setRepeat(1)
                        .setInitialStartDelay(0)
                        .setRepeatIndefinitely(false);
        Promise<Boolean> promise =
                new Promise<>(
                        () -> {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                fail(e.getMessage());
                            }
                            return true;
                        },
                        result -> {
                            // success handler
                            assertTrue(result, "Task not executed successfully");
                        },
                        exception -> {
                            // failure handler
                            fail(exception.getMessage());
                        });

        int numOfThreads = 5;
        for (int i = 0; i < numOfThreads; i++) {
            machine.execute(promise, settings);
        }

        int count = 0;
        for (Thread thread : ThreadUtils.getThreads(Thread.currentThread().getThreadGroup())) {
            if (thread.getName().equals("ExecutionThread")) {
                count++;
            }
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertEquals(numOfThreads, count);
        assertTrue(promise.isFinished(), "Promise did not finish successfully");
    }
}
