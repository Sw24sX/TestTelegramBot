package com.example.testtelegrambot.domain;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "java_quiz")
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="seq")
    @GenericGenerator(name = "seq", strategy="increment")
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "option1")
    private String optionOne;

    @Column(name = "option2")
    private String optionTwo;

    @Column(name = "option3")
    private String optionThree;

    @Column(name = "answer_correct ")
    private String answerCorrect;
}
