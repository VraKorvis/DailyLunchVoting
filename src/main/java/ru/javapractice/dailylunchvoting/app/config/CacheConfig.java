package ru.javapractice.dailylunchvoting.app.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
@EnableCaching
@Slf4j
public class CacheConfig {

    @PostConstruct
    public void init() {
        log.info("[Init][CACHE] Creating CaffeineCacheManager...");
    }

    @Bean
    public CacheManager cacheManager() {
        return CacheManagerFactory.createCaffeine();
    }
}
