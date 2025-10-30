package com.xtraCoder.SecurityApp.SecurityApplication.handlers;

import com.xtraCoder.SecurityApp.SecurityApplication.Service.JwtService;
import com.xtraCoder.SecurityApp.SecurityApplication.Service.UserService;
import com.xtraCoder.SecurityApp.SecurityApplication.entities.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public final UserService userService;
    private final JwtService jwtService;
    @Value("${deploy.env}")
    private String deployEnv;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        User user =userService.getUserByEmail(email);

     if (user == null){
         User newUser = User.builder()
                 .name(oAuth2User.getAttribute("name"))
                 .email(email)
                 .build();
         user = userService.save(newUser);
     }
        String accessToken =  jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Create HttpOnly cookie to store refresh token
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);

        // Redirect to frontend with access token as query parameter
        String frontEndUrl = "http://localhost:8080/home.html?token=" + accessToken;
//        getRedirectStrategy().sendRedirect(request, response
//        , frontEndUrl); -> you can also use the both the lines there purpose is same
        response.sendRedirect(frontEndUrl);

    }
}
