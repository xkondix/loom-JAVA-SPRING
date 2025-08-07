package com.kowalczyk.konrad.loom.tool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;

public class StructuredConcurrencyExample {

    /**
     * <a href="https://openjdk.org/jeps/505">...</a>
     * VM options --enable-preview
     */
    public static void main(String[] args) {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Supplier<String> user = scope.fork(StructuredConcurrencyExample::findName);
            Supplier<Integer> id = scope.fork(StructuredConcurrencyExample::findID);

            scope.join();
            scope.throwIfFailed();  // If any of them throws an exception then abort all

            System.out.println("Name: " + user.get());
            System.out.println("id: " + id.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static Integer findID() throws InterruptedException {
        Thread.sleep(5000);
        return 1;
    }

    private static String findName() {
        return "Konrad";
    }


}
