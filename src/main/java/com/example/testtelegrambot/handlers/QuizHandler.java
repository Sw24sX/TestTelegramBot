package com.example.testtelegrambot.handlers;

import com.example.testtelegrambot.bot.Handler;
import com.example.testtelegrambot.domain.Question;
import com.example.testtelegrambot.domain.Users;
import com.example.testtelegrambot.enums.State;
import com.example.testtelegrambot.repository.QuestionRepository;
import com.example.testtelegrambot.repository.UsersRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.testtelegrambot.util.TelegramUtil.createInlineKeyboardButton;
import static com.example.testtelegrambot.util.TelegramUtil.createMessageTemplate;

@Component
public class QuizHandler implements Handler {
    //Храним поддерживаемые CallBackQuery в виде констант
    public static final String QUIZ_CORRECT = "/quiz_correct";
    public static final String QUIZ_INCORRECT = "/quiz_incorrect";
    public static final String QUIZ_START = "/quiz_start";
    //Храним варианты ответа
    private static final List<String> OPTIONS = List.of("A", "B", "C", "D");

    private final UsersRepository userRepository;
    private final QuestionRepository questionRepository;

    public QuizHandler(UsersRepository userRepository, QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(Users user, String message) {
        if (message.startsWith(QUIZ_CORRECT)) {
            // действие на коллбек с правильным ответом
            return correctAnswer(user, message);
        } else if (message.startsWith(QUIZ_INCORRECT)) {
            // действие на коллбек с неправильным ответом
            return incorrectAnswer(user);
        } else {
            return startNewQuiz(user);
        }
    }

    private List<PartialBotApiMethod<? extends Serializable>> correctAnswer(Users user, String message) {
        final int currentScore = user.getScore() + 1;
        user.setScore(currentScore);
        userRepository.save(user);

        return nextQuestion(user);
    }

    private List<PartialBotApiMethod<? extends Serializable>> incorrectAnswer(Users user) {
        final int currentScore = user.getScore();
        // Обновляем лучший итог
        if (user.getHighScore() < currentScore) {
            user.setHighScore(currentScore);
        }
        // Меняем статус пользователя
        user.setScore(0);
        user.setBotState(State.NONE.name());
        userRepository.save(user);

        // Создаем кнопку для повторного начала игры
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Try again?", QUIZ_START));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        return List.of(createMessageTemplate(user)
                .setText(String.format("Incorrect!%nYou scored *%d* points!", currentScore))
                .setReplyMarkup(inlineKeyboardMarkup));
    }

    private List<PartialBotApiMethod<? extends Serializable>> startNewQuiz(Users user) {
        user.setBotState(State.PLAYING_QUIZ.name());
        userRepository.save(user);

        return nextQuestion(user);
    }

    private List<PartialBotApiMethod<? extends Serializable>> nextQuestion(Users user) {
        Question question = questionRepository.getRandomQuestion();

        // Собираем список возможных вариантов ответа
        List<String> options = new ArrayList<>(List.of(question.getAnswerCorrect(), question.getOptionOne(), question.getOptionTwo(), question.getOptionThree()));
        // Перемешиваем
        Collections.shuffle(options);

        // Начинаем формировать сообщение с вопроса
        StringBuilder sb = new StringBuilder();
        sb.append('*')
                .append(question.getQuestion())
                .append("*\n\n");

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        // Создаем два ряда кнопок
        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtonsRowTwo = new ArrayList<>();

        // Формируем сообщение и записываем CallBackData на кнопки
        for (int i = 0; i < options.size(); i++) {
            InlineKeyboardButton button = new InlineKeyboardButton();

            final String callbackData = options.get(i).equalsIgnoreCase(question.getAnswerCorrect()) ? QUIZ_CORRECT : QUIZ_INCORRECT;

            button.setText(OPTIONS.get(i))
                    .setCallbackData(String.format("%s %d", callbackData, question.getId()));

            if (i < 2) {
                inlineKeyboardButtonsRowOne.add(button);
            } else {
                inlineKeyboardButtonsRowTwo.add(button);
            }
            sb.append(OPTIONS.get(i) + ". " + options.get(i));
            sb.append("\n");
        }

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne, inlineKeyboardButtonsRowTwo));
        return List.of(createMessageTemplate(user)
                .setText(sb.toString())
                .setReplyMarkup(inlineKeyboardMarkup));
    }


    @Override
    public State operatedBotState() {
        return null;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(QUIZ_START, QUIZ_CORRECT, QUIZ_INCORRECT);
    }
}
