package com.kowalczyk.konrad.loom.continuation;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

public class ContinuationExample2 {
    private static final ContinuationScope SCOPE = new ContinuationScope("shopping");

    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 3; i++) {
            final String customer = "customer-" + i;
            ContinuationScope scope = new ContinuationScope(customer);
            Continuation cont = new Continuation(scope, () -> {
                enterStore(scope, customer);
                exitStore(customer);
            });
            cont.run();
            pickProducts(customer);
            payForItems(cont, customer);
        }
    }

    public static void enterStore(ContinuationScope scope, String customer) {
        System.out.printf("enterStore - %s\n", customer);
        Continuation.yield(scope);
    }

    public static void pickProducts(String customer) {
        System.out.printf("pickProducts - %s\n", customer);
    }

    public static void payForItems(Continuation cont, String customer) throws InterruptedException {
        System.out.println("paying ...");
        Thread.sleep(1000);
        cont.run();
    }

    public static void exitStore(String customer) {
        System.out.printf("exitStore - %s\n", customer);

    }

}
