package ru.javapractice.dailylunchvoting.configuration;

import java.time.LocalTime;

public class ConstConfig {
    private ConstConfig() {}
    public static final LocalTime VOTING_DEADLINE = LocalTime.of(11, 0);
}
