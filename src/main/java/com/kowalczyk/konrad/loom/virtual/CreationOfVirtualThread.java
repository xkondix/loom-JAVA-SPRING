package com.kowalczyk.konrad.loom.virtual;


import java.time.ZonedDateTime;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

//https://openjdk.org/jeps/444
public class CreationOfVirtualThread {

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
        Thread.ofVirtual().start(() -> System.out.println(Thread.currentThread() + " | time: " + ZonedDateTime.now()));

    }

    private static final Callable<String> TASK = () -> {
        String val = "callable";
        System.out.println(Thread.currentThread() + " | time: " + ZonedDateTime.now() + " -> type: " + val);
        return val;
    };
}
