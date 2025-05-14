package ru.javapractice.dailylunchvoting.app.config;

import java.time.LocalTime;

public class ConstConfig {
    private ConstConfig() {}
    public static final LocalTime VOTING_END_TIME = LocalTime.of(11, 0);
    public static final LocalTime VOTING_START_TIME = LocalTime.of(6, 0);
}
