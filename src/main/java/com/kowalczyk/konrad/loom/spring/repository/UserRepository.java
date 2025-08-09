package com.kowalczyk.konrad.loom.spring.repository;

import com.kowalczyk.konrad.loom.spring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u JOIN FETCH u.city WHERE u.name = :name")
    Optional<User> findByNameWithCity(@Param("name") String name);
}
