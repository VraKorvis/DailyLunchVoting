package ru.javapractice.dailylunchvoting.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;

import static ru.javapractice.dailylunchvoting.app.config.ConstConfig.*;

@UtilityClass
public class OperationTimeChecker {

    public static boolean canVote() {
        LocalTime now = LocalTime.now();
        return !now.isBefore(VOTING_START_TIME) && now.isBefore(VOTING_END_TIME);
    }

    public static boolean canUpdateVote(LocalDate voteDate) {
        return isToday(voteDate) && canVote();
    }

    public static boolean isPastDate(LocalDate date) {
        return date.isBefore(LocalDate.now());
    }

    public static boolean isFutureDate(LocalDate menuDate) {
        return menuDate.isAfter(LocalDate.now());
    }

    public static boolean hasVotingStarted(LocalDate date) {
        return date.equals(LocalDate.now()) && LocalTime.now().isAfter(VOTING_START_TIME);
    }

    private static boolean isToday(LocalDate date) {
        return LocalDate.now().equals(date);
    }


}
