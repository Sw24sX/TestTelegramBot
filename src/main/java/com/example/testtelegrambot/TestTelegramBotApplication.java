package com.example.testtelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
public class TestTelegramBotApplication {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(TestTelegramBotApplication.class, args);
    }

}
