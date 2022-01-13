package com.example.testtelegrambot.handlers;

import com.example.testtelegrambot.bot.Handler;
import com.example.testtelegrambot.domain.Users;
import com.example.testtelegrambot.enums.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.example.testtelegrambot.handlers.RegistrationHandler.NAME_CHANGE;
import static com.example.testtelegrambot.util.TelegramUtil.createInlineKeyboardButton;
import static com.example.testtelegrambot.util.TelegramUtil.createMessageTemplate;

@Component
public class HelpHandler implements Handler {
    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Users user, String message) {
        // Создаем кнопку для смены имени
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Change name", NAME_CHANGE));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        return List.of(createMessageTemplate(user).setText(String.format("" +
                "You've asked for help %s? Here it comes!", user.getName()))
                .setReplyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public State operatedBotState() {
        return State.NONE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return Collections.emptyList();
    }
}
