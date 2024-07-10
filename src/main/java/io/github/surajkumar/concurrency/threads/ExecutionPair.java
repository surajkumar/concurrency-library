package io.github.surajkumar.concurrency.threads;

import io.github.surajkumar.concurrency.promise.Promise;

/**
 * The ExecutionPair class represents a pair of a Promise and its corresponding ExecutionSettings.
 * It provides methods to retrieve the Promise and ExecutionSettings.
 */
public record ExecutionPair(Promise<?> promise, ExecutionSettings executionSettings) {}
