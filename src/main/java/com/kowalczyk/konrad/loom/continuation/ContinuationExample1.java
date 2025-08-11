package com.kowalczyk.konrad.loom.continuation;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

public class ContinuationExample1 {

    /**
     * VM options --enable-preview --add-exports java.base/jdk.internal.vm=ALL-UNNAMED
     */
    public static void main(String[] args) {
        Continuation cont = getSimpleContinuation();
        cont.run();
        System.out.println("after yield");
        cont.run();
        System.out.println("after run");
    }

    private static Continuation getSimpleContinuation() {
        ContinuationScope scope = new ContinuationScope("test");
        Continuation continuation = new Continuation(scope, () ->
        {
            System.out.println("A");
            Continuation.yield(scope);
            System.out.println("B");
        });
        return continuation;
    }

}
