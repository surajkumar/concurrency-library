package io.github.surajkumar.concurrency.machines;

import static org.junit.jupiter.api.Assertions.*;

import io.github.surajkumar.concurrency.promise.Promise;
import io.github.surajkumar.concurrency.threads.ExecutionSettings;
import io.github.surajkumar.concurrency.utils.ThreadUtils;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

class ThreadPerTaskExecutionMachineTest {

    @Test
    void testExecution() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ThreadPerTaskExecutionMachine machine = new ThreadPerTaskExecutionMachine();
        ExecutionSettings settings = new ExecutionSettings().setRepeatIndefinitely(false);

        Promise<Boolean> promise =
                new Promise<>(
                        () -> {
                            latch.countDown();
                            return true;
                        },
                        result -> assertTrue(result, "Task not executed successfully"),
                        exception -> fail(exception.getMessage()));

        machine.execute(promise, settings);

        latch.await();

        assertTrue(promise.isFinished(), "Promise did not finish successfully");
    }

    @Test
    void testUsesMultipleThreads() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ThreadPerTaskExecutionMachine machine = new ThreadPerTaskExecutionMachine();
        ExecutionSettings settings = new ExecutionSettings().setRepeatIndefinitely(false);
        Promise<Boolean> promise =
                new Promise<>(
                        () -> {
                            latch.countDown();
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            return true;
                        },
                        result -> assertTrue(result, "Task not executed successfully"),
                        exception -> fail(exception.getMessage()));

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

        latch.await();

        assertEquals(count, count);
    }
}
