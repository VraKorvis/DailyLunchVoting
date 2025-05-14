package ru.javapractice.dailylunchvoting.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalTime;

import static ru.javapractice.dailylunchvoting.app.config.ConstConfig.*;

@UtilityClass
public class OperationTimeChecker {

    public static boolean canVote(TimeProvider timeProvider) {
        LocalTime now = timeProvider.nowTime();
        return now.isAfter(VOTING_START_TIME) && now.isBefore(VOTING_END_TIME);
    }

    public static boolean canUpdateVote(LocalDate voteDate, TimeProvider timeProvider) {
        return isToday(voteDate, timeProvider) && canVote(timeProvider);
    }

    public static boolean isPastDate(LocalDate date, TimeProvider timeProvider) {
        return date.isBefore(timeProvider.nowDate());
    }

    public static boolean isFutureDate(LocalDate menuDate, TimeProvider timeProvider) {
        return menuDate.isAfter(timeProvider.nowDate());
    }

    public static boolean hasVotingStarted(LocalDate date, TimeProvider timeProvider) {
        return date.equals(timeProvider.nowDate()) && timeProvider.nowTime().isAfter(VOTING_START_TIME);
    }

    private static boolean isToday(LocalDate date, TimeProvider timeProvider) {
        return timeProvider.nowDate().equals(date);
    }


}
