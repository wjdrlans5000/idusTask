package com.example.idustask;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class IdustaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdustaskApplication.class, args);
    }

}

