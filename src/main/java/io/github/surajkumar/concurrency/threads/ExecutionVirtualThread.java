package io.github.surajkumar.concurrency.threads;

import io.github.surajkumar.concurrency.metrics.ExecutionThreadMetrics;
import io.github.surajkumar.concurrency.promise.Promise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The ExecutionVirtualThread class represents a virtual thread that executes promises. It extends
 * the ExecutionThread class and provides methods to manage the execution and lifecycle of the
 * virtual thread.
 */
public class ExecutionVirtualThread extends ExecutionThread {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionVirtualThread.class);
    private final List<ExecutionThreadWatcher> watchers = new CopyOnWriteArrayList<>();
    private final ExecutionThreadMetrics metrics = new ExecutionThreadMetrics();
    private final ExecutionPair executionPair;

    /**
     * The ExecutionVirtualThread class represents a virtual thread that executes an ExecutionPair.
     * It provides methods for adding and removing ExecutionThreadWatcher, as well as notifying
     * watchers about the progress and completion of execution.
     *
     * @param executionPair The execution pair containing the promise and execution settings.
     */
    public ExecutionVirtualThread(ExecutionPair executionPair) {
        this.executionPair = executionPair;
    }

    /**
     * Executes the virtual thread. This method completes the promise associated with the execution
     * pair. If there are execution settings defined, it handles the settings like initial start
     * delay, delay between iterations, and repetition count. If there are no execution settings
     * defined, it completes the promise once. It notifies the watchers about the progress and
     * completion of execution. It also increments the metrics for total promises, completed
     * promises, and failed promises.
     */
    @Override
    public void run() {
        LOGGER.trace(this + " running");
        Promise<?> promise = executionPair.getPromise();
        ExecutionSettings executionSettings = executionPair.getExecutionSettings();
        metrics.incrementTotalPromises();
        LOGGER.trace("Running promise {}", promise);
        notifyWatcherOfRunning(promise);
        if (executionSettings != null) {
            if (executionSettings.getInitialStartDelay() > 0
                    || executionSettings.getDelayBetween() > 0) {
                LOGGER.warn(
                        "Delays are not allowed within a ExecutionVirtualThread and will be"
                                + " ignored");
            }
            int repeat = executionSettings.getRepeat();
            while (repeat >= 0 || executionSettings.isRepeatIndefinitely()) {
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

    /**
     * Adds a {@link ExecutionThreadWatcher} to the list of registered watchers.
     *
     * @param watcher the {@link ExecutionThreadWatcher} to add
     */
    @Override
    public void addWatcher(ExecutionThreadWatcher watcher) {
        synchronized (watchers) {
            LOGGER.trace("{} registered watcher {}", this, watcher);
            watchers.add(watcher);
        }
    }

    /**
     * Removes the specified {@link ExecutionThreadWatcher} from the list of registered watchers.
     *
     * @param watcher the {@link ExecutionThreadWatcher} to remove
     */
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
