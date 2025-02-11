package com.factopia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@SpringBootApplication
public class FactopiaApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FactopiaApplication.class, args);
    }
}