package com.kowalczyk.konrad.loom.continuation.example3.custom;

import java.util.concurrent.CountDownLatch;

import static com.kowalczyk.konrad.loom.continuation.example3.CoffeehouseUtil.*;

public class CoffeehouseCustom {
    public static final CustomVirtualThreadScheduler SCHEDULER = new CustomVirtualThreadScheduler();

    public static void main(String[] args) {
        new Thread(SCHEDULER::start).start();
        int customerCount = 100;
        long startTime = System.nanoTime();
        CountDownLatch latch = new CountDownLatch(customerCount);

        for (int i = 0; i < customerCount; i++) {
            SCHEDULER.schedule(createCustomerThread("Coffeehouse Katowice", latch));
            SCHEDULER.schedule(createCustomerThread("Coffeehouse Amsterdam", latch));
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("All customers finished. Shutting down scheduler.");
        SCHEDULER.shutdown();

        long endTime = System.nanoTime();
        long durationMillis = (endTime - startTime) / 1_000_000;
        System.out.println("Wątki zakończone. Czas wykonania: " + durationMillis + " ms");
    }

    public static CustomVirtualThread createCustomerThread(String coffeehouse, CountDownLatch latch) {
        return new CustomVirtualThread(thread -> {
            String customerID = "Client - " + thread.getId();
            enterCafe(coffeehouse, customerID);
            placeOrder(coffeehouse, customerID);
            waitForCoffee(getRandomCoffee().toString(), coffeehouse, customerID, randomMillis());
            drinkCoffee(coffeehouse, customerID);
            exitCafe(coffeehouse, customerID);
            latch.countDown();
        });
    }

    public static void waitForCoffee(String coffeeName, String coffeehouse, String customerID, int delayMillis) {
        System.out.println(customerID + " is waiting for a " + coffeeName + " at " + coffeehouse + "...");
        CustomWaitingOperation.perform("Preparing " + coffeeName, delayMillis);
        System.out.println(customerID + "'s " + coffeeName + " is ready!");
    }
}
