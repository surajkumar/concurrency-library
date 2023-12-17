package io.github.surajkumar.concurrency.pools;

import io.github.surajkumar.concurrency.exceptions.ExecutionMachineShutdownException;
import io.github.surajkumar.concurrency.threads.ExecutionThread;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicThreadPoolTest {
    @Test
    void testBorrowAndReturn() {
        DynamicThreadPool threadPool = new DynamicThreadPool(2);

        ExecutionThread firstThread = threadPool.borrow();
        ExecutionThread secondThread = threadPool.borrow();

        assertEquals(0, threadPool.getMetrics().getAvailableThreads());
        assertEquals(2, threadPool.getMetrics().getActiveThreads());

        threadPool.returnToPool(firstThread);
        threadPool.returnToPool(secondThread);


        assertEquals(2, threadPool.getMetrics().getAvailableThreads());
        assertEquals(0, threadPool.getMetrics().getActiveThreads());
    }

    @Test
    void testBorrowAfterShutdown() {
        DynamicThreadPool threadPool = new DynamicThreadPool();
        threadPool.shutdown();
        assertThrows(ExecutionMachineShutdownException.class, threadPool::borrow);
    }

    @Test
    void testReturnToPoolAfterShutdown() {
        DynamicThreadPool threadPool = new DynamicThreadPool(1);
        ExecutionThread thread = threadPool.borrow();
        threadPool.shutdown();
        threadPool.returnToPool(thread);
        assertEquals(0, threadPool.getMetrics().getAvailableThreads());
        assertEquals(0, threadPool.getMetrics().getActiveThreads());
    }

    @Test
    void testPoolScaling() throws InterruptedException {
        DynamicThreadPool threadPool = new DynamicThreadPool(2, new PoolOptions()
                .setMaxCapacity(Integer.MAX_VALUE)
                .setEnableScaling(true)
                .setScaleUpAmount(2)
                .setScaleDownAmount(2));

        ExecutionThread firstThread = threadPool.borrow();
        ExecutionThread secondThread = threadPool.borrow();
        ExecutionThread thirdThread = threadPool.borrow();

        // Scaling should kick on the third borrow, so it goes up by 2 but then available is 1 because
        // we just borrowed

        assertEquals(1, threadPool.getMetrics().getAvailableThreads());
        assertEquals(3, threadPool.getMetrics().getActiveThreads());

        threadPool.returnToPool(thirdThread);
        assertEquals(2, threadPool.getMetrics().getAvailableThreads());

        threadPool.shutdown();
        assertTrue(threadPool.isShutdown());
    }

    @Test
    void testConstructorWithInitialCapacityAndPoolOptions() {
        PoolOptions poolOptions = new PoolOptions()
            .setEnableScaling(false);

        DynamicThreadPool threadPool = new DynamicThreadPool(2, poolOptions);

        ExecutionThread firstThread = threadPool.borrow();
        ExecutionThread secondThread = threadPool.borrow();
        assertEquals(0, threadPool.getMetrics().getAvailableThreads());
        assertEquals(2, threadPool.getMetrics().getActiveThreads());
        threadPool.returnToPool(firstThread);
        ExecutionThread thirdThread = threadPool.borrow();

        // pool should now be empty as scaling is disabled
        assertEquals(0, threadPool.getMetrics().getAvailableThreads());
        assertEquals(2, threadPool.getMetrics().getActiveThreads());
    }
}