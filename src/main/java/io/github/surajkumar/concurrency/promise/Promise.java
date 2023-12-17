package io.github.surajkumar.concurrency.promise;

import io.github.surajkumar.concurrency.Task;
import io.github.surajkumar.concurrency.metrics.PromiseMetrics;

/**
 * The Promise class represents a promise that encapsulates a task and its result. It provides
 * methods to handle the result of the task and access execution metrics.
 *
 * @param <T> the type of result returned by the task
 */
public class Promise<T> {
    private final Task<T> task;
    private PromiseHandler<T> resultHandler;
    private PromiseHandler<Exception> exceptionHandler;
    private final PromiseMetrics metrics;
    private boolean finished;
    private T result;

    public Promise(Task<T> task) {
        this(task, null, null);
    }

    public Promise(
            Task<T> task,
            PromiseHandler<T> resultHandler,
            PromiseHandler<Exception> exceptionHandler) {
        this.task = task;
        this.resultHandler = resultHandler;
        this.exceptionHandler = exceptionHandler;
        this.metrics = new PromiseMetrics();
        this.finished = false;
    }

    /**
     * Sets the result handler for the Promise. This method assigns the provided result handler to
     * the instance variable {@code resultHandler} and returns the current Promise instance.
     *
     * @param result the handler for the result of the Promise
     * @return the current Promise instance
     * @param <T> the type of result handled by the Promise
     */
    public Promise<T> onResolve(PromiseHandler<T> result) {
        this.resultHandler = result;
        return this;
    }

    /**
     * Sets the exception handler for the Promise. This method assigns the provided exception
     * handler to the instance variable {@code exceptionHandler} and returns the current Promise
     * instance.
     *
     * @param exception the handler for handling exceptions in the Promise
     * @return the current Promise instance
     */
    public Promise<T> onReject(PromiseHandler<Exception> exception) {
        this.exceptionHandler = exception;
        return this;
    }

    /**
     * Retrieves the metrics of the Promise execution.
     *
     * @return The metrics of the Promise execution as an instance of PromiseMetrics.
     */
    public PromiseMetrics getMetrics() {
        return metrics;
    }

    /**
     * Completes the Promise execution.
     *
     * <p>This method clears the metrics, executes the task, handles the result or exception, and
     * updates the metrics accordingly. If a result handler is provided, it is called with the
     * result of the execution. If an exception handler is provided, it is called with any exception
     * encountered during the execution.
     *
     * <p>This method should ideally not be called manually as it will execute the promise on the
     * current thread. Instead you should use an Executor.
     */
    public void complete() {
        metrics.clear();
        try {
            long startMemory =
                    Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            result = task.run();
            finished = true;
            synchronized (this) {
                notify();
            }
            long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            metrics.setStart(System.nanoTime());
            metrics.setEnd(System.nanoTime());
            metrics.setSuccess(true);
            metrics.setMemoryUsage(endMemory - startMemory);
            if (resultHandler != null) {
                resultHandler.handle(result);
            }
        } catch (Exception ex) {
            metrics.setSuccess(false);
            metrics.setStackTrace(ex);
            metrics.setErrorDetails(ex.getMessage());
            if (exceptionHandler != null) {
                exceptionHandler.handle(ex);
            }
        }
    }

    /**
     * The get() method retrieves the result of the Promise. It first checks if the result has
     * already been set and returns it if it is not null. If the result is null, the method enters a
     * synchronized block and waits until another thread notifies it. Once notified, it returns the
     * result.
     *
     * @return The result of the Promise execution.
     */
    public T get() {
        if (result != null) {
            return result;
        }
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException ignore) {
            }
        }
        return result;
    }

    /**
     * Checks if the Promise execution is finished.
     *
     * @return true if the Promise execution is finished, false otherwise
     */
    public boolean isFinished() {
        return finished;
    }
}
