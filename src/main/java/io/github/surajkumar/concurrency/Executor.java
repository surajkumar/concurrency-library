package io.github.surajkumar.concurrency;

import io.github.surajkumar.concurrency.exceptions.ExecutionMachineShutdownException;
import io.github.surajkumar.concurrency.machines.ExecutionMachine;
import io.github.surajkumar.concurrency.machines.SingleThreadedExecutionMachine;
import io.github.surajkumar.concurrency.pools.ThreadPool;
import io.github.surajkumar.concurrency.promise.Promise;
import io.github.surajkumar.concurrency.threads.ExecutionSettings;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Executor class is responsible for executing promises asynchronously using an
 * ExecutionMachine. It provides various methods for running promises and managing their execution.
 */
public class Executor {
    private static final Logger LOGGER = LogManager.getLogger(Executor.class);
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final ExecutionMachine executionMachine;

    /**
     * Creates an Executor with the provided ExecutionMachine.
     *
     * @param executionMachine the ExecutionMachine used to execute promises ExecutionMachine
     *     interface represents a machine that executes promises using different strategies. It
     *     provides methods to execute promises, get the thread pool, and implements the
     *     ExecutionThreadWatcher interface for monitoring promise execution.
     */
    public Executor(ExecutionMachine executionMachine) {
        this.executionMachine = executionMachine;
    }

    /** Executes the given promises in parallel. */
    public Executor() {
        this(new SingleThreadedExecutionMachine());
    }

    /**
     * Executes the given promises in parallel and waits for all of them to complete.
     *
     * @param promises the promises to join
     */
    public void join(Promise<?>... promises) {
        run(promises);
        for (Promise<?> p : promises) {
            p.get();
        }
    }

    /**
     * Executes the given promises in parallel.
     *
     * @param promises the promises to execute
     */
    public void run(Promise<?>... promises) {
        run(new ExecutionSettings(), promises);
    }

    /**
     * Executes the given promises using the provided execution settings.
     *
     * @param executionSettings the execution settings to apply
     * @param promises the promises to execute
     * @throws ExecutionMachineShutdownException if the execution machine is shutdown
     */
    public void run(ExecutionSettings executionSettings, Promise<?>... promises) {
        if (!running.get()) {
            throw new ExecutionMachineShutdownException();
        }
        if (promises == null || promises.length == 0) {
            LOGGER.warn("No promises provided to run.");
            return;
        }
        for (Promise<?> p : promises) {
            LOGGER.trace("Sending {} to {}", p, executionMachine);
            executionMachine.execute(p, executionSettings);
        }
    }

    /**
     * Shuts down the Executor's thread pool, if it exists. If the shutdown method on the thread
     * pool has already been called, calling this method has no effect.
     */
    public void shutdown() {
        ThreadPool threadPool = executionMachine.threadPool();
        if (threadPool != null) {
            executionMachine.threadPool().shutdown();
        }
    }
}
