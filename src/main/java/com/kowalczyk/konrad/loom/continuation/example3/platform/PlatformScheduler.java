package com.kowalczyk.konrad.loom.continuation.example3.platform;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlatformScheduler {
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public void schedule(Runnable runnable) {
        executor.submit(runnable);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
