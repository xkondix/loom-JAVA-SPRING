package com.kowalczyk.konrad.loom.spring.component;

import com.kowalczyk.konrad.loom.spring.model.City;
import com.kowalczyk.konrad.loom.spring.model.User;
import com.kowalczyk.konrad.loom.spring.repository.CityRepository;
import com.kowalczyk.konrad.loom.spring.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    public DataInitializer(UserRepository userRepository, CityRepository cityRepository) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
    }

    @PostConstruct
    public void initData() {
        if (cityRepository.count() == 0) {
            City katowice = cityRepository.save(new City("Katowice", 50.2599, 19.0216));
            City milan = cityRepository.save(new City("Milan", 45.4642, 9.1900));
            City amsterdam = cityRepository.save(new City("Amsterdam", 52.3676, 4.9041));

            if (userRepository.count() == 0) {
                userRepository.saveAll(List.of(
                        new User("Konrad Kowalczyk", katowice),
                        new User("User 2", milan),
                        new User("User 3", amsterdam)
                ));
            }
        }
    }
}
