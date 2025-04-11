package ru.javapractice.dailylunchvoting.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.testdata.UserData;
import ru.javapractice.dailylunchvoting.util.TimingExtension;
import ru.javapractice.dailylunchvoting.util.VotingTimeChecker;
import ru.javapractice.dailylunchvoting.util.exception.VotingProcessException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.slf4j.LoggerFactory.getLogger;
import static ru.javapractice.dailylunchvoting.testdata.VoteData.*;

@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:/spring/spring-db.xml"
})
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
    public void get() {
        MATCHER.assertMatch(voteService.get(USER1_VOTE1_ID, UserData.USER_1_ID), USER1_TODAY_VOTE);
    }

    @Test
    public void getWithRestaurant() {
        MATCHER.assertMatch(voteService.getWithRestaurant(USER1_VOTE1_ID, UserData.USER_1_ID), USER1_TODAY_VOTE);
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
    public void createOrUpdate() {
        if (VotingTimeChecker.isVotingTimeExpired()) {
            assertThrows(VotingProcessException.class, () -> voteService.createOrUpdate(getNew(), UserData.USER_1_ID));
        } else {
            Vote created = voteService.createOrUpdate(getNew(), UserData.USER_1_ID);
            int createdId = created.id();
            Vote newVote = getNew();
            newVote.setId(createdId);
            MATCHER.assertMatch(created, newVote);
        }
    }

}