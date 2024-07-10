package io.github.surajkumar.concurrency.machines;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.surajkumar.concurrency.exceptions.NoExecutionThreadAvailableException;
import io.github.surajkumar.concurrency.pools.ThreadPool;
import io.github.surajkumar.concurrency.promise.Promise;
import io.github.surajkumar.concurrency.threads.ExecutionSettings;
import io.github.surajkumar.concurrency.threads.ExecutionThread;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PooledExecutionMachineTest {

    @Test
    void execute_WhenExecutionThreadIsAvailable_ShouldQueuePromiseForExecution() {
        ThreadPool mockThreadPool = Mockito.mock(ThreadPool.class);
        Promise<?> mockPromise = Mockito.mock(Promise.class);
        ExecutionSettings executionSettings = new ExecutionSettings();
        ExecutionThread mockExecutionThread = Mockito.mock(ExecutionThread.class);

        when(mockThreadPool.borrow()).thenReturn(mockExecutionThread);
        PooledExecutionMachine pooledExecutionMachine = new PooledExecutionMachine(mockThreadPool);

        pooledExecutionMachine.execute(mockPromise, executionSettings);

        verify(mockExecutionThread, times(1)).queuePromise(mockPromise, executionSettings);
        verify(mockExecutionThread, times(1)).addWatcher(pooledExecutionMachine);
    }

    @Test
    void execute_WhenNoExecutionThreadIsAvailable_ShouldThrowNoExecutionThreadAvailableException() {
        ThreadPool mockThreadPool = Mockito.mock(ThreadPool.class);
        Promise<?> mockPromise = Mockito.mock(Promise.class);
        ExecutionSettings executionSettings = new ExecutionSettings();

        when(mockThreadPool.borrow()).thenReturn(null);
        PooledExecutionMachine pooledExecutionMachine = new PooledExecutionMachine(mockThreadPool);

        try {
            pooledExecutionMachine.execute(mockPromise, executionSettings);
        } catch (Exception e) {
            assert e instanceof NoExecutionThreadAvailableException;
        }
    }

    @Test
    void testOnPromiseCompleteInStandardCase() {
        ThreadPool threadPoolMock = Mockito.mock(ThreadPool.class);
        PooledExecutionMachine pooledMachine = new PooledExecutionMachine(threadPoolMock);
        Promise<?> promiseMock = Mockito.mock(Promise.class);
        ExecutionThread executionThreadMock = Mockito.mock(ExecutionThread.class);

        pooledMachine.onPromiseComplete(promiseMock, executionThreadMock);

        Mockito.verify(executionThreadMock, times(1)).removeWatcher(pooledMachine);
        Mockito.verify(threadPoolMock, times(1)).returnToPool(executionThreadMock);
    }

    @Test
    void testOnPromiseCompleteWithNullThread() {
        ThreadPool threadPoolMock = Mockito.mock(ThreadPool.class);
        PooledExecutionMachine pooledMachine = new PooledExecutionMachine(threadPoolMock);
        Promise<?> promiseMock = Mockito.mock(Promise.class);

        assertThrows(
                NullPointerException.class,
                () -> pooledMachine.onPromiseComplete(promiseMock, null));
    }
}
