package io.github.surajkumar.concurrency.threads;

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

    public int getDelayBetween() {
        return delayBetween;
    }

    public ExecutionSettings setDelayBetween(int delayBetween) {
        this.delayBetween = delayBetween;
        return this;
    }

    public int getRepeat() {
        return repeat;
    }

    public ExecutionSettings setRepeat(int repeat) {
        this.repeat = repeat;
        return this;
    }

    public int getInitialStartDelay() {
        return initialStartDelay;
    }

    public ExecutionSettings setInitialStartDelay(int initialStartDelay) {
        this.initialStartDelay = initialStartDelay;
        return this;
    }

    public ExecutionSettings setRepeatIndefinitely(boolean repeatIndefinitely) {
        this.repeatIndefinitely = repeatIndefinitely;
        return this;
    }

    public boolean isRepeatIndefinitely() {
        return repeatIndefinitely;
    }

    public String getName() {
        return name;
    }

    public ExecutionSettings setName(String name) {
        this.name = name;
        return this;
    }
}
