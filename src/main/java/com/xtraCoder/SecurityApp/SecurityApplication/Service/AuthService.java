package com.xtraCoder.SecurityApp.SecurityApplication.Service;

import com.xtraCoder.SecurityApp.SecurityApplication.dto.LoginDto;
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

    //Below method is for login
    public String login(LoginDto loginDto) {
        //Here first we are authenticating the user using AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        //Here we are generating JWT token using JwtService
        User user = (User) authentication.getPrincipal();
        return jwtService.generateToken(user);
    }
}
