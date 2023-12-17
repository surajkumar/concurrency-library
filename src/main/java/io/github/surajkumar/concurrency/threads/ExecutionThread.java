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

public class ExecutionThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionThread.class);
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final BlockingQueue<ExecutionPair> queue = new LinkedBlockingQueue<>();
    private final List<ExecutionThreadWatcher> watchers = new CopyOnWriteArrayList<>();
    private final ExecutionThreadMetrics metrics = new ExecutionThreadMetrics();
    private Thread thread;

    @Override
    public void run() {
        LOGGER.trace(this + " running");
        while(running.get() && !thread.isInterrupted()) {
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
            if(executionSettings != null) {
                sleep(executionSettings.getInitialStartDelay());
                int repeat = executionSettings.getRepeat();
                while(isRunning() && (repeat >= 0 || executionSettings.isRepeatIndefinitely())) {
                    sleep(executionSettings.getDelayBetween());
                    promise.complete();
                    if(!executionSettings.isRepeatIndefinitely()) {
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

    public void addWatcher(ExecutionThreadWatcher watcher) {
        if(!isRunning()) {
            throw new ExecutionThreadRetiredException();
        }
        synchronized (watchers) {
            LOGGER.trace("{} registered watcher {}", this, watcher);
            watchers.add(watcher);
        }
    }

    public void removeWatcher(ExecutionThreadWatcher watcher) {
        synchronized (watchers) {
            boolean removed = watchers.remove(watcher);
            if (removed) {
                LOGGER.trace("{} removed watcher {}", this, watcher);
            }
        }
    }

    public void queuePromise(Promise<?> promise, ExecutionSettings executionSettings) {
        if(!isRunning()) {
            throw new ExecutionThreadRetiredException();
        }
        queue.add(new ExecutionPair(promise, executionSettings));
    }

    public Thread getThread() {
        return thread;
    }

    public boolean isRunning() {
        return running.get();
    }

    public void setRunning(boolean running) {
        this.running.set(running);
    }

    public ExecutionThreadMetrics getMetrics() {
        return metrics;
    }

    public static ExecutionThread createStarted() {
        return createStarted("ExecutionThread");
    }

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
