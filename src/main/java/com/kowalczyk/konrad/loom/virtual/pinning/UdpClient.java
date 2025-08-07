package com.kowalczyk.konrad.loom.virtual.pinning;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class UdpClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        String message = "i am coming ⚡";
        System.out.println(message);
        byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
        System.out.println("Sending raw bytes: " + Arrays.toString(buffer));

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getLocalHost(), 12345);
        socket.send(packet);
        socket.close();
    }
}
