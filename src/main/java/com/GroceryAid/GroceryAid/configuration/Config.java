package com.GroceryAid.GroceryAid.configuration;

import com.GroceryAid.GroceryAid.services.WalmartSignatureGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class Config {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public WalmartSignatureGenerator walmartSignatureGenerator() {
        return new WalmartSignatureGenerator();
    }
}
