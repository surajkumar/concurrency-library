package io.github.surajkumar.concurrency;

public interface Task<T> {
    T run() throws InterruptedException;
}
