package io.github.surajkumar.concurrency;

import io.github.surajkumar.concurrency.machines.PooledExecutionMachine;
import io.github.surajkumar.concurrency.machines.SingleThreadedExecutionMachine;
import io.github.surajkumar.concurrency.machines.ThreadPerTaskExecutionMachine;
import io.github.surajkumar.concurrency.machines.VirtualThreadPerTaskExecutionMachine;
import io.github.surajkumar.concurrency.pools.DynamicThreadPool;

/**
 * The GlobalExecutor class provides access to different types of Executors for executing promises.
 */
public class GlobalExecutor {
    private static Executor singleThread;
    private static Executor virtual;
    private static Executor newThread;
    private static Executor pooled;

    /**
     * The GlobalExecutor class provides access to different types of Executors for executing
     * promises.
     */
    public GlobalExecutor() {}

    /**
     * Retrieves an Executor that executes promises in a single thread.
     *
     * @return Returns the single thread Executor.
     */
    public static Executor getSingleThreadExecutor() {
        if (singleThread == null) {
            singleThread = new Executor(new SingleThreadedExecutionMachine());
        }
        return singleThread;
    }

    /**
     * Retrieves an Executor that executes promises using a virtual thread per task execution model.
     *
     * @return Returns the virtual thread per task Executor.
     */
    public static Executor getVirtualExecutor() {
        if (virtual == null) {
            virtual = new Executor(new VirtualThreadPerTaskExecutionMachine());
        }
        return virtual;
    }

    /**
     * Retrieves an Executor that executes promises in a new thread per task execution model.
     *
     * @return Returns the new thread per task Executor.
     */
    public static Executor getNewThreadExecutor() {
        if (newThread == null) {
            newThread = new Executor(new ThreadPerTaskExecutionMachine());
        }
        return newThread;
    }

    /**
     * Retrieves an Executor that executes promises using a dynamic thread pool for execution. The
     * dynamic thread pool can automatically scale up or down based on demand.
     *
     * @return Returns the dynamic thread pool Executor.
     */
    public static Executor getPooledExecutor() {
        if (pooled == null) {
            pooled = new Executor(new PooledExecutionMachine(new DynamicThreadPool(1)));
        }
        return pooled;
    }
}
