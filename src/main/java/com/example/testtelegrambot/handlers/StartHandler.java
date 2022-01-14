package com.example.testtelegrambot.handlers;

import com.example.testtelegrambot.bot.Handler;
import com.example.testtelegrambot.domain.Users;
import com.example.testtelegrambot.enums.State;
import com.example.testtelegrambot.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.example.testtelegrambot.util.TelegramUtil.createMessageTemplate;

@Component
public class StartHandler implements Handler {
    @Value("${bot.name}")
    private String botUsername;

    private final UsersRepository userRepository;

    public StartHandler(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Users user, String message) {
        // Приветствуем пользователя
        SendMessage welcomeMessage = createMessageTemplate(user);
        welcomeMessage.setText(String.format("Hola! I'm *%s*%nI am here to help you learn Java", botUsername));

        // Просим назваться
        SendMessage registrationMessage = createMessageTemplate(user);
        registrationMessage.setText("In order to start our journey tell me your name");

        // Меняем пользователю статус на - "ожидание ввода имени"
        user.setBotState(State.ENTER_NAME.name());
        userRepository.save(user);

        return List.of(welcomeMessage, registrationMessage);
    }

    @Override
    public State operatedBotState() {
        return State.START;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
