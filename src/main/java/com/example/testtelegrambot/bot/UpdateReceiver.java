package com.example.testtelegrambot.bot;

import com.example.testtelegrambot.domain.Users;
import com.example.testtelegrambot.enums.State;
import com.example.testtelegrambot.repository.UsersRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Component
public class UpdateReceiver {
    private final List<Handler> handlers;

    private final UsersRepository usersRepository;

    public UpdateReceiver(List<Handler> handlers, UsersRepository usersRepository) {
        this.handlers = handlers;
        this.usersRepository = usersRepository;
    }

    // Обрабатываем полученный Update
    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        // try-catch, чтобы при несуществующей команде просто возвращать пустой список
        try {
            // Проверяем, если Update - сообщение с текстом
            if (isMessageWithText(update)) {
                // Получаем Message из Update
                Message message = update.getMessage();
                // Получаем айди чата с пользователем
                String chatId = message.getFrom().getId().toString();

                // Просим у репозитория пользователя. Если такого пользователя нет - создаем нового и возвращаем его.
                // Как раз на случай нового пользователя мы и сделали конструктор с одним параметром в классе User
                Users user = usersRepository.getByChatId(chatId)
                        .orElseGet(() -> usersRepository.save(new Users(chatId)));
                // Ищем нужный обработчик и возвращаем результат его работы
                return getHandlerByState(State.findByName(user.getBotState())).handle(user, message.getText());

            } else if (update.hasCallbackQuery()) {
                final CallbackQuery callbackQuery = update.getCallbackQuery();
                String chatId = callbackQuery.getFrom().getId().toString();
                final Users user = usersRepository.getByChatId(chatId)
                        .orElseGet(() -> usersRepository.save(new Users(chatId)));

                return getHandlerByCallBackQuery(callbackQuery.getData()).handle(user, callbackQuery.getData());
            }

            throw new UnsupportedOperationException();
        } catch (UnsupportedOperationException e) {
            return Collections.emptyList();
        }
    }

    private Handler getHandlerByState(State state) {
        return handlers.stream()
                .filter(h -> h.operatedBotState() != null)
                .filter(h -> h.operatedBotState().equals(state))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private Handler getHandlerByCallBackQuery(String query) {
        return handlers.stream()
                .filter(h -> h.operatedCallBackQuery().stream()
                        .anyMatch(query::startsWith))
                .findAny()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private boolean isMessageWithText(Update update) {
        return !update.hasCallbackQuery() && update.hasMessage() && update.getMessage().hasText();
    }
}
