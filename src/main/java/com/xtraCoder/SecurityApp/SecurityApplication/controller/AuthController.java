package com.xtraCoder.SecurityApp.SecurityApplication.controller;

import com.xtraCoder.SecurityApp.SecurityApplication.Service.UserService;
import com.xtraCoder.SecurityApp.SecurityApplication.dto.SignUpDto;
import com.xtraCoder.SecurityApp.SecurityApplication.dto.UserDto;
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

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignUpDto signUpDto){
        UserDto userDto = userService.signUp(signUpDto);
        return ResponseEntity.ok(userDto);
    }
}
