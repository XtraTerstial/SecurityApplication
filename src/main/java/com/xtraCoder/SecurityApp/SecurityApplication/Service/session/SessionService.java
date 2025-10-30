package com.xtraCoder.SecurityApp.SecurityApplication.Service.session;

import com.xtraCoder.SecurityApp.SecurityApplication.entities.Session;
import com.xtraCoder.SecurityApp.SecurityApplication.entities.User;
import com.xtraCoder.SecurityApp.SecurityApplication.repository.SessionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepo sessionRepo;
    private final int SESSION_LIMIT = 2;

    public void generateNewSession(User user, String refreshToken) {
        List<Session> userSessions = sessionRepo.findByUser(user);
        if(userSessions.size() == SESSION_LIMIT){
            //Remove the oldest session
            //Sort sessions by lastUsedAt
            userSessions.sort(Comparator.comparing(Session::getLastUsedAt));
            //Get the least recently used session (which is the first one after sorting)
            // And deleet it
            Session leastRecentlyUsedSession = userSessions.getFirst();
            sessionRepo.delete(leastRecentlyUsedSession);
        }

        Session newSession = Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        sessionRepo.save(newSession);
    }


    //Here we validate if the session exists for the given refresh token
    public boolean validateSession(String refreshToken) {
        Session session = sessionRepo.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new SessionAuthenticationException("Session not found for Refresh Token: " + refreshToken));
        session.setLastUsedAt(LocalDateTime.now());
        sessionRepo.save(session);
        return true;
    }

    public void deleteByRefreshToken(String refreshToken) {
        sessionRepo.deleteByRefreshToken(refreshToken);
    }
}
