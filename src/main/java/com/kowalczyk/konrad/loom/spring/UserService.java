package com.kowalczyk.konrad.loom.spring;

import com.kowalczyk.konrad.loom.spring.component.WeatherHttpClient;
import com.kowalczyk.konrad.loom.spring.model.User;
import com.kowalczyk.konrad.loom.spring.model.UserWeatherPojo;
import com.kowalczyk.konrad.loom.spring.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final WeatherHttpClient client;


    public UserService(UserRepository userRepository, WeatherHttpClient client) {
        this.userRepository = userRepository;
        this.client = client;
    }

    public CompletableFuture<UserWeatherPojo> getUserWithWeather(String name) {
        long start = System.currentTimeMillis();

        return CompletableFuture.supplyAsync(() -> {
            log.info("Thread before DB query - {}", Thread.currentThread());
            User user = userRepository.findByNameWithCity(name)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            log.info("Thread after DB query - {}", Thread.currentThread());
            return user;
        }).thenCompose(user -> {
            log.info("Thread before calling weather API - {}", Thread.currentThread());
            return client.getTemperatureAsync(
                    user.getCity().getLatitude(),
                    user.getCity().getLongitude()
            ).thenApply(temp -> {
                log.info("Thread after receiving weather API response - {}", Thread.currentThread());
                log.info("Total time: {} ms", (System.currentTimeMillis() - start));
                return new UserWeatherPojo(user.getName(), user.getCity().getName(), temp);
            });
        });

    }

    public UserWeatherPojo getUserWithWeatherVirtual(String name) {
        long start = System.currentTimeMillis();
        log.info("Thread before DB query - {}", Thread.currentThread());

        User user = userRepository.findByNameWithCity(name)
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("Thread before calling weather API and after DB query - {}", Thread.currentThread());
        String temp = client.getTemperatureSync(
                user.getCity().getLatitude(),
                user.getCity().getLongitude());

        log.info("Thread after receiving weather API response - {}", Thread.currentThread());
        log.info("Time of execution: {} ms", (System.currentTimeMillis() - start) / 1000.0);

        return new UserWeatherPojo(user.getName(), user.getCity().getName(), temp);
    }


}
