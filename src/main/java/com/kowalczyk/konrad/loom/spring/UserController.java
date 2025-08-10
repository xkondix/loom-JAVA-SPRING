package com.kowalczyk.konrad.loom.spring;

import com.kowalczyk.konrad.loom.spring.model.UserWeatherPojo;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/weather", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<UserWeatherPojo> getUserWithWeather(@RequestParam String name) {
            return userService.getUserWithWeather(name);
        }

    @GetMapping(value = "/weather/virtual", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserWeatherPojo getUserWithWeatherVirtual(@RequestParam String name) {
        return userService.getUserWithWeatherVirtual(name);
    }
}
