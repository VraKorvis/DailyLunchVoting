package ru.javapractice.dailylunchvoting.restaurant.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.javapractice.dailylunchvoting.common.exception.VotingProcessException;
import ru.javapractice.dailylunchvoting.restaurant.model.Vote;
import ru.javapractice.dailylunchvoting.user.UserData;
import ru.javapractice.dailylunchvoting.util.TimingExtension;
import ru.javapractice.dailylunchvoting.util.VotingTimeChecker;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.javapractice.dailylunchvoting.restaurant.VoteData.*;

@SpringBootTest
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ExtendWith(TimingExtension.class)
public class VoteServiceTest {

    private static final Logger log = getLogger("result");

    @Autowired
    private VoteService voteService;

    @Test
    public void getAll() {
        MATCHER.assertMatch(voteService.getAll(), getAllTestVotes());
    }

    @Test
    public void getAllByUserId() {
        MATCHER.assertMatch(voteService.getAllByUserId(UserData.USER_1_ID), USER1_TODAY_VOTE, USER1_VOTE3, USER1_VOTE2);
    }

    @Test
    public void findByUserIdForToday() {
        MATCHER.assertMatch(voteService.findByUserIdForToday(UserData.USER_1_ID), USER1_TODAY_VOTE);
    }

    @Test
    public void getAllWithRestaurantForToday() {
        MATCHER.assertMatch(voteService.getAllWithRestaurantForToday(), Arrays.asList(USER2_TODAY_VOTE, USER1_TODAY_VOTE));
    }

    @Test
    public void getAllWithRestaurantForTodayByUserId() {
        MATCHER.assertMatch(voteService.getAllWithRestaurantByUserId(UserData.USER_1_ID), USER1_TODAY_VOTE, USER1_VOTE3, USER1_VOTE2);
    }

    @Test
    public void save(){
        Vote newVOte = getNew();
        if (VotingTimeChecker.canVoteToday()){
            voteService.vote(newVOte.getRestaurant().id(), UserData.USER_2_ID);
            Vote created = voteService.findByUserIdForToday(UserData.USER_2_ID);
            int createdId = created.id();
            Vote newVote = getNew();
            newVote.setId(createdId);
            MATCHER.assertMatch(created, newVote);
        }
        else {
            assertThrows(VotingProcessException.class, () -> voteService.vote(newVOte.getRestaurant().id(), UserData.USER_2_ID));
        }
    }

    @Test
    public void update() {
        Vote updated = getUpdated(USER1_TODAY_VOTE);
        if (VotingTimeChecker.canUpdateVote(updated)) {
            voteService.vote(updated.getRestaurant().id(), UserData.USER_1_ID);
            Vote actual = voteService.findByUserIdForToday(UserData.USER_1_ID);
            MATCHER.assertMatch(actual, updated);
        } else {
            assertThrows(VotingProcessException.class, () -> voteService.vote(updated.getRestaurant().id(), UserData.USER_1_ID));
        }
    }
}