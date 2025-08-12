package com.kowalczyk.konrad.loom.future;


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class CompletableFutureTest {
    public static void main(String[] args) {

        Runnable action = () -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        long start = System.currentTimeMillis();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            CompletableFuture<?>[] futures = IntStream.range(0, 1_000_000)
                    .mapToObj(_ -> CompletableFuture.runAsync(action, executor))
                    .toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(futures).join();

        }

        long time = System.currentTimeMillis() - start;
        System.out.printf("%s took %.3f seconds%n", "CompletableFuture", time / 1000.0);
    }
}


