package io.github.surajkumar.concurrency.pools;

import io.github.surajkumar.concurrency.threads.ExecutionThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Pool {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pool.class);
    private final BlockingQueue<ExecutionThread> queue = new LinkedBlockingQueue<>();
    private final BlockingQueue<ExecutionThread> loaned = new LinkedBlockingQueue<>();
    private final int initialCapacity;
    private int currentCapacity;
    private final PoolOptions poolOptions;

    public Pool(int initialCapacity, PoolOptions poolOptions) {
        this.initialCapacity = initialCapacity;
        this.currentCapacity = initialCapacity;
        this.poolOptions = poolOptions;
    }

    public boolean hasAvailable() {
        return queue.peek() != null;
    }

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

    public ExecutionThread get() {
        ExecutionThread executionThread = queue.poll();
        if(executionThread != null) {
            loaned.add(executionThread);
        }
        return executionThread;
    }

    public void add(ExecutionThread executionThread) {
        if(executionThread != null) {
            queue.add(executionThread);
            loaned.remove(executionThread);
        }
    }

    public void remove(ExecutionThread executionThread) {
        if(executionThread != null) {
            queue.remove(executionThread);
            loaned.remove(executionThread);
        }
    }

    public void scaleUp() {
        if(!poolOptions.isEnableScaling()) {
            return;
        }
        if(currentCapacity == poolOptions.getMaxCapacity()) {
            LOGGER.trace("Pool is at max capacity");
            return;
        }

        int scale = Math.min(poolOptions.getMaxCapacity() - currentCapacity, poolOptions.getScaleUpAmount());

        for(int i = 0; i < scale; i++) {
            ExecutionThread thread = new ExecutionThread();
            thread.setRunning(true);
            add(thread);
        }
        currentCapacity += scale;
    }

    public void scaleDown() {
        if(!poolOptions.isEnableScaling()) {
            return;
        }
        int scale = Math.max(0, currentCapacity - poolOptions.getScaleDownAmount());
        for(int i = 0; i < scale; i++) {
            if(queue.peek() != null) {
                ExecutionThread thread = queue.poll();
                thread.setRunning(false);
                remove(thread);
            }
        }
        currentCapacity = Math.max(0, currentCapacity - scale);
    }

    public Queue<ExecutionThread> getLoaned() {
        return loaned;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public PoolOptions getPoolOptions() {
        return poolOptions;
    }

    public int getSize() {
        return queue.size();
    }

    public int getInitialCapacity() {
        return initialCapacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }
}
