package ru.javapractice.dailylunchvoting.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class SystemTimeProvider implements TimeProvider {
    @Override
    public LocalDate nowDate() {
        return LocalDate.now();
    }

    @Override
    public LocalTime nowTime() {
        return LocalTime.now();
    }

    @Override
    public LocalDateTime nowDateTime() {
        return LocalDateTime.now();
    }
}
