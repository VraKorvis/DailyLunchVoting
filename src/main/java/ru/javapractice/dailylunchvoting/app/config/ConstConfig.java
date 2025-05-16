package ru.javapractice.dailylunchvoting.app.config;

import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class ConstConfig {
    public static final LocalTime VOTING_END_TIME = LocalTime.of(11, 0);
    public static final LocalTime VOTING_START_TIME = LocalTime.of(6, 0);
}
