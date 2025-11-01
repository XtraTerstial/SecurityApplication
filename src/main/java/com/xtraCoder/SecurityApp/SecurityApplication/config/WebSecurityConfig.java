package com.xtraCoder.SecurityApp.SecurityApplication.config;

import com.xtraCoder.SecurityApp.SecurityApplication.entities.enums.Permission;
import com.xtraCoder.SecurityApp.SecurityApplication.entities.enums.Role;
import com.xtraCoder.SecurityApp.SecurityApplication.filter.JwtAuthFilter;
import com.xtraCoder.SecurityApp.SecurityApplication.handlers.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import static com.xtraCoder.SecurityApp.SecurityApplication.entities.enums.Permission.*;
import static com.xtraCoder.SecurityApp.SecurityApplication.entities.enums.Role.ADMIN;
import static com.xtraCoder.SecurityApp.SecurityApplication.entities.enums.Role.CREATOR;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
//Security filter chain is a series of filters that
// process incoming requests to your application.
//Here, we are configuring how security is handled in the application.
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private static final String[] publicRoutes = {
            "/error","/auth/**","/home.html"
    };

    // It sets up a "security filter chain" that
    // intercepts incoming requests and decides who is allowed to access what.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)throws Exception{
        httpSecurity
                //authorizeHttpRequests(...): This starts the process
                // of defining access rules. It's like telling
                // the bouncer at a club which rules to follow.
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(publicRoutes).permitAll()
                                .requestMatchers(HttpMethod.GET,"/posts/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/posts/**")
                                .hasAnyRole(ADMIN.name(), CREATOR.name())
                        .requestMatchers(HttpMethod.POST,"/posts/**")
                            .hasAnyAuthority(POST_CREATE.name())
                        .requestMatchers(HttpMethod.GET,"/posts/**")
                            .hasAnyAuthority(POST_VIEW.name())
                        .requestMatchers(HttpMethod.PUT,"/posts/**")
                            .hasAnyAuthority(POST_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE,"/posts/**")
                            .hasAnyAuthority(POST_DELETE.name())
                        .anyRequest().authenticated()
                )
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionConfig -> sessionConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Below line is to add our custom JWT filter before the default UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2Config -> oauth2Config
                        .failureUrl("/login?error=true")
                        .successHandler(oAuth2SuccessHandler)
                );

//                .logout(logout -> logout
//                        .logoutUrl("/logout")// The URL to call for logout
//                        .invalidateHttpSession(true)// This is what "deletes" the session on the server side
//                        .deleteCookies("JSESSIONID")// clears the session cookie
//                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())// Sends 200 ok
//                )


        return httpSecurity.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

//    @Bean
//    UserDetailsService myInMemoryUserDetailsService(){
//        UserDetails normalUser = User
//                .withUsername("devbrat")
//                .password(passwordEncoder().encode("devbrat123"))
//                .roles("USER")
//                .build();
//        UserDetails adminUser = User
//                .withUsername("Bob the Builder")
//                .password(passwordEncoder().encode("Bob9876"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(normalUser, adminUser);
//    }

}
