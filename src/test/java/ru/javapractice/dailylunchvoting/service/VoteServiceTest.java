package ru.javapractice.dailylunchvoting.service;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.util.VotingTimeChecker;
import ru.javapractice.dailylunchvoting.util.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.util.exception.VotingProcessException;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:/spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class VoteServiceTest {

    @Autowired
    private VoteService voteService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getAll() {
        VoteData.VOTE_MATCHER.assertMatch(voteService.getAll(), VoteData.allVotes());
    }

    @Test
    public void create() {
        if (VotingTimeChecker.isVotingTimeExpired()) {
            Assert.assertThrows(VotingProcessException.class, () -> voteService.create(VoteData.getNew(), UserData.USER1_ID));
        } else {
            Vote created = voteService.create(VoteData.getNew(), UserData.USER1_ID);
            int createdId = created.id();
            Vote newVote = VoteData.getNew();
            newVote.setId(createdId);
            VoteData.VOTE_MATCHER.assertMatch(created, newVote);
        }
    }

    @Test
    public void get() {
        VoteData.VOTE_MATCHER.assertMatch(voteService.get(VoteData.USER1_VOTE1_ID, UserData.USER1_ID), VoteData.USER1_VOTE1);
    }

    @Test
    public void delete() {
        voteService.delete(VoteData.USER1_VOTE1_ID, UserData.USER1_ID);
        Assert.assertThrows(NotFoundException.class, () -> voteService.get(VoteData.USER1_VOTE1_ID, UserData.USER1_ID));
    }

    @Test
    public void deleteNotFound() {
        Assert.assertThrows(NotFoundException.class, () -> voteService.delete(VoteData.NOT_FOUND_ID, UserData.USER1_ID));
    }

    @Test
    public void deleteNotOwn() {
        Assert.assertThrows(NotFoundException.class, () -> voteService.delete(VoteData.USER1_VOTE1_ID, UserData.USER2_ID));
    }

    @Test
    public void update() {
        var isExpired = VotingTimeChecker.isVotingTimeExpired();
        Vote updated = VoteData.getUpdated();

        if (isExpired) {
            Assert.assertThrows(VotingProcessException.class, () ->  voteService.update(updated, UserData.USER1_ID));
        }
        else {
            voteService.update(updated, UserData.USER1_ID);
            VoteData.VOTE_MATCHER.assertMatch(voteService.get(VoteData.USER1_VOTE1_ID, UserData.USER1_ID), VoteData.getUpdated());
        }
    }
}