package io.github.surajkumar.concurrency.pools;

import io.github.surajkumar.concurrency.Executor;
import io.github.surajkumar.concurrency.exceptions.ExecutionMachineShutdownException;
import io.github.surajkumar.concurrency.machines.SingleThreadedExecutionMachine;
import io.github.surajkumar.concurrency.metrics.ThreadPoolMetrics;
import io.github.surajkumar.concurrency.promise.Promise;
import io.github.surajkumar.concurrency.threads.ExecutionSettings;
import io.github.surajkumar.concurrency.threads.ExecutionThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * DynamicThreadPool represents a dynamic thread pool that can be used to manage and borrow ExecutionThreads.
 * The pool dynamically scales up or down based on the demand and the specified pool options.
 */
public class DynamicThreadPool implements ThreadPool {
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicThreadPool.class);
    private static final int DEFAULT_INITIAL_CAPACITY = 2;
    private static final int DEFAULT_SCALE =
            Math.max(Runtime.getRuntime().availableProcessors() / 2, 1);
    private final Pool pool;
    private final ThreadPoolMetrics threadPoolMetrics = new ThreadPoolMetrics();
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Executor autoScalingExecutor = new Executor(new SingleThreadedExecutionMachine());

    /**
     * A dynamic thread pool that allows for automatic scaling of the number of threads based on the workload.
     */
    public DynamicThreadPool() {
        this(
                DEFAULT_INITIAL_CAPACITY,
                new PoolOptions()
                        .setEnableScaling(true)
                        .setScaleUpAmount(DEFAULT_SCALE)
                        .setScaleDownAmount(DEFAULT_SCALE)
                        .setWaitFor(true));
    }

    /**
     * A dynamic thread pool that allows for automatic scaling of the number of threads based on the workload.
     *
     * @param initialCapacity the initial capacity of the thread pool
     */
    public DynamicThreadPool(int initialCapacity) {
        this(
                initialCapacity,
                new PoolOptions()
                        .setEnableScaling(true)
                        .setScaleUpAmount(DEFAULT_SCALE)
                        .setScaleDownAmount(DEFAULT_SCALE));
    }

    /**
     * Constructs a new DynamicThreadPool with the specified initial capacity and pool options.
     *
     * @param initialCapacity the initial capacity of the thread pool
     * @param poolOptions the pool options for the thread pool
     */
    public DynamicThreadPool(int initialCapacity, PoolOptions poolOptions) {
        this.pool = new Pool(initialCapacity, poolOptions);

        for (int i = 0; i < initialCapacity; i++) {
            pool.add(ExecutionThread.createStarted("DynamicThread" + i));
        }

        threadPoolMetrics.setInitialCapacity(initialCapacity);
        threadPoolMetrics.setActiveThreads(0);

        if (poolOptions.isEnableScaling()) {
            startAutoScalingTask();
        }
    }

    @Override
    public ExecutionThread borrow() {
        if (!running.get()) {
            throw new ExecutionMachineShutdownException();
        }
        if (!pool.hasAvailable()) {
            pool.scaleUp();
        }
        threadPoolMetrics.setAvailableThreads(threadPoolMetrics.getAvailableThreads() - 1);
        threadPoolMetrics.setActiveThreads(threadPoolMetrics.getActiveThreads() + 1);
        if (pool.getPoolOptions().isWaitFor()) {
            return pool.take();
        } else {
            return pool.get();
        }
    }

    @Override
    public void returnToPool(ExecutionThread executionThread) {
        if (!running.get()) {
            LOGGER.warn(
                    "ExecutionMachine has been shutdown but received a returnToPool request."
                            + " Retiring {}",
                    executionThread);
            executionThread.setRunning(false);
            return;
        }
        LOGGER.trace("Returning {} to pool", executionThread);
        pool.add(executionThread);
        threadPoolMetrics.setAvailableThreads(pool.getSize());
        threadPoolMetrics.setActiveThreads(threadPoolMetrics.getActiveThreads() - 1);
    }

    @Override
    public void shutdown() {
        LOGGER.trace("Shutdown initiated");
        running.set(false);
        while (!pool.isEmpty()) {
            ExecutionThread executionThread = pool.get();
            executionThread.setRunning(false);
            executionThread.getThread().interrupt();
            LOGGER.trace("Shutdown " + executionThread);
        }
        pool.getLoaned()
                .forEach(
                        t -> {
                            t.setRunning(false);
                        });
        pool.getLoaned().clear();
    }

    @Override
    public boolean isShutdown() {
        return !running.get();
    }

    @Override
    public ThreadPoolMetrics getMetrics() {
        return threadPoolMetrics;
    }

    /**
     * Starts the auto-scaling task in the DynamicThreadPool.
     */
    private void startAutoScalingTask() {
        ExecutionSettings settings =
                new ExecutionSettings().setDelayBetween(1000).setRepeatIndefinitely(true);
        autoScalingExecutor.run(
                settings,
                new Promise<>(
                        () -> {
                            if (isShutdown()) {
                                autoScalingExecutor.shutdown();
                                return null;
                            }
                            int size = pool.getSize();
                            if (size > (pool.getInitialCapacity() + 2)) {
                                pool.scaleDown();
                            }
                            return pool.getSize();
                        }));
    }
}
