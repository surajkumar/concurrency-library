package io.github.surajkumar.concurrency;

import io.github.surajkumar.concurrency.machines.PooledExecutionMachine;
import io.github.surajkumar.concurrency.machines.SingleThreadedExecutionMachine;
import io.github.surajkumar.concurrency.machines.ThreadPerTaskExecutionMachine;
import io.github.surajkumar.concurrency.machines.VirtualThreadPerTaskExecutionMachine;
import io.github.surajkumar.concurrency.pools.DynamicThreadPool;

public class GlobalExecutor {
    private static Executor singleThread;
    private static Executor virtual;
    private static Executor newThread;
    private static Executor pooled;

    public static Executor getSingleThreadExecutor() {
        if (singleThread == null) {
            singleThread = new Executor(new SingleThreadedExecutionMachine());
        }
        return singleThread;
    }

    public static Executor getVirtualExecutor() {
        if (virtual == null) {
            virtual = new Executor(new VirtualThreadPerTaskExecutionMachine());
        }
        return virtual;
    }

    public static Executor getNewThreadExecutor() {
        if (newThread == null) {
            newThread = new Executor(new ThreadPerTaskExecutionMachine());
        }
        return newThread;
    }

    public static Executor getPooledExecutor() {
        if (pooled == null) {
            pooled = new Executor(new PooledExecutionMachine(new DynamicThreadPool(1)));
        }
        return pooled;
    }
}
