package com.xtraCoder.SecurityApp.SecurityApplication.repository;

import com.xtraCoder.SecurityApp.SecurityApplication.entities.Session;
import com.xtraCoder.SecurityApp.SecurityApplication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SessionRepo extends JpaRepository<Session, Long> {

    List<Session> findByUser(User user);

    Optional<Session> findByRefreshToken(String refreshToken);

    @Transactional
    void deleteByRefreshToken(String refreshToken);
}
