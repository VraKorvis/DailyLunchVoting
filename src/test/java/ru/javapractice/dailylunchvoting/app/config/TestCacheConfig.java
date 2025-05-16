package ru.javapractice.dailylunchvoting.app.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@EnableCaching
public class TestCacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return CacheManagerFactory.createCaffeine();
    }
}
