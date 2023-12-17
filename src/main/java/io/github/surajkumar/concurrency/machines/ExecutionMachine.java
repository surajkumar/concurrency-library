package io.github.surajkumar.concurrency.machines;

import io.github.surajkumar.concurrency.pools.ThreadPool;
import io.github.surajkumar.concurrency.promise.Promise;
import io.github.surajkumar.concurrency.threads.ExecutionSettings;
import io.github.surajkumar.concurrency.threads.ExecutionThreadWatcher;

public interface ExecutionMachine extends ExecutionThreadWatcher {
    void execute(Promise<?> promise, ExecutionSettings executionSettings);

    ThreadPool getThreadPool();
}
