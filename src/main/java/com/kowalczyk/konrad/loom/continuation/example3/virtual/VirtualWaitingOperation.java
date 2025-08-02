package com.kowalczyk.konrad.loom.continuation.example3.virtual;

public class VirtualWaitingOperation {

    public static void perform(String name, int delay) {
        System.out.println("VirtualThread: " + Thread.currentThread() + " Waiting for " + name + " for " + delay + " seconds");
        try {
            Thread.sleep(delay * 1_000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
