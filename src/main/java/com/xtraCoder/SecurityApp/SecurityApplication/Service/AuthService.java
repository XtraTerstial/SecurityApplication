package com.xtraCoder.SecurityApp.SecurityApplication.Service;

import com.xtraCoder.SecurityApp.SecurityApplication.Service.session.SessionService;
import com.xtraCoder.SecurityApp.SecurityApplication.dto.LoginDto;
import com.xtraCoder.SecurityApp.SecurityApplication.dto.LoginResponseDto;
import com.xtraCoder.SecurityApp.SecurityApplication.entities.Session;
import com.xtraCoder.SecurityApp.SecurityApplication.entities.User;
import com.xtraCoder.SecurityApp.SecurityApplication.repository.SessionRepo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService  userService;
    private final SessionService sessionService;
    private final SessionRepo sessionRepo;

    //Below method is for login
    public LoginResponseDto login(LoginDto loginDto) {
        //Here first we are authenticating the user using AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        //Here we are generating JWT token using JwtService
        User user = (User) authentication.getPrincipal();
        String accessToken =  jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        sessionService.generateNewSession(user, refreshToken);

        return new LoginResponseDto(user.getId(), accessToken, refreshToken);
    }

    public LoginResponseDto refreshToken(String refreshToken) {
        Long userID = jwtService.getUserIdFromToken(refreshToken);
        sessionService.validateSession(refreshToken);
        User user = userService.getUserById(userID);

        String accessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDto(user.getId(), accessToken, refreshToken);
    }

   public void logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = WebUtils.getCookie(request, "refreshToken");
        if(cookie != null){
            String refreshToken = cookie.getValue();
            sessionService.deleteByRefreshToken(refreshToken);
            //Remove the cookie from client side
            Cookie deleteCookie = new Cookie("refreshToken", null);
            deleteCookie.setMaxAge(0);
            deleteCookie.setPath("/");
            response.addCookie(deleteCookie);
        }
    }
}

