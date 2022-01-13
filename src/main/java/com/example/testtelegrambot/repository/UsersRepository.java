package com.example.testtelegrambot.repository;

import com.example.testtelegrambot.domain.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> getByChatId(String chatId);
}