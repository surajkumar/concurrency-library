package io.github.surajkumar.concurrency.metrics;

import java.util.Arrays;

public class PromiseMetrics {
    private long start;
    private long end;
    private boolean success;
    private long memoryUsage;
    private String errorDetails;
    private StackTraceElement[] stackTrace;

    public PromiseMetrics() {}

    public void clear() {
        start = 0;
        end = 0;
        success = false;
        memoryUsage = 0;
        errorDetails = null;
        stackTrace = null;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    protected long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getExecutionTime() {
        if(start > 0 && end > 0) {
            return end - start;
        }
        return -1;
    }

    public long getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(long memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public String getErrorDetails() {
        if(isSuccess()) {
            return "";
        }
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public StackTraceElement[] getStackTrace() {
        if(isSuccess()) {
            return new StackTraceElement[0];
        }
        return stackTrace;
    }

    public void setStackTrace(Throwable throwable) {
        this.stackTrace = throwable.getStackTrace();
    }

    @Override
    public String toString() {
        return "PromiseMetrics{" +
                "start=" + start +
                ", end=" + end +
                ", success=" + success +
                ", executionTime=" + getExecutionTime() +
                ", memoryUsage=" + memoryUsage +
                ", errorDetails='" + getErrorDetails() + '\'' +
                ", stackTrace=" + Arrays.toString(getStackTrace()) +
                '}';
    }

}
