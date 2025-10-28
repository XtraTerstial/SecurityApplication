package com.xtraCoder.SecurityApp.SecurityApplication.Service;

import com.xtraCoder.SecurityApp.SecurityApplication.dto.LoginDto;
import com.xtraCoder.SecurityApp.SecurityApplication.dto.LoginResponseDto;
import com.xtraCoder.SecurityApp.SecurityApplication.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService  userService;

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
        return new LoginResponseDto(user.getId(), accessToken, refreshToken);
    }

    public LoginResponseDto refreshToken(String refreshToken) {
        Long userID = jwtService.getUserIdFromToken(refreshToken);
        User user = userService.getUserById(userID);

        String accessToken = jwtService.generateAccessToken(user);
        return new LoginResponseDto(user.getId(), accessToken, refreshToken);
    }
}
