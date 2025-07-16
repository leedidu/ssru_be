package com.example.ssru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SsRuApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsRuApplication.class, args);
    }

}
