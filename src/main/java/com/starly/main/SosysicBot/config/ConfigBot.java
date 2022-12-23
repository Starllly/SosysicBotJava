package com.starly.main.SosysicBot.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class ConfigBot {
    @Value("${botTelegram.name}")
    String botName;
    @Value("${botTelegram.key}")
    String token;
}
