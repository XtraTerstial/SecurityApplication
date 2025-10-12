package com.xtraCoder.SecurityApp.SecurityApplication.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "getAuditorAware") // Enable JPA Auditing for automatic timestamp management
public class AppConfig {

    @Bean
    ModelMapper getModelMapper(){
        return new ModelMapper();
    }


}
