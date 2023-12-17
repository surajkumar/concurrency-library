package io.github.surajkumar.concurrency.threads;

/**
 * The ExecutionSettings class represents the settings for executing a task. It specifies the
 * initial start delay of the task, the delay between successive executions, the number of times the
 * task should be repeated, and whether the task should be repeated indefinitely.
 */
public class ExecutionSettings {
    private String name;
    private int initialStartDelay;
    private int delayBetween;
    private int repeat;
    private boolean repeatIndefinitely;

    public ExecutionSettings() {
        initialStartDelay = 0;
        delayBetween = 0;
        repeat = 0;
        repeatIndefinitely = false;
    }

    /**
     * Retrieves the delay between successive executions.
     *
     * @return the delay between successive executions
     */
    public int getDelayBetween() {
        return delayBetween;
    }

    /**
     * Sets the delay between successive executions.
     *
     * @param delayBetween the delay between successive executions
     * @return the modified ExecutionSettings instance
     */
    public ExecutionSettings setDelayBetween(int delayBetween) {
        this.delayBetween = delayBetween;
        return this;
    }

    /**
     * Retrieves the number of times the task should be repeated.
     *
     * @return the number of times the task should be repeated
     */
    public int getRepeat() {
        return repeat;
    }

    /**
     * Sets the number of times the task should be repeated.
     *
     * @param repeat the number of times the task should be repeated
     * @return the modified ExecutionSettings instance
     */
    public ExecutionSettings setRepeat(int repeat) {
        this.repeat = repeat;
        return this;
    }

    /**
     * Retrieves the initial start delay of the ExecutionSettings.
     *
     * @return the initial start delay of the ExecutionSettings
     */
    public int getInitialStartDelay() {
        return initialStartDelay;
    }

    /**
     * Sets the initial start delay of the ExecutionSettings.
     *
     * @param initialStartDelay the initial start delay to be set
     * @return the modified ExecutionSettings instance
     */
    public ExecutionSettings setInitialStartDelay(int initialStartDelay) {
        this.initialStartDelay = initialStartDelay;
        return this;
    }

    /**
     * Sets whether the task should be repeated indefinitely.
     *
     * @param repeatIndefinitely true if the task should be repeated indefinitely, false otherwise
     * @return the modified ExecutionSettings instance
     */
    public ExecutionSettings setRepeatIndefinitely(boolean repeatIndefinitely) {
        this.repeatIndefinitely = repeatIndefinitely;
        return this;
    }

    /**
     * Retrieves whether the task should be repeated indefinitely.
     *
     * @return true if the task should be repeated indefinitely, false otherwise
     */
    public boolean isRepeatIndefinitely() {
        return repeatIndefinitely;
    }

    /**
     * Retrieves the name of the ExecutionThread.
     *
     * @return the name of the ExecutionThread
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the ExecutionThread.
     *
     * @param name the name to set
     * @return the modified ExecutionSettings instance
     */
    public ExecutionSettings setName(String name) {
        this.name = name;
        return this;
    }
}
