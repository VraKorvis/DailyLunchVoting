package ru.javapractice.dailylunchvoting.service;

import ru.javapractice.dailylunchvoting.model.Vote;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javapractice.dailylunchvoting.model.AbstractBaseEntity.START_SEQ;

public class VoteData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator("restaurant", "user");

    public static final int USER1_VOTE1_ID = START_SEQ + 7;
    public static final int NOT_FOUND_ID = 10;
    public static final Vote USER1_VOTE1 = new Vote(USER1_VOTE1_ID, LocalDateTime.of(2025, 3, 20, 7, 0, 0), RestaurantData.restaurantA);
    public static final Vote USER1_VOTE2 = new Vote(USER1_VOTE1_ID + 1, LocalDateTime.of(2025, 3, 20, 8, 0, 0), RestaurantData.restaurantB);
    public static final Vote USER2_VOTE1 = new Vote(USER1_VOTE1_ID + 2, LocalDateTime.of(2025, 3, 20, 9, 0, 0), RestaurantData.restaurantB);
    public static final Vote USER2_VOTE2 = new Vote(USER1_VOTE1_ID + 3, LocalDateTime.of(2025, 3, 21, 8, 0, 0), RestaurantData.restaurantA);
    public static final Vote ADMIN_VOTE_1 = new Vote(USER1_VOTE1_ID + 4, LocalDateTime.of(2025, 3, 21, 9, 0, 0), RestaurantData.restaurantC);
    public static final Vote ADMIN_VOTE_2 = new Vote(USER1_VOTE1_ID + 5, LocalDateTime.of(2025, 3, 21, 10, 0, 0), RestaurantData.restaurantC);

    private VoteData() {
    }

    public static List<Vote> allVotes() {
        return Stream.of(USER1_VOTE1, USER1_VOTE2, USER2_VOTE1, USER2_VOTE2, ADMIN_VOTE_1, ADMIN_VOTE_2)
                .sorted(Comparator.comparing(Vote::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    public static Vote getNew() {
        return new Vote(LocalDateTime.of(2025, 3, 22, 7, 0, 0), RestaurantData.restaurantA);
    }

    public static Vote getUpdated() {
        return new Vote(LocalDateTime.of(2025, 3, 22, 10, 0, 0), RestaurantData.restaurantB);
    }
}
