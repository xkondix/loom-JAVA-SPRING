package com.kowalczyk.konrad.loom.virtual.pinning;

import java.util.concurrent.Executors;

public class PinningSynchronized {

    /**
     * <a href="https://openjdk.org/jeps/491">...</a>
     * VM options -Djdk.tracePinnedThreads=full -Djdk.virtualThreadScheduler.parallelism=1
     */
    private static final Object lock = new Object();

    public static void main(String[] args) {
        Runnable task = () -> {
            synchronized (lock) {
                System.out.println("Start: " + Thread.currentThread());
                try {
                    Thread.sleep(3000); // Pinning, because synchronized (lock)
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("End: " + Thread.currentThread());
            }
        };

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(task);
            executor.submit(task);
        }
    }
}
