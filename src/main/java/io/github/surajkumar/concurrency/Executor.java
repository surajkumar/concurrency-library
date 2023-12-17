package io.github.surajkumar.concurrency;

import io.github.surajkumar.concurrency.exceptions.ExecutionMachineShutdownException;
import io.github.surajkumar.concurrency.machines.ExecutionMachine;
import io.github.surajkumar.concurrency.machines.SingleThreadedExecutionMachine;
import io.github.surajkumar.concurrency.pools.ThreadPool;
import io.github.surajkumar.concurrency.promise.Promise;
import io.github.surajkumar.concurrency.threads.ExecutionSettings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class Executor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Executor.class);
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final ExecutionMachine executionMachine;

    public Executor(ExecutionMachine executionMachine) {
        this.executionMachine = executionMachine;
    }

    public Executor() {
        this(new SingleThreadedExecutionMachine());
    }

    public void join(Promise<?>... promises) {
        run(promises);
        for (Promise<?> p : promises) {
            p.get();
        }
    }

    public void run(Promise<?>... promises) {
        run(new ExecutionSettings(), promises);
    }

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

    public void shutdown() {
        ThreadPool threadPool = executionMachine.getThreadPool();
        if (threadPool != null) {
            executionMachine.getThreadPool().shutdown();
        }
    }
}
