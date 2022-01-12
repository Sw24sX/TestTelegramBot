package com.example.testtelegrambot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "test_spring_boot_sw24sx_bot";
    }

    @Override
    public String getBotToken() {
        return "5035525493:AAGIM3HXlVGUsW4xKJd0h7fxLqpo2TyNvn4";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText().trim();
            String chatId = update.getMessage().getChatId().toString();

            SendMessage sm = new SendMessage();
            sm.setChatId(chatId);
            sm.setText("Я люблю Настю Копытову) Она живет в г. Екатеринбурге, на ул. Восточная 84а, в кв. 14");

            try {
                execute(sm);
            } catch (TelegramApiException e) {
                //todo add logging to the project.
//                e.printStackTrace();
            }
        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}
