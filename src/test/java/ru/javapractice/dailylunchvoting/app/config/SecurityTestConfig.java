package ru.javapractice.dailylunchvoting.app.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.javapractice.dailylunchvoting.app.AuthUserProvider;
import ru.javapractice.dailylunchvoting.app.AuthUtilTestImpl;

@TestConfiguration
@EnableWebSecurity
@Import(AuthUtilTestImpl.class)
public class SecurityTestConfig {

    @Bean
    public AuthUserProvider authUserProvider() {
        return new AuthUtilTestImpl();
    }

    @Bean("userDetailsService")
    public UserDetailsService userDetailsService(AuthUserProvider authUserProvider) {
        return username -> authUserProvider.safeGet();
    }
}
