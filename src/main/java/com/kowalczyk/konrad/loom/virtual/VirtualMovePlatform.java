package com.kowalczyk.konrad.loom.virtual;

import java.util.concurrent.Executors;

public class VirtualMovePlatform {

    public static void main(String[] args) { //VM option -Djdk.virtualThreadScheduler.parallelism=2

        Runnable task1 = () -> {
            System.out.println("Task 1 start on - " + Thread.currentThread());
            try {
                System.out.println("Task 1 sleep 2s");
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Task 1 restart on - " + Thread.currentThread());
        };

        Runnable task2 = () -> {
            System.out.println("Task 2 start on - " + Thread.currentThread());
            try {
                System.out.println("Task 2 sleep 3s");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Task 2 restart on - " + Thread.currentThread());
        };

        Runnable task3 = () -> {
            System.out.println("Task 3 start on - " + Thread.currentThread());
            System.out.println("Task 3 end on - " + Thread.currentThread());
        };


        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(task1);
            executor.submit(task2);
            executor.submit(task3);
        }
    }
}
