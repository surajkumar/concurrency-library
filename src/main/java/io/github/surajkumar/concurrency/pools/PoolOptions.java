package io.github.surajkumar.concurrency.pools;

public class PoolOptions {
    private int maxCapacity;
    private boolean enableScaling;
    private int scaleUpAmount;
    private int scaleDownAmount;
    private boolean waitFor;

    public boolean isEnableScaling() {
        return enableScaling;
    }

    public PoolOptions setEnableScaling(boolean enableScaling) {
        this.enableScaling = enableScaling;
        return this;
    }

    public int getScaleUpAmount() {
        return scaleUpAmount;
    }

    public PoolOptions setScaleUpAmount(int scaleUpAmount) {
        this.scaleUpAmount = scaleUpAmount;
        return this;
    }

    public int getScaleDownAmount() {
        return scaleDownAmount;
    }

    public PoolOptions setScaleDownAmount(int scaleDownAmount) {
        this.scaleDownAmount = scaleDownAmount;
        return this;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public PoolOptions setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        return this;
    }

    public boolean isWaitFor() {
        return waitFor;
    }

    public PoolOptions setWaitFor(boolean waitFor) {
        this.waitFor = waitFor;
        return this;
    }
}
