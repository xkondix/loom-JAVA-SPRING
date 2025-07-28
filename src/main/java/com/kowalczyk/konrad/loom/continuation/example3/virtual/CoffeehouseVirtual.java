package com.kowalczyk.konrad.loom.continuation.example3.virtual;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static com.kowalczyk.konrad.loom.continuation.example3.CoffeehouseUtil.*;

public class CoffeehouseVirtual {
    private static final AtomicInteger customerCounter = new AtomicInteger(1);
    public static final VirtualScheduler SCHEDULER = new VirtualScheduler();
    public static final int CUSTOMER_COUNT = 100;

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(CUSTOMER_COUNT * 2);
        long startTime = System.nanoTime();

        for (int i = 0; i < CUSTOMER_COUNT; i++) {
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

    public static Runnable createCustomerThread(String coffeehouse, CountDownLatch latch) {
        return () -> {
            String customerID = "Client - " + customerCounter.getAndIncrement();
            enterCafe(coffeehouse, customerID);
            placeOrder(coffeehouse, customerID);
            waitForCoffee(getRandomCoffee().toString(), coffeehouse, customerID, randomMillis());
            drinkCoffee(coffeehouse, customerID);
            exitCafe(coffeehouse, customerID);
            latch.countDown();
        };
    }

    public static void waitForCoffee(String coffeeName, String coffeehouse, String customerID, int delayMillis) {
        System.out.println(customerID + " is waiting for a " + coffeeName + " at " + coffeehouse + "...");
        VirtualWaitingOperation.perform("Preparing " + coffeeName, delayMillis);
        System.out.println(customerID + "'s " + coffeeName + " is ready!");
    }

}
