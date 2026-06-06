package com.lmello.titer;

import com.lmello.titer.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class TiterApplication {
    static void main(String[] args) {
        SpringApplication.run(TiterApplication.class, args);
    }
}
