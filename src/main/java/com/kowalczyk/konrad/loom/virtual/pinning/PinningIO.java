package com.kowalczyk.konrad.loom.virtual.pinning;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.Executors;

public class PinningIO {

    /**
     * The java.io package provides APIs for streams of bytes and characters.
     * The implementations of these APIs are heavily synchronized and require
     * changes to avoid pinning when they are used in virtual threads.
     */
    public static void main(String[] args) {
        Runnable task = () -> {
            System.out.println("before read - " + Thread.currentThread());
            try (var fis = new FileInputStream("C:\\Users\\konra\\Desktop\\loom\\src\\main\\resources\\example.txt")) {
                int read = fis.read();
                System.out.println(read);
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("after read- " + Thread.currentThread());
        };

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 1; i <= 10; i++) {
                executor.submit(task);
            }
        }

    }
}
