package io.github.surajkumar.concurrency.promise;

/**
 * The PromiseHandler interface represents a handler for the result of a Promise.
 * It defines a single method called handle() that takes in a result of type T.
 *
 * @param <T> the type of result to be handled
 */
public interface PromiseHandler<T> {
    /**
     * The handle() method is a callback method that is called when the result of a Promise is available.
     * It takes in a result of type T and performs handling operations on it.
     *
     * @param result the result of a Promise execution
     */
    void handle(T result);
}
