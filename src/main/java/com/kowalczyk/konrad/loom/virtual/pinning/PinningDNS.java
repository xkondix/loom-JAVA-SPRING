package com.kowalczyk.konrad.loom.virtual.pinning;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

public class PinningDNS {

    /**
     * <a href="https://openjdk.org/jeps/444#Networking">...</a>
     * VM options -Djdk.tracePinnedThreads=full
     */
    public static void main(String[] args) {
        Runnable task = () -> {
            System.out.println("Starting DNS resolution... " + Thread.currentThread());
            InetAddress address = null;
            try {
                address = InetAddress.getByName("openjdk.org"); // Potential pinning
                Thread.sleep(1000);
            } catch (UnknownHostException | InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Resolved: " + address + " " + Thread.currentThread());
        };

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 1; i <= 3; i++) {
                executor.submit(task);
            }
        }

    }

}
