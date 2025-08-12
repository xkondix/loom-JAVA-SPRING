package com.kowalczyk.konrad.loom.continuation.example3.custom;

import java.util.concurrent.CountDownLatch;

import static com.kowalczyk.konrad.loom.continuation.example3.CoffeehouseUtil.*;

public class CoffeehouseCustom {

    /**
     * VM options --enable-preview --add-exports java.base/jdk.internal.vm=ALL-UNNAMED
     */
    public static final CustomVirtualThreadScheduler SCHEDULER = new CustomVirtualThreadScheduler();
    public static final int CUSTOMER_COUNT = 100;

    public static void main(String[] args) {
        new Thread(SCHEDULER::start).start();
        long startTime = System.nanoTime();
        CountDownLatch latch = new CountDownLatch(CUSTOMER_COUNT * 2);

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
        System.out.println("Threads completed. Execution time: " + durationMillis + " ms");
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
