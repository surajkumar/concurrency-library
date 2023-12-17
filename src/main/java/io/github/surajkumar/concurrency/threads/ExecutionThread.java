package io.github.surajkumar.concurrency.threads;

import io.github.surajkumar.concurrency.exceptions.ExecutionThreadRetiredException;
import io.github.surajkumar.concurrency.metrics.ExecutionThreadMetrics;
import io.github.surajkumar.concurrency.promise.Promise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The ExecutionThread class represents a thread that executes promises. It implements the Runnable
 * interface and provides methods to manage the execution and lifecycle of the thread.
 */
public class ExecutionThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionThread.class);
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final BlockingQueue<ExecutionPair> queue = new LinkedBlockingQueue<>();
    private final List<ExecutionThreadWatcher> watchers = new CopyOnWriteArrayList<>();
    private final ExecutionThreadMetrics metrics = new ExecutionThreadMetrics();
    private Thread thread;

    /**
     * The ExecutionThread class represents a thread that is responsible for executing promises. It
     * runs until it is either interrupted or the running flag is set to false. It retrieves
     * ExecutionPair objects from the queue, executes the associated Promise, and updates the
     * execution metrics accordingly.
     */
    public ExecutionThread() {}

    /**
     * This method represents the execution logic of the ExecutionThread. It runs the thread until
     * it is interrupted or the running flag is set to false. It retrieves ExecutionPair objects
     * from the queue, executes the associated Promise, and updates the execution metrics
     * accordingly.
     */
    @Override
    public void run() {
        LOGGER.trace(this + " running");
        while (running.get() && !thread.isInterrupted()) {
            ExecutionPair executionPair;
            try {
                executionPair = queue.take();
            } catch (InterruptedException e) {
                LOGGER.trace("Thread was interrupted");
                running.set(false);
                return;
            }
            Promise<?> promise = executionPair.getPromise();
            ExecutionSettings executionSettings = executionPair.getExecutionSettings();
            metrics.incrementTotalPromises();
            LOGGER.trace("Running promise {}", promise);
            notifyWatcherOfRunning(promise);
            if (executionSettings != null) {
                sleep(executionSettings.getInitialStartDelay());
                int repeat = executionSettings.getRepeat();
                while (isRunning() && (repeat >= 0 || executionSettings.isRepeatIndefinitely())) {
                    sleep(executionSettings.getDelayBetween());
                    promise.complete();
                    if (!executionSettings.isRepeatIndefinitely()) {
                        repeat--;
                    }
                }
            } else {
                promise.complete();
            }
            notifyWatcherOfComplete(promise);
            if (promise.getMetrics().isSuccess()) {
                metrics.incrementCompletedPromises();
            } else {
                metrics.incrementFailedPromises();
            }
        }
        notifyWatcherOfRetirement();
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignore) {
            // Ignore
        }
    }

    private void notifyWatcherOfComplete(Promise<?> promise) {
        synchronized (watchers) {
            for (ExecutionThreadWatcher watcher : watchers) {
                watcher.onPromiseComplete(promise, this);
            }
        }
    }

    private void notifyWatcherOfRunning(Promise<?> promise) {
        synchronized (watchers) {
            for (ExecutionThreadWatcher watcher : watchers) {
                watcher.onPromiseRunning(promise, this);
            }
        }
    }

    private void notifyWatcherOfRetirement() {
        synchronized (watchers) {
            for (ExecutionThreadWatcher watcher : watchers) {
                watcher.onExecutionThreadRetirement(this);
            }
        }
    }

    /**
     * Adds a {@link ExecutionThreadWatcher} to the list of registered watchers. If the execution
     * thread is not running, it throws an {@link ExecutionThreadRetiredException}.
     *
     * @param watcher the {@link ExecutionThreadWatcher} to add
     * @throws ExecutionThreadRetiredException if the execution thread is not running
     */
    public void addWatcher(ExecutionThreadWatcher watcher) {
        if (!isRunning()) {
            throw new ExecutionThreadRetiredException();
        }
        synchronized (watchers) {
            LOGGER.trace("{} registered watcher {}", this, watcher);
            watchers.add(watcher);
        }
    }

    /**
     * Removes a {@link ExecutionThreadWatcher} from the list of registered watchers.
     *
     * @param watcher the {@link ExecutionThreadWatcher} to remove
     */
    public void removeWatcher(ExecutionThreadWatcher watcher) {
        synchronized (watchers) {
            boolean removed = watchers.remove(watcher);
            if (removed) {
                LOGGER.trace("{} removed watcher {}", this, watcher);
            }
        }
    }

    /**
     * Adds a promise to the execution queue with the specified execution settings. If the execution
     * thread is not running, it throws an ExecutionThreadRetiredException.
     *
     * @param promise the promise to add to the queue
     * @param executionSettings the execution settings for the promise
     * @throws ExecutionThreadRetiredException if the execution thread is not running
     */
    public void queuePromise(Promise<?> promise, ExecutionSettings executionSettings) {
        if (!isRunning()) {
            throw new ExecutionThreadRetiredException();
        }
        queue.add(new ExecutionPair(promise, executionSettings));
    }

    /**
     * Retrieves the thread associated with this ExecutionThread.
     *
     * @return the thread associated with this ExecutionThread
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * Returns the current running state of the execution thread.
     *
     * @return true if the execution thread is running, false otherwise.
     */
    public boolean isRunning() {
        return running.get();
    }

    /**
     * Sets the running state of the ExecutionThread.
     *
     * @param running the running state to set
     */
    public void setRunning(boolean running) {
        this.running.set(running);
    }

    /**
     * Retrieves the metrics of the execution thread.
     *
     * @return The execution metrics as an ExecutionThreadMetrics object.
     */
    public ExecutionThreadMetrics getMetrics() {
        return metrics;
    }

    /**
     * Creates and starts an ExecutionThread with the default name "ExecutionThread".
     *
     * @return the created ExecutionThread
     */
    public static ExecutionThread createStarted() {
        return createStarted("ExecutionThread");
    }

    /**
     * This method creates and starts an ExecutionThread with the given name.
     *
     * @param name the name of the ExecutionThread
     * @return the created ExecutionThread
     */
    public static ExecutionThread createStarted(String name) {
        ExecutionThread executionThread = new ExecutionThread();
        executionThread.setRunning(true);
        executionThread.thread = new Thread(executionThread);
        executionThread.thread.setName(name);
        executionThread.thread.start();
        return executionThread;
    }

    @Override
    public String toString() {
        return thread.getName();
    }
}
