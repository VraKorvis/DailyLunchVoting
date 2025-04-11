package ru.javapractice.dailylunchvoting.testdata;

import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.util.MatcherFactory;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javapractice.dailylunchvoting.model.AbstractBaseEntity.START_SEQ;

public class VoteData {
    public static final MatcherFactory.Matcher<Vote> MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "restaurant.menus", "user");

    public static final int USER1_VOTE1_ID = START_SEQ + 20;
    public static final int NOT_FOUND_ID = 10;
    public static final Vote USER1_TODAY_VOTE = new Vote(USER1_VOTE1_ID, LocalDate.now(), RestaurantData.RESTAURANT_C);
    public static final Vote USER2_TODAY_VOTE = new Vote(USER1_VOTE1_ID + 1, LocalDate.now(), RestaurantData.RESTAURANT_A);
    public static final Vote USER1_VOTE2 = new Vote(USER1_VOTE1_ID + 2, LocalDate.of(2025, 3, 20), RestaurantData.RESTAURANT_A);
    public static final Vote USER2_VOTE1 = new Vote(USER1_VOTE1_ID + 3, LocalDate.of(2025, 3, 20), RestaurantData.RESTAURANT_B);
    public static final Vote USER2_VOTE2 = new Vote(USER1_VOTE1_ID + 4, LocalDate.of(2025, 3, 20), RestaurantData.RESTAURANT_C);
    public static final Vote USER1_VOTE3 = new Vote(USER1_VOTE1_ID + 5, LocalDate.of(2025, 3, 21), RestaurantData.RESTAURANT_B);
    public static final Vote ADMIN_VOTE_1 = new Vote(USER1_VOTE1_ID + 6, LocalDate.of(2025, 3, 21), RestaurantData.RESTAURANT_A);

    private VoteData() {
    }

    public static List<Vote> getAllTestVotes() {
        return Stream.of(USER2_TODAY_VOTE, USER1_TODAY_VOTE, USER1_VOTE2, USER2_VOTE1, USER2_VOTE2, USER1_VOTE3, ADMIN_VOTE_1)
                .sorted(Comparator.comparing(Vote::getDate)
                        .reversed()
                        .thenComparing(Vote::id, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public static Vote getNew() {
        return new Vote(null, LocalDate.now(), RestaurantData.RESTAURANT_C);
    }

    public static Vote getUpdated(Vote vote) {
        var updatedVote = new Vote(vote);
        updatedVote.setRestaurant(RestaurantData.RESTAURANT_B);
        return updatedVote;
    }

}
