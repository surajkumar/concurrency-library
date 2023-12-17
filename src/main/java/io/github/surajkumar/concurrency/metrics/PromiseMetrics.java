package io.github.surajkumar.concurrency.metrics;

import java.util.Arrays;

/**
 * The PromiseMetrics class represents the metrics of a Promise execution.
 * It provides methods to access and manipulate various metrics such as start time, end time,
 * success status, execution time, memory usage, error details, and stack trace.
 */
public class PromiseMetrics {
    private long start;
    private long end;
    private boolean success;
    private long memoryUsage;
    private String errorDetails;
    private StackTraceElement[] stackTrace;

    public PromiseMetrics() {}

    /**
     * Clears the metrics of a Promise execution. This method resets the start time,
     * end time, success status, memory usage, error details, and stack trace.
     */
    public void clear() {
        start = 0;
        end = 0;
        success = false;
        memoryUsage = 0;
        errorDetails = null;
        stackTrace = null;
    }

    /**
     * Retrieves the start time of the Promise execution.
     *
     * @return The start time of the Promise execution
     */
    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    /**
     * Retrieves the end time of the Promise execution.
     *
     * @return The end time of the Promise execution
     */
    protected long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    /**
     * Determines if the Promise execution is successful.
     *
     * @return true if the Promise execution is successful, false otherwise
     */
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Retrieves the execution time of the Promise execution.
     *
     * @return The execution time of the Promise execution in nanoseconds.
     *         Returns -1 if the start time or end time is not set.
     */
    public long getExecutionTime() {
        if (start > 0 && end > 0) {
            return end - start;
        }
        return -1;
    }

    /**
     * Retrieves the memory usage of the Promise execution.
     *
     * @return The memory usage of the Promise execution in bytes.
     */
    public long getMemoryUsage() {
        return memoryUsage;
    }

    /**
     * Sets the memory usage of the Promise execution.
     *
     * @param memoryUsage The memory usage of the Promise execution in bytes.
     */
    public void setMemoryUsage(long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    /**
     * Retrieves the error details of the Promise execution. If the Promise execution is successful, an empty string is returned.
     *
     * @return The error details of the Promise execution. Returns an empty string if the Promise execution is successful.
     */
    public String getErrorDetails() {
        if (isSuccess()) {
            return "";
        }
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    /**
     * Retrieves the stack trace of the Promise execution.
     *
     * @return The stack trace of the Promise execution as an array of StackTraceElement objects.
     *         Returns an empty array if the Promise execution is successful.
     */
    public StackTraceElement[] getStackTrace() {
        if (isSuccess()) {
            return new StackTraceElement[0];
        }
        return stackTrace;
    }

    public void setStackTrace(Throwable throwable) {
        this.stackTrace = throwable.getStackTrace();
    }

    @Override
    public String toString() {
        return "PromiseMetrics{"
                + "start="
                + start
                + ", end="
                + end
                + ", success="
                + success
                + ", executionTime="
                + getExecutionTime()
                + ", memoryUsage="
                + memoryUsage
                + ", errorDetails='"
                + getErrorDetails()
                + '\''
                + ", stackTrace="
                + Arrays.toString(getStackTrace())
                + '}';
    }
}
