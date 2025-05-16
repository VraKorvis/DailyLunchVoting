package ru.javapractice.dailylunchvoting.app.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import ru.javapractice.dailylunchvoting.app.AuthUserProvider;
import ru.javapractice.dailylunchvoting.app.AuthUtilTestImpl;

@TestConfiguration
@EnableWebSecurity
public class SecurityTestConfig {

    @Bean
    public AuthUserProvider authUserProvider() {
        return new AuthUtilTestImpl();
    }
}
