package com.example.testtelegrambot.enums;

import java.util.Arrays;

public enum State {
    NONE,
    START,
    ENTER_NAME,
    PLAYING_QUIZ;

    public static State findByName(String name) {
        return Arrays.stream(values())
                .filter(value -> value.name().equals(name))
                .findFirst().get();
    }
}
