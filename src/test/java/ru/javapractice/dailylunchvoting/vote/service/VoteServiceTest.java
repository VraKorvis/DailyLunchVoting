package ru.javapractice.dailylunchvoting.vote.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javapractice.dailylunchvoting.AbstractIntegrationServiceTest;
import ru.javapractice.dailylunchvoting.mapper.VoteMapper;
import ru.javapractice.dailylunchvoting.util.TimeProvider;
import ru.javapractice.dailylunchvoting.vote.model.Vote;
import ru.javapractice.dailylunchvoting.restaurant.to.VoteTo;
import ru.javapractice.dailylunchvoting.user.UserData;
import ru.javapractice.dailylunchvoting.util.OperationTimeChecker;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javapractice.dailylunchvoting.vote.VoteData.*;


public class VoteServiceTest extends AbstractIntegrationServiceTest {

    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteMapper voteMapper;
    @Autowired
    private TimeProvider timeProvider;

    @Test
    public void findAllByUserId() {
        var actual = voteService.findAllByUserId(UserData.USER_1_ID);
        var expected = voteMapper.toVoteTos(List.of(USER1_TODAY_VOTE, USER1_VOTE3, USER1_VOTE2));
        MATCHER_TO.assertMatch(actual, expected);
    }

    @Test
    public void findByUserIdForToday() {
        var actual = voteService.findByUserIdForToday(UserData.USER_1_ID);
        var expected = voteMapper.toVoteTo(USER1_TODAY_VOTE);
        MATCHER_TO.assertMatch(actual, expected);
    }

    @Test
    public void getAllForToday() {
        var actual = voteService.getAllForToday();
        var expected = voteMapper.toVoteTos(Arrays.asList(ADMIN_TODAY_VOTE_1, USER1_TODAY_VOTE));
        MATCHER_TO.assertMatch(actual, expected);
    }

    @Test
    public void getAllForTodayByUserId() {
        var actual = voteService.getAllWithRestaurantByUserId(UserData.USER_1_ID);
        var expected = List.of(USER1_TODAY_VOTE, USER1_VOTE3, USER1_VOTE2);
        MATCHER.assertMatch(actual, expected);
    }

    @Test
    public void save() {
        var createdVote = getNew();
        var result = voteService.vote(createdVote.getRestaurant().id(), UserData.USER_2_ID);
        if (OperationTimeChecker.canVote(timeProvider)) {
            voteService.vote(createdVote.getRestaurant().id(), UserData.USER_2_ID);
            var createdTo = voteService.findByUserIdForToday(UserData.USER_2_ID);
            int createdId = createdTo.id();
            var newVote = getNew();
            newVote.setId(createdId);
            MATCHER_TO.assertMatch(createdTo, voteMapper.toVoteTo(newVote));
            assertTrue(result.success(), "Expected the vote to be accepted during the voting period");
        } else {
            assertFalse(result.success(), "Expected the vote to be rejected after the voting deadline");
            assertNotNull(result.message(), "An error message should be returned when voting is not allowed");
        }
    }

    @Test
    public void update() {
        Vote updated = getUpdated(USER1_TODAY_VOTE);
        var result = voteService.vote(updated.getRestaurant().id(), UserData.USER_2_ID);
        if (OperationTimeChecker.canUpdateVote(updated.getVotedAt(), timeProvider)) {
            voteService.vote(updated.getRestaurant().id(), UserData.USER_1_ID);
            VoteTo actual = voteService.findByUserIdForToday(UserData.USER_1_ID);
            MATCHER_TO.assertMatch(actual, voteMapper.toVoteTo(updated));
            assertTrue(result.success(), "Expected the vote to be accepted during the voting period");
        } else {
            assertFalse(result.success(), "Expected the vote to be rejected after the voting deadline");
            assertNotNull(result.message(), "An error message should be returned when voting is not allowed");        }
    }
}