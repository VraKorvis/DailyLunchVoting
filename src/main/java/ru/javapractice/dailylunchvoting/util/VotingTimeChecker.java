package ru.javapractice.dailylunchvoting.util;

import ru.javapractice.dailylunchvoting.configuration.ConstConfig;

import java.time.LocalTime;

public class VotingTimeChecker {
    private VotingTimeChecker() {}

    public static boolean isVotingTimeExpired() {
        LocalTime currentTime = LocalTime.now();
        LocalTime votingDeadline = ConstConfig.VOTING_DEADLINE;
        return currentTime.isAfter(votingDeadline) || currentTime.equals(votingDeadline);
    }
}
