package ru.javapractice.dailylunchvoting.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component("loggingKeyGenerator")
@Slf4j
public class LoggingKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String key = Arrays.stream(params)
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        log.info("Generated cache key for method {}.{}: [{}]",
                target.getClass().getSimpleName(), method.getName(), key);
        return key;    }
}