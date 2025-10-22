package com.xtraCoder.SecurityApp.SecurityApplication;

import com.xtraCoder.SecurityApp.SecurityApplication.Service.JwtService;
import com.xtraCoder.SecurityApp.SecurityApplication.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SecurityApplicationTests {

    @Autowired
    private JwtService jwtService;

	@Test
	void contextLoads() {

        User user = new User(4L, "dev@gmail.com", "12345");

        String token = jwtService.generateToken(user);

        System.out.println(token);

        Long id = jwtService.getUserIdFromToken(token);
        System.out.println(id);
	}

}
