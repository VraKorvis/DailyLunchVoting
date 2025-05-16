package ru.javapractice.dailylunchvoting.restaurant.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.AbstractControllerTest;
import ru.javapractice.dailylunchvoting.mapper.RestaurantMapperService;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javapractice.dailylunchvoting.restaurant.RestaurantMenuData.*;
import static ru.javapractice.dailylunchvoting.user.UserData.USER_1_MAIL;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
class RestaurantRestControllerTest extends AbstractControllerTest {

    @Autowired
    private RestaurantMapperService restaurantMapperService;

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void getAllWithMenuForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(RestaurantRestController.REST_URL + "/with-assigned-menu"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(restaurantMapperService.toWithAssignedMenuTos(List.of(RESTAURANT_A, RESTAURANT_B))));
    }

}