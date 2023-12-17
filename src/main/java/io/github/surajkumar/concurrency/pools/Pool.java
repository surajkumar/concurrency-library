package io.github.surajkumar.concurrency.pools;

import io.github.surajkumar.concurrency.threads.ExecutionThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/** Pool represents a thread pool that manages ExecutionThreads for executing promises. */
public class Pool {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pool.class);
    private final BlockingQueue<ExecutionThread> queue = new LinkedBlockingQueue<>();
    private final BlockingQueue<ExecutionThread> loaned = new LinkedBlockingQueue<>();
    private final int initialCapacity;
    private int currentCapacity;
    private final PoolOptions poolOptions;

    /**
     * The Pool class represents a pool of execution threads.
     *
     * @param initialCapacity The initial size of the pool
     * @param poolOptions The options to configure this pool
     */
    public Pool(int initialCapacity, PoolOptions poolOptions) {
        this.initialCapacity = initialCapacity;
        this.currentCapacity = initialCapacity;
        this.poolOptions = poolOptions;
    }

    /**
     * Checks if there are available items in the queue.
     *
     * @return true if there are available items in the queue, false otherwise.
     */
    public boolean hasAvailable() {
        return queue.peek() != null;
    }

    /**
     * Retrieves an ExecutionThread from the queue. If no thread is currently available in the
     * queue, the method waits until a thread becomes available.
     *
     * @return the retrieved ExecutionThread or null if the method is interrupted while waiting for
     *     a thread
     */
    public ExecutionThread take() {
        try {
            ExecutionThread executionThread = queue.take();
            loaned.add(executionThread);
            return executionThread;
        } catch (InterruptedException e) {
            LOGGER.error("{} interrupted", this.getClass().getName(), e);
            Thread.currentThread().interrupt();
            return null;
        }
    }

    /**
     * Retrieves an ExecutionThread from the queue. If no thread is currently available in the
     * queue, the method waits until a thread becomes available.
     *
     * @return the retrieved ExecutionThread or null if the method is interrupted while waiting for
     *     a thread
     */
    public ExecutionThread get() {
        ExecutionThread executionThread = queue.poll();
        if (executionThread != null) {
            loaned.add(executionThread);
        }
        return executionThread;
    }

    /**
     * Adds an ExecutionThread to the pool.
     *
     * @param executionThread the ExecutionThread to be added
     */
    public void add(ExecutionThread executionThread) {
        if (executionThread != null) {
            queue.add(executionThread);
            loaned.remove(executionThread);
        }
    }

    /**
     * Removes the specified ExecutionThread from the pool.
     *
     * @param executionThread the ExecutionThread to be removed
     */
    public void remove(ExecutionThread executionThread) {
        if (executionThread != null) {
            queue.remove(executionThread);
            loaned.remove(executionThread);
        }
    }

    /**
     * Increases the capacity of the pool by creating and adding new ExecutionThread instances.
     * However, if scaling is disabled in the pool options or the pool is already at maximum
     * capacity, the method does nothing.
     */
    public void scaleUp() {
        if (!poolOptions.isEnableScaling()) {
            return;
        }
        if (currentCapacity >= poolOptions.getMaxCapacity()) {
            LOGGER.trace("Pool is at max capacity");
            return;
        }

        int scale = poolOptions.getScaleUpAmount();
        if (scale + currentCapacity > poolOptions.getMaxCapacity()
                && poolOptions.getMaxCapacity() > 0) {
            scale =
                    Math.min(
                            poolOptions.getMaxCapacity() - currentCapacity,
                            poolOptions.getScaleUpAmount());
        }

        for (int i = 0; i < scale; i++) {
            ExecutionThread thread = ExecutionThread.createStarted();
            add(thread);
        }
        currentCapacity += scale;
    }

    /**
     * Scales down the pool by removing ExecutionThreads from the queue. If scaling is disabled or
     * the scale down amount is greater than the current capacity, the method does nothing.
     */
    public void scaleDown() {
        if (!poolOptions.isEnableScaling()) {
            return;
        }
        int scale = Math.max(0, currentCapacity - poolOptions.getScaleDownAmount());
        for (int i = 0; i < scale; i++) {
            if (queue.peek() != null) {
                ExecutionThread thread = queue.poll();
                thread.setRunning(false);
                remove(thread);
            }
        }
        currentCapacity = Math.max(0, currentCapacity - scale);
    }

    /**
     * Retrieves the loaned ExecutionThreads from the pool.
     *
     * @return a Queue of ExecutionThreads that are currently loaned out from the pool
     */
    public Queue<ExecutionThread> getLoaned() {
        return loaned;
    }

    /**
     * Checks if there are available items in the queue.
     *
     * @return true if there are available items in the queue, false otherwise.
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Retrieves the PoolOptions object associated with the Pool.
     *
     * @return the PoolOptions object
     */
    public PoolOptions getPoolOptions() {
        return poolOptions;
    }

    /**
     * Retrieves the size of the queue.
     *
     * @return the size of the queue
     */
    public int getSize() {
        return queue.size();
    }

    /**
     * Retrieves the initial capacity of the pool.
     *
     * @return the initial capacity of the pool
     */
    public int getInitialCapacity() {
        return initialCapacity;
    }

    /**
     * Retrieves the current capacity of the pool.
     *
     * @return the current capacity of the pool
     */
    public int getCurrentCapacity() {
        return currentCapacity;
    }

    /**
     * Sets the current capacity of the pool.
     *
     * @param currentCapacity the new current capacity for the pool
     */
    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }
}
