package com.xtraCoder.SecurityApp.SecurityApplication.repository;

import com.xtraCoder.SecurityApp.SecurityApplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
