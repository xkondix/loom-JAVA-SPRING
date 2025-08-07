package com.kowalczyk.konrad.loom.virtual.pinning;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;
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
                String received = new String(
                        packet.getData(),
                        packet.getOffset(),
                        packet.getLength(),
                        StandardCharsets.UTF_8
                );
                System.out.println("Received on " + Thread.currentThread() + " data - " + received);
                Thread.sleep(5000);
                System.out.println("after sleep " + Thread.currentThread() + " data - " + received);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(task);
        }
    }
}
