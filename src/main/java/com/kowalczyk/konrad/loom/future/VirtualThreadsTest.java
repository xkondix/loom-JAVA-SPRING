package com.kowalczyk.konrad.loom.future;

import java.util.concurrent.Executors;

public class VirtualThreadsTest {
    public static void main(String[] args)  {

        Runnable action = () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        long start = System.currentTimeMillis();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < 1_000_000; i++) {
                executor.submit(action);
            }
        }

        long time = System.currentTimeMillis() - start;
        System.out.printf("%s took %.3f seconds%n", "Virtual Thread", time / 1000.0);
    }
}
