package com.starly.main.SosysicBot.model;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<TelegramUser, Long> {
}
