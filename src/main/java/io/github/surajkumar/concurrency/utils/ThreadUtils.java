package io.github.surajkumar.concurrency.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The ThreadUtils class provides utility methods for working with threads.
 *
 * This class allows retrieving all threads within a given thread group.
 */
public class ThreadUtils {

    /**
     * Retrieves all threads within a given thread group.
     *
     * @param group the thread group to retrieve threads from
     * @return a list of threads within the specified thread group
     */
    public static List<Thread> getThreads(ThreadGroup group) {
        List<Thread> threadList = new ArrayList<>();
        Stack<ThreadGroup> groupStack = new Stack<>();
        groupStack.push(group);
        while (!groupStack.isEmpty()) {
            ThreadGroup currentGroup = groupStack.pop();
            int threadCount = currentGroup.activeCount();
            Thread[] threads = new Thread[threadCount];
            currentGroup.enumerate(threads);
            for (Thread thread : threads) {
                if (thread != null) {
                    threadList.add(thread);
                }
            }
            int subgroupCount = currentGroup.activeGroupCount();
            ThreadGroup[] subgroups = new ThreadGroup[subgroupCount];
            currentGroup.enumerate(subgroups);
            for (ThreadGroup subgroup : subgroups) {
                groupStack.push(subgroup);
            }
        }
        return threadList;
    }
}
