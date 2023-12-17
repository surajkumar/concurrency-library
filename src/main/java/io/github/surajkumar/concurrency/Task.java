package io.github.surajkumar.concurrency;

/**
 * The Task interface represents a task that can be executed by a Promise.
 * It defines a single method called run() that returns a result of type T.
 *
 * @param <T> the type of result returned by the task
 */
public interface Task<T> {
    /**
     * The run() method executes the task and returns the result.
     *
     * @return the result of executing the task
     */
    T run();
}
