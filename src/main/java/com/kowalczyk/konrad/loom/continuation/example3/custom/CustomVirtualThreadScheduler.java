package com.kowalczyk.konrad.loom.continuation.example3.custom;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomVirtualThreadScheduler {
    public static ScopedValue<CustomVirtualThread> CURRENT_VIRTUAL_THREAD = ScopedValue.newInstance();
    private volatile boolean running = true;
    private final Queue<CustomVirtualThread> queue = new ConcurrentLinkedQueue<>();
    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    public void start() {
        while (running) {
            if (!queue.isEmpty()) {
                CustomVirtualThread remove = queue.remove();
                executor.submit(() -> ScopedValue.where(CURRENT_VIRTUAL_THREAD, remove)
                        .run(remove::run));
            }
        }
    }

    public void schedule(CustomVirtualThread virtualThread) {
        queue.add(virtualThread);
    }

    public void shutdown() {
        running = false;
        executor.shutdown();
    }
}
