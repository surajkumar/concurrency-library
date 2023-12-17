package io.github.surajkumar.concurrency.promise;

public interface PromiseHandler<T> {
    void handle(T result);
}
