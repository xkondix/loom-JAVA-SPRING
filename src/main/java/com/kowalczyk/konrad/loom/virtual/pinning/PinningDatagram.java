package com.kowalczyk.konrad.loom.virtual.pinning;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executors;

public class PinningDatagram {

    /**
     * <a href="https://openjdk.org/jeps/444#Networking">...</a>
     * VM options -Djdk.tracePinnedThreads=full
     */
    public static void main(String[] args) throws InterruptedException {

        Runnable task = () -> {
            try (DatagramSocket socket = new DatagramSocket(12345)) {
                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                System.out.println("Waiting to receive UDP packet... " + Thread.currentThread());
                socket.receive(packet); // Pinning
                System.out.println("Received on" + Thread.currentThread() + " data - " + new String(packet.getData()));
                Thread.sleep(5000);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(task);
        }

    }
}
