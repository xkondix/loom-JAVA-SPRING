package com.kowalczyk.konrad.loom.tool;

import java.util.NoSuchElementException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;

public class StructuredWithScopedValueExample {

    /**
     * <a href="https://openjdk.org/jeps/505">...</a>
     * VM options --enable-preview
     */
    private static final ScopedValue<String> NAME = ScopedValue.newInstance();

    public static void main(String[] args) {
        try {
//            Thread.startVirtualThread(() -> { StructuredTaskScope needs to be run on a platform thread!!!

            ScopedValue.where(NAME, "Konrad").run(() -> {
                try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                    Supplier<String> userTask = scope.fork(StructuredWithScopedValueExample::findName);
                    Supplier<Integer> idTask = scope.fork(StructuredWithScopedValueExample::findID);
                    System.out.println("StructuredTaskScope running in: " + Thread.currentThread());

                    scope.join();
                    scope.throwIfFailed();

                    System.out.println("Final result for " + NAME.get() + ":");
                    System.out.println("- Name: " + userTask.get());
                    System.out.println("- ID: " + idTask.get());
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            userWithoutScoped();

//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Integer findID() throws InterruptedException {
        System.out.println("findID running in: " + Thread.currentThread());
        System.out.println("findID - User: " + NAME.get());
        Thread.sleep(2000);
        System.out.println("after sleep findID running in: " + Thread.currentThread());
        return 98;
    }

    private static String findName() {
        System.out.println("findName running in: " + Thread.currentThread());
        System.out.println("findName - User: " + NAME.get());
        return NAME.get();
    }

    private static void userWithoutScoped() {
        try {
            System.out.println("userWithoutScoped: " + NAME.get());
        } catch (NoSuchElementException e) {
            System.out.println("userWithoutScoped - ScopedValue not set: " + e);
        }
    }
}
