package ru.javapractice.dailylunchvoting.vote.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.mapper.VoteMapper;
import ru.javapractice.dailylunchvoting.util.TimeProvider;
import ru.javapractice.dailylunchvoting.vote.model.Vote;
import ru.javapractice.dailylunchvoting.restaurant.to.VoteTo;
import ru.javapractice.dailylunchvoting.user.UserData;
import ru.javapractice.dailylunchvoting.util.TimingExtension;
import ru.javapractice.dailylunchvoting.util.OperationTimeChecker;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static ru.javapractice.dailylunchvoting.vote.VoteData.*;

@SpringBootTest
@ExtendWith(TimingExtension.class)
@Slf4j(topic = "result")
@Transactional
public class VoteServiceTest {

    @Autowired
    private VoteService voteService;
    @Autowired
    private VoteMapper voteMapper;
    @Autowired
    private TimeProvider timeProvider;

    @Test
    public void getAllByUserId() {
        MATCHER.assertMatch(voteService.getAllByUserId(UserData.USER_1_ID), USER1_TODAY_VOTE, USER1_VOTE3, USER1_VOTE2);
    }

    @Test
    public void findByUserIdForToday() {
        MATCHER_TO.assertMatch(voteService.findByUserIdForToday(UserData.USER_1_ID), voteMapper.toVoteTo(USER1_TODAY_VOTE));
    }

    @Test
    public void getAllForToday() {
        MATCHER_TO.assertMatch(voteService.getAllForToday(), voteMapper.toVoteTos(Arrays.asList(ADMIN_TODAY_VOTE_1, USER1_TODAY_VOTE)));
    }

    @Test
    public void getAllForTodayByUserId() {
        MATCHER.assertMatch(voteService.getAllWithRestaurantByUserId(UserData.USER_1_ID), USER1_TODAY_VOTE, USER1_VOTE3, USER1_VOTE2);
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