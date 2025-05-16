package ru.javapractice.dailylunchvoting.vote.web;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javapractice.dailylunchvoting.AbstractControllerTest;
import ru.javapractice.dailylunchvoting.app.config.AppConfig;
import ru.javapractice.dailylunchvoting.app.config.SecurityTestConfig;
import ru.javapractice.dailylunchvoting.mapper.VoteMapper;
import ru.javapractice.dailylunchvoting.restaurant.to.VoteTo;
import ru.javapractice.dailylunchvoting.vote.VoteData;
import ru.javapractice.dailylunchvoting.vote.model.VoteResult;
import ru.javapractice.dailylunchvoting.vote.service.VoteService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javapractice.dailylunchvoting.restaurant.RestaurantMenuData.*;
import static ru.javapractice.dailylunchvoting.user.UserData.*;
import static ru.javapractice.dailylunchvoting.vote.VoteData.USER1_TODAY_VOTE;

@WebMvcTest(controllers = VoteController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({
        VoteControllerTest.TestConfig.class,
        AppConfig.class,
        SecurityTestConfig.class,
})
class VoteControllerTest extends AbstractControllerTest {

    @Autowired
    VoteMapper voteMapper;
    @Autowired
    private VoteService voteService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public VoteService voteService() {
            return Mockito.mock(VoteService.class);
        }
        @Bean
        public VoteMapper voteMapper() {
            return Mappers.getMapper(VoteMapper.class);
        }
    }

    @Test
    @WithMockUser(value = USER_1_MAIL)
    void getTodayVote() throws Exception {

        VoteTo voteTo = voteMapper.toVoteTo(USER1_TODAY_VOTE);

        when(voteService.findByUserIdForToday(USER_1_ID)).thenReturn(voteTo);

        perform(MockMvcRequestBuilders.get(VoteController.REST_URL + "/for-today/me"))
                .andExpect(status().isOk())
                .andExpect(VoteData.MATCHER_TO.contentJson(voteTo));
    }

    @Test
    @WithMockUser(value = USER_1_MAIL)
    void voteSuccess() throws Exception {
        int restaurantId = RESTAURANT_A_ID;
        VoteResult voteResult = new VoteResult(true, "Your vote has been successfully updated", restaurantId);

        when(voteService.vote(restaurantId, USER_1_ID)).thenReturn(voteResult);

        perform(MockMvcRequestBuilders.post(VoteController.REST_URL)
                .param("restaurantId", String.valueOf(restaurantId)))
                .andDo(print())
                .andExpect(VoteData.MATCHER_VOTE_RESULT.contentJson(voteResult));
    }
}