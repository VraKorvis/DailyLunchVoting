package ru.javapractice.dailylunchvoting.util;

import lombok.experimental.UtilityClass;
import ru.javapractice.dailylunchvoting.app.config.ConstConfig;
import ru.javapractice.dailylunchvoting.restaurant.model.Vote;

import java.time.LocalDate;
import java.time.LocalTime;

@UtilityClass
public class OperationTimeChecker {

    public static boolean canVote() {
        return isAfter(ConstConfig.VOTING_START_TIME) && isBefore(ConstConfig.VOTING_END_TIME);
    }

    public static boolean canUpdate(Vote vote) {
        return isToday(vote.getDate()) && canVote();
    }

    public static boolean canAssignMenu() {
        return isAfter(ConstConfig.VOTING_END_TIME) || isBefore(ConstConfig.VOTING_START_TIME);
    }

    public static boolean isBefore(LocalTime endTime) {
        return LocalTime.now().isBefore(endTime);
    }

    public static boolean isAfter(LocalTime startTime) {
        return LocalTime.now().isAfter(startTime);
    }

    private static boolean isToday(LocalDate date) {
        return LocalDate.now().equals(date);
    }
}
