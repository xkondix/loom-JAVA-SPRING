package com.kowalczyk.konrad.loom.tool;

import java.util.NoSuchElementException;

public class ScopedValuesExample {

    /**
     * <a href="https://openjdk.org/jeps/506">...</a>
     * VM options --enable-preview
     */

    private static final ScopedValue<String> NAME = ScopedValue.newInstance();

    public static void main(String[] args) {
        ScopedValue.where(NAME, "Konrad").run(ScopedValuesExample::user1);
        user2();
        ScopedValue.where(NAME, "Weronika").run(ScopedValuesExample::user3);
        ScopedValue.where(NAME, "Mateusz").run(() -> {
            user3();
            Thread.ofVirtual().start(ScopedValuesExample::user4);
        });


    }

    static void user1() {
        System.out.println("user1, " + NAME.get());
    }

    static void user2() {
        try {
            System.out.println("user2, " + NAME.get());

        } catch (NoSuchElementException e) {
            System.out.println("user2 - " + e);
        }
    }

    static void user3() {
        System.out.println("user3, " + NAME.get());
        user4();
    }

    static void user4() {
        System.out.println("user4, " + NAME.get());

    }
}
