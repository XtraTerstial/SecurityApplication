package com.xtraCoder.SecurityApp.SecurityApplication.controller;

import com.xtraCoder.SecurityApp.SecurityApplication.Service.AuthService;
import com.xtraCoder.SecurityApp.SecurityApplication.Service.UserService;
import com.xtraCoder.SecurityApp.SecurityApplication.dto.LoginDto;
import com.xtraCoder.SecurityApp.SecurityApplication.dto.SignUpDto;
import com.xtraCoder.SecurityApp.SecurityApplication.dto.UserDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpDto signUpDto){
        UserDto userDto = userService.signUp(signUpDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletRequest request,
                                        HttpServletResponse response){
        String token = authService.login(loginDto);
        // Here we are creating a HttpOnly cookie to store the JWT token
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ResponseEntity.ok(token);
    }
}
