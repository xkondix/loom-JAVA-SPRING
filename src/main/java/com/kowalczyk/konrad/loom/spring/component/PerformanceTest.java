package com.kowalczyk.konrad.loom.spring.component;

import com.kowalczyk.konrad.loom.spring.UserService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Semaphore;
import java.util.stream.IntStream;

//@Component
public class PerformanceTest {

    private final Executor virtualThreadExecutor;
    private final UserService userService;
    private final Semaphore semaphore = new Semaphore(2000);

    public PerformanceTest(Executor virtualThreadExecutor, UserService userService) {
        this.virtualThreadExecutor = virtualThreadExecutor;
        this.userService = userService;
    }

    //        @PostConstruct
    public void runTests() {
        runScenarioParallelBlockingCode(() ->
                userService.getUserWithWeatherVirtual("Konrad Kowalczyk")
        );

        runScenarioParallel(() ->
                userService.getUserWithWeather("Konrad Kowalczyk")
        );
    }

    private void runScenarioParallelBlockingCode(Runnable action) {
        long start = System.currentTimeMillis();

        IntStream.range(0, 100000)
                .parallel()
                .forEach(_ -> action.run());

        long time = System.currentTimeMillis() - start;
        System.out.printf("%s took %.3f seconds%n", "Blocking code", time / 1000.0);
    }

    private void runScenarioParallel(Runnable action) {
        long start = System.currentTimeMillis();

        List<CompletableFuture<Void>> futures = IntStream.range(0, 100000)
                .mapToObj(_ -> CompletableFuture.runAsync(action))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long time = System.currentTimeMillis() - start;
        System.out.printf("%s took %.3f seconds%n", "Blocking code", time / 1000.0);
    }

    public void runScenarioParallel2(Runnable action) {
        long start = System.currentTimeMillis();

        List<CompletableFuture<Void>> futures = IntStream.range(0, 100000)
                .mapToObj(i -> CompletableFuture.runAsync(() -> {
                    try {
                        semaphore.acquire();
                        action.run();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        semaphore.release();
                    }
                }))
                .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long time = System.currentTimeMillis() - start;
        System.out.printf("Limited parallelism took %.3f seconds%n", time / 1000.0);
    }
}
