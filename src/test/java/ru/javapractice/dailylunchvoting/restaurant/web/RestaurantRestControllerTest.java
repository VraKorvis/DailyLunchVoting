package ru.javapractice.dailylunchvoting.restaurant.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javapractice.dailylunchvoting.AbstractControllerTest;
import ru.javapractice.dailylunchvoting.mapper.RestaurantMapperService;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javapractice.dailylunchvoting.restaurant.RestaurantMenuData.*;

class RestaurantRestControllerTest extends AbstractControllerTest {

    private final String REST_URL = RestaurantRestController.REST_URL + "/";

    @Autowired
    private RestaurantMapperService restaurantMapperService;

    @Test
    void getWithMenuForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_A_ID + "/with-menu"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(restaurantMapperService.toWithAssignedMenuTo(RESTAURANT_A)));
    }

    @Test
    void getAllWithMenuForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "with-menu"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(Stream.of(RESTAURANT_A, RESTAURANT_B)
                        .map((r) -> restaurantMapperService.toWithAssignedMenuTo(RESTAURANT_A))
                        .collect(Collectors.toList()))
                );
    }

}