package ru.javapractice.dailylunchvoting.service.testdata;

import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.service.util.MatcherFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javapractice.dailylunchvoting.model.AbstractBaseEntity.START_SEQ;

public class VoteData {
    public static final MatcherFactory.Matcher<Vote> MATCHER = MatcherFactory.usingIgnoringFieldsComparator( "user");
    public static final MatcherFactory.Matcher<Vote> MATCHER_IGNORE_FIELDS = MatcherFactory.usingIgnoringFieldsComparator( "restaurant", "user");

    public static final int USER1_VOTE1_ID = START_SEQ + 20;
    public static final int NOT_FOUND_ID = 10;
    public static final Vote USER1_TODAY_VOTE = new Vote(USER1_VOTE1_ID, LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 45)), RestaurantData.RESTAURANT_A);
    public static final Vote USER1_VOTE2 = new Vote(USER1_VOTE1_ID + 1, LocalDateTime.of(2025, 3, 20, 7, 0, 0), RestaurantData.RESTAURANT_B);
    public static final Vote USER2_VOTE1 = new Vote(USER1_VOTE1_ID + 2, LocalDateTime.of(2025, 3, 20, 8, 0, 0), RestaurantData.RESTAURANT_B);
    public static final Vote USER2_VOTE2 = new Vote(USER1_VOTE1_ID + 3, LocalDateTime.of(2025, 3, 20, 9, 0, 0), RestaurantData.RESTAURANT_A);
    public static final Vote ADMIN_VOTE_1 = new Vote(USER1_VOTE1_ID + 4, LocalDateTime.of(2025, 3, 21, 8, 0, 0), RestaurantData.RESTAURANT_C);
    public static final Vote ADMIN_VOTE_2 = new Vote(USER1_VOTE1_ID + 5, LocalDateTime.of(2025, 3, 21, 9, 0, 0), RestaurantData.RESTAURANT_C);

    private VoteData() {
    }

    public static List<Vote> getAllTestVotesSortedByDate() {
        return Stream.of(USER1_TODAY_VOTE, USER1_VOTE2, USER2_VOTE1, USER2_VOTE2, ADMIN_VOTE_1, ADMIN_VOTE_2)
                .sorted(Comparator.comparing(Vote::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    public static Vote getNew() {
        return new Vote(null, LocalDateTime.now(), RestaurantData.RESTAURANT_C);
    }

    public static Vote getUpdated(Vote vote) {
        var updatedVote = new Vote(vote);
        updatedVote.setRestaurant(RestaurantData.RESTAURANT_B);
        return updatedVote;
    }

}
