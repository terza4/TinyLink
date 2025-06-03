package com.example.tinylink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TinyLinkApplication {
    public static void main(String[] args) {
        SpringApplication.run(TinyLinkApplication.class, args);
    }
}
