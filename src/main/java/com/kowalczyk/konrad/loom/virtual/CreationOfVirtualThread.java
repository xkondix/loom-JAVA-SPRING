package com.kowalczyk.konrad.loom.virtual;


import java.time.ZonedDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class CreationOfVirtualThread {

    /**
     * <a href="https://openjdk.org/jeps/444">...</a>
     */
    public static void main(String[] args) {

        // Way 1
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(() -> System.out.println(Thread.currentThread() + " | time: " + ZonedDateTime.now()));
            executor.submit(() -> System.out.println(Thread.currentThread() + " | time: " + ZonedDateTime.now()));
            executor.submit(TASK);
            executor.submit(() -> System.out.println(Thread.currentThread() + " | time: " + ZonedDateTime.now()));
            executor.submit(() -> System.out.println(Thread.currentThread() + " | time: " + ZonedDateTime.now()));
        }

        // Way 2
        Thread.ofVirtual().start(() -> System.out.println("\n" + Thread.currentThread() + " | time: " + ZonedDateTime.now()));

        long start = System.nanoTime();
        int threadCount = 10_000_000;
        ConcurrentHashMap.KeySetView<Integer, Boolean> results = ConcurrentHashMap.newKeySet();

        Thread[] threads = IntStream.range(0, threadCount)
                .mapToObj(i -> Thread.ofVirtual().unstarted(() -> results.add(i)))
                .toArray(Thread[]::new);

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted", e);
            }
        }

        long end = System.nanoTime();
        System.out.printf("\nCompleted %d virtual threads in %.3f seconds%n", threadCount, (end - start) / 1_000_000_000.0);


    }

    private static final Callable<String> TASK = () -> {
        String val = "callable";
        System.out.println(Thread.currentThread() + " | time: " + ZonedDateTime.now() + " -> type: " + val);
        return val;
    };
}
