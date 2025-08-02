package com.kowalczyk.konrad.loom.continuation.example3.virtual;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualScheduler {
    private final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    public void schedule(Runnable runnable) {
        executor.submit(runnable);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
