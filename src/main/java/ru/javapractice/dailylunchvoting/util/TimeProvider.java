package ru.javapractice.dailylunchvoting.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface TimeProvider {
    LocalDate nowDate();
    LocalTime nowTime();
    LocalDateTime nowDateTime();
}