package io.github.surajkumar.concurrency.threads;

import io.github.surajkumar.concurrency.metrics.ExecutionThreadMetrics;
import io.github.surajkumar.concurrency.promise.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ExecutionVirtualThread extends ExecutionThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionVirtualThread.class);
    private final List<ExecutionThreadWatcher> watchers = new CopyOnWriteArrayList<>();
    private final ExecutionThreadMetrics metrics = new ExecutionThreadMetrics();
    private final ExecutionPair executionPair;

    public ExecutionVirtualThread(ExecutionPair executionPair) {
        this.executionPair = executionPair;
    }

    @Override
    public void run() {
        LOGGER.trace(this + " running");
        Promise<?> promise = executionPair.getPromise();
        ExecutionSettings executionSettings = executionPair.getExecutionSettings();
        metrics.incrementTotalPromises();
        LOGGER.trace("Running promise {}", promise);
        notifyWatcherOfRunning(promise);
        if(executionSettings != null) {
            if(executionSettings.getInitialStartDelay() > 0 || executionSettings.getDelayBetween() > 0) {
                LOGGER.warn("Delays are not allowed within a ExecutionVirtualThread and will be ignored");
            }
            int repeat = executionSettings.getRepeat();
            while(repeat >= 0 || executionSettings.isRepeatIndefinitely()) {
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
        notifyWatcherOfRetirement();
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

    @Override
    public void addWatcher(ExecutionThreadWatcher watcher) {
        synchronized (watchers) {
            LOGGER.trace("{} registered watcher {}", this, watcher);
            watchers.add(watcher);
        }
    }

    @Override
    public void removeWatcher(ExecutionThreadWatcher watcher) {
        synchronized (watchers) {
            boolean removed = watchers.remove(watcher);
            if (removed) {
                LOGGER.trace("{} removed watcher {}", this, watcher);
            }
        }
    }
}
