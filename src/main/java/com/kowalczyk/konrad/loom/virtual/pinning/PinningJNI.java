package com.kowalczyk.konrad.loom.virtual.pinning;

import java.util.concurrent.Executors;

public class PinningJNI {

    /**
     * There are two scenarios in which a virtual thread cannot be unmounted during blocking operations because it is pinned to its carrier:
     * When it executes code inside a synchronized block or method, or
     * When it executes a native method or a foreign function.
     * <a href="https://openjdk.org/jeps/444">...</a>
     * VM options -Djdk.tracePinnedThreads=full -Djava.library.path=C:\Users\konra\Desktop\loom\src\main\resources
     * use your path!!!
     * gcc -I"$env:JAVA_HOME\include" -I"$env:JAVA_HOME\include\win32" -shared -o native.dll native.c
     * gcc -I"$env:JAVA_HOME\include" -I"$env:JAVA_HOME\include\win32" -shared -o native2.dll native2.c
     * gcc -I"$env:JAVA_HOME\include" -I"$env:JAVA_HOME\include\win32" -shared -o native3.dll native3.c
     */

    static {
        System.loadLibrary("native");
        System.loadLibrary("native2");
        System.loadLibrary("native3");
    }

    private static native void nativeSleep();

    private static native void nativeSleep2();

    private static final Object LOCK = new byte[1024];

    private static native void nativeBlock(Object lock);

    public static void main(String[] args) throws InterruptedException {

        Runnable task = () -> {
            try {
                System.out.println("task 1 - " + Thread.currentThread());
                PinningJNI.nativeSleep();
                Thread.sleep(2000);
                System.out.println("task 1 - " + Thread.currentThread());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        };

        Runnable task2 = () -> {
            try {
                System.out.println("task 2 - " + Thread.currentThread());
                PinningJNI.nativeSleep2();
                Thread.sleep(2000);
                System.out.println("task 2 - " + Thread.currentThread());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        };

        Runnable task3 = () -> {
            try {
                System.out.println("task 3 - " + Thread.currentThread());
                PinningJNI.nativeBlock(LOCK);
                Thread.sleep(2000);
                System.out.println("task 3 - " + Thread.currentThread());
            } catch (Throwable e) {
                e.printStackTrace();
            }
        };

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            executor.submit(task);
            executor.submit(task2);
            executor.submit(task3);
            Thread.sleep(5000);
        }
    }
}
