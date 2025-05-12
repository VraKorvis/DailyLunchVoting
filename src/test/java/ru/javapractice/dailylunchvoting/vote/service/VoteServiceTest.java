package ru.javapractice.dailylunchvoting.vote.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.javapractice.dailylunchvoting.common.exception.AppException;
import ru.javapractice.dailylunchvoting.mapper.VoteMapper;
import ru.javapractice.dailylunchvoting.vote.model.Vote;
import ru.javapractice.dailylunchvoting.restaurant.to.VoteTo;
import ru.javapractice.dailylunchvoting.user.UserData;
import ru.javapractice.dailylunchvoting.util.TimingExtension;
import ru.javapractice.dailylunchvoting.util.OperationTimeChecker;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.javapractice.dailylunchvoting.vote.VoteData.*;

@SpringBootTest
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ExtendWith(TimingExtension.class)
public class VoteServiceTest {

    private static final Logger log = getLogger("result");

    @Autowired
    private VoteService voteService;

    @Autowired
    private VoteMapper voteMapper;

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
    public void save(){
        Vote newVOte = getNew();
        if (OperationTimeChecker.canVote()){
            voteService.vote(newVOte.getRestaurant().id(), UserData.USER_2_ID);
            VoteTo created = voteService.findByUserIdForToday(UserData.USER_2_ID);
            int createdId = created.id();
            Vote newVote = getNew();
            newVote.setId(createdId);
            MATCHER_TO.assertMatch(created, voteMapper.toVoteTo(newVote));
        }
        else {
            assertThrows(AppException.class, () -> voteService.vote(newVOte.getRestaurant().id(), UserData.USER_2_ID));
        }
    }

    @Test
    public void update() {
        Vote updated = getUpdated(USER1_TODAY_VOTE);
        if (OperationTimeChecker.canUpdateVote(updated.getVotedAt())) {
            voteService.vote(updated.getRestaurant().id(), UserData.USER_1_ID);
            VoteTo actual = voteService.findByUserIdForToday(UserData.USER_1_ID);
            MATCHER_TO.assertMatch(actual, voteMapper.toVoteTo(updated));
        } else {
            assertThrows(AppException.class, () -> voteService.vote(updated.getRestaurant().id(), UserData.USER_1_ID));
        }
    }
}