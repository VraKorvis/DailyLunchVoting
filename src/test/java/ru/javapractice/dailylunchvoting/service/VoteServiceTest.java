package ru.javapractice.dailylunchvoting.service;

import org.junit.*;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javapractice.dailylunchvoting.model.Vote;
import ru.javapractice.dailylunchvoting.service.testdata.UserData;
import ru.javapractice.dailylunchvoting.util.VotingTimeChecker;
import ru.javapractice.dailylunchvoting.util.exception.VotingProcessException;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javapractice.dailylunchvoting.service.testdata.VoteData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:/spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class VoteServiceTest {

    private static final Logger log = getLogger("result");
    private static final StringBuilder results = new StringBuilder();

    @Rule
    // http://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-bussiness-relev
    public final Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };

    @Autowired
    private VoteService voteService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void printResult() {
        log.info("\n---------------------------------" +
                "\nTest                 Duration, ms" +
                "\n---------------------------------" +
                results +
                "\n---------------------------------");
    }

    @Test
    public void getAll() {
        MATCHER_IGNORE_FIELDS.assertMatch(voteService.getAll(), getAllTestVotesSortedByDate());
    }

    @Test
    public void getAllByUserId() {
        MATCHER_IGNORE_FIELDS.assertMatch(voteService.getAllByUserId(UserData.USER1_ID), USER1_TODAY_VOTE, USER1_VOTE3, USER1_VOTE2);
    }

    @Test
    public void get() {
        MATCHER_IGNORE_FIELDS.assertMatch(voteService.get(USER1_VOTE1_ID, UserData.USER1_ID), USER1_TODAY_VOTE);
    }

    @Test
    public void getWithRestaurant() {
        MATCHER.assertMatch(voteService.getWithRestaurant(USER1_VOTE1_ID, UserData.USER1_ID), USER1_TODAY_VOTE);
    }

    @Test
    public void getAllWithRestaurant() {
        MATCHER.assertMatch(voteService.getAllWithRestaurant(), getAllTestVotesSortedByDate());
    }

    @Test
    public void getAllWithRestaurantByUserId() {
        MATCHER.assertMatch(voteService.getAllWithRestaurantByUserId(UserData.USER1_ID), getAllTestVotesSortedByDate());
    }

    @Test
    public void create() {
        if (VotingTimeChecker.isVotingTimeExpired()) {
            Assert.assertThrows(VotingProcessException.class, () -> voteService.create(getNew(), UserData.USER1_ID));
        } else {
            Vote created = voteService.create(getNew(), UserData.USER1_ID);
            int createdId = created.id();
            Vote newVote = getNew();
            newVote.setId(createdId);
            MATCHER.assertMatch(created, newVote);
        }
    }

    @Test
    public void update() {
        var isExpired = VotingTimeChecker.isVotingTimeExpired();
        Vote updated = getUpdated(USER1_TODAY_VOTE);

        if (isExpired) {
            Assert.assertThrows(VotingProcessException.class, () ->  voteService.update(updated, UserData.USER1_ID));
        }
        else {
            voteService.update(updated, UserData.USER1_ID);
            MATCHER.assertMatch(voteService.getWithRestaurant(USER1_VOTE1_ID, UserData.USER1_ID), updated);
        }
    }

}