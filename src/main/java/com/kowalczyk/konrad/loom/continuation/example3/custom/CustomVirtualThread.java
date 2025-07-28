package com.kowalczyk.konrad.loom.continuation.example3.custom;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class CustomVirtualThread {
    public static final ContinuationScope SCOPE = new ContinuationScope("VirtualThread");
    public static final AtomicInteger COUNTER = new AtomicInteger(1);
    private final Continuation cont;
    private final int id;

    public CustomVirtualThread(Consumer<CustomVirtualThread> consumer) {
        id = COUNTER.getAndIncrement();
        Runnable runnable = () -> {
            consumer.accept(this);
        };
        cont = new Continuation(SCOPE, runnable);
    }

    public void run() {
        System.out.println("VirtualThread - " + id + " is running on" + Thread.currentThread());
        cont.run();
    }

    public int getId() {
        return id;
    }

    public boolean isDone() {
        return cont.isDone();
    }
}
