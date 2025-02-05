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
        Environment env = context.getEnvironment();

        System.out.println("✅ Active Profile: " + Arrays.toString(env.getActiveProfiles()));
        System.out.println("✅ Datasource URL: " + env.getProperty("spring.datasource.url"));
        System.out.println("✅ Datasource Username: " + env.getProperty("spring.datasource.username"));
        System.out.println("✅ Driver Class Name: " + env.getProperty("spring.datasource.driver-class-name"));
    }
}