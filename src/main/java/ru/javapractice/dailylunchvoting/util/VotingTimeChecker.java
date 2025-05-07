package ru.javapractice.dailylunchvoting.util;

import ru.javapractice.dailylunchvoting.app.config.ConstConfig;
import ru.javapractice.dailylunchvoting.restaurant.model.Vote;

import java.time.LocalDate;
import java.time.LocalTime;

public class VotingTimeChecker {
    private VotingTimeChecker() {}

    public static boolean canVoteToday() {
        return isVotingAllowed();
    }

    public static boolean canUpdateVote(Vote vote) {
        return isToday(vote.getDate()) && isVotingAllowed();
    }

    private static boolean isVotingAllowed() {
        LocalTime now = LocalTime.now();
        LocalTime deadline = ConstConfig.VOTING_DEADLINE;
        return now.isBefore(deadline);
    }

    private static boolean isToday(LocalDate date) {
        return LocalDate.now().equals(date);
    }
}
