package com.kowalczyk.konrad.loom.continuation.example3.custom;

import jdk.internal.vm.Continuation;

import java.util.Timer;
import java.util.TimerTask;

import static com.kowalczyk.konrad.loom.continuation.example3.custom.CustomVirtualThreadScheduler.CURRENT_VIRTUAL_THREAD;

public class CustomWaitingOperation {

    public static void perform(String name, int duration) {
        var virtualThread = CURRENT_VIRTUAL_THREAD.get();
        System.out.println("CustomVirtualThread waiting for " + name + " for " + duration + " seconds");

        var timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                CoffeehouseCustom.SCHEDULER.schedule(virtualThread);
                timer.cancel();
            }
        }, duration * 1_000L);
        Continuation.yield(CustomVirtualThread.SCOPE);
    }
}
