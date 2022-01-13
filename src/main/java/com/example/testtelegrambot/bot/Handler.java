package com.example.testtelegrambot.bot;

import com.example.testtelegrambot.domain.Users;
import com.example.testtelegrambot.enums.State;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.User;

import java.io.Serializable;
import java.util.List;

public interface Handler {
    // основной метод, который будет обрабатывать действия пользователя
    List<PartialBotApiMethod<? extends Serializable>> handle(Users user, String message);

    // метод, который позволяет узнать, можем ли мы обработать текущий State у пользователя
    State operatedBotState();

    // метод, который позволяет узнать, какие команды CallBackQuery мы можем обработать в этом классе
    List<String> operatedCallBackQuery();
}
