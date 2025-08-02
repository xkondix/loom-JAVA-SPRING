package com.kowalczyk.konrad.loom.continuation.example3;

import com.kowalczyk.konrad.loom.continuation.example3.custom.CustomWaitingOperation;

import java.util.Random;

public class CoffeehouseUtil {
    private static final Random RANDOM = new Random();

    public static CoffeeType getRandomCoffee() {
        CoffeeType[] values = CoffeeType.values();
        return values[RANDOM.nextInt(values.length)];
    }

    public static void enterCafe(String coffeehouse, String customerID) {
        System.out.println(customerID + " enters " + coffeehouse + ".");
    }

    public static void placeOrder(String coffeehouse, String customerID) {
        System.out.println(customerID + " places an order at " + coffeehouse + ".");
    }


    public static void drinkCoffee(String coffeehouse, String customerID) {
        System.out.println(customerID + " starts drinking the coffee at " + coffeehouse + ".");
    }

    public static void exitCafe(String coffeehouse, String customerID) {
        System.out.println(customerID + " leaves " + coffeehouse + ".");
    }

    public static int randomMillis() {
        return 1 + new Random().nextInt(6);
    }
}
