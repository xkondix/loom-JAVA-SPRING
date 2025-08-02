package com.kowalczyk.konrad.loom.virtual.pinning;

import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class PinningReentrantLock {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Runnable task = () -> {
            synchronized (lock) {
                System.out.println("Start: " + Thread.currentThread());
                try {
                    Thread.sleep(3000);
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
