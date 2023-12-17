package io.github.surajkumar.concurrency.pools;

/** The PoolOptions class represents the options configuration for a pool of execution threads. */
public class PoolOptions {
    private int maxCapacity;
    private boolean enableScaling;
    private int scaleUpAmount;
    private int scaleDownAmount;
    private boolean waitFor;

    public boolean isEnableScaling() {
        return enableScaling;
    }

    /**
     * Enables or disables scaling for the pool of execution threads.
     *
     * @param enableScaling true to enable scaling, false to disable scaling
     * @return the PoolOptions instance
     */
    public PoolOptions setEnableScaling(boolean enableScaling) {
        this.enableScaling = enableScaling;
        return this;
    }

    /**
     * Retrieves the scale up amount for the pool of execution threads.
     *
     * @return the scale up amount
     */
    public int getScaleUpAmount() {
        return scaleUpAmount;
    }

    /**
     * Sets the scale up amount for the pool of execution threads.
     *
     * @param scaleUpAmount the scale up amount to set
     * @return the PoolOptions instance
     */
    public PoolOptions setScaleUpAmount(int scaleUpAmount) {
        this.scaleUpAmount = scaleUpAmount;
        return this;
    }

    /**
     * Retrieves the scale down amount for the pool of execution threads.
     *
     * @return the scale down amount
     */
    public int getScaleDownAmount() {
        return scaleDownAmount;
    }

    /**
     * Sets the scale down amount for the pool of execution threads.
     *
     * @param scaleDownAmount the scale down amount to set
     * @return the PoolOptions instance
     */
    public PoolOptions setScaleDownAmount(int scaleDownAmount) {
        this.scaleDownAmount = scaleDownAmount;
        return this;
    }

    /**
     * Retrieves the maximum capacity of the pool.
     *
     * @return the maximum capacity of the pool
     */
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Sets the maximum capacity of the pool.
     *
     * @param maxCapacity the maximum capacity to set
     * @return the PoolOptions instance
     */
    public PoolOptions setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        return this;
    }

    /**
     * Retrieves the value of the waitFor option in the PoolOptions. This option determines whether
     * the borrow() method should wait for an ExecutionThread to be available or immediately return
     * null if none are available.
     *
     * @return true if the borrow() method should wait for an ExecutionThread, false otherwise
     */
    public boolean isWaitFor() {
        return waitFor;
    }

    /**
     * Retrieves the value of the {@code waitFor} option in the PoolOptions. This option determines
     * whether the {@code borrow()} method should wait for an ExecutionThread to be available or
     * immediately return {@code null} if none are available.
     *
     * @param waitFor the value to set for the {@code waitFor} option
     * @return the {@code PoolOptions} instance
     */
    public PoolOptions setWaitFor(boolean waitFor) {
        this.waitFor = waitFor;
        return this;
    }
}
