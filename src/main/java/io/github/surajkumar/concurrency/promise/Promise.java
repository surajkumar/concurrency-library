package io.github.surajkumar.concurrency.promise;

import io.github.surajkumar.concurrency.Task;
import io.github.surajkumar.concurrency.metrics.PromiseMetrics;

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

    public Promise<T> onResolve(PromiseHandler<T> result) {
        this.resultHandler = result;
        return this;
    }

    public Promise<T> onReject(PromiseHandler<Exception> exception) {
        this.exceptionHandler = exception;
        return this;
    }

    public PromiseMetrics getMetrics() {
        return metrics;
    }

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

    public boolean isFinished() {
        return finished;
    }
}
