package com.lmello.titer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TiterApplication {
    static void main(String[] args) {
        SpringApplication.run(TiterApplication.class, args);
    }
}
