package com.xtraCoder.SecurityApp.SecurityApplication.filter;

import com.xtraCoder.SecurityApp.SecurityApplication.Service.JwtService;
import com.xtraCoder.SecurityApp.SecurityApplication.Service.UserService;
import com.xtraCoder.SecurityApp.SecurityApplication.entities.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
//In this class we are creating a filter to validate the JWT token from the request header
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token (eg:- "bearer avufvugfkuae...")
        if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = requestTokenHeader.split("Bearer")[1];

            Long userId = jwtService.getUserIdFromToken(token);
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Here you can set the user details in the SecurityContext if needed
                // For example, you can load the user from the database using the userId
                // and set it in the SecurityContextHolder
                User user = userService.getUserById(userId);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, null);
                // Set the authentication in the SecurityContext
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        }catch (Exception e){
            handlerExceptionResolver.resolveException(request, response, null, e);
        }


    }
}
