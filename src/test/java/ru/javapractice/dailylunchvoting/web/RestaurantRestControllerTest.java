package ru.javapractice.dailylunchvoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javapractice.dailylunchvoting.util.RestaurantMapper;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javapractice.dailylunchvoting.testdata.RestaurantMenuData.*;

class RestaurantRestControllerTest extends AbstractControllerTest{

    private final String REST_URL = RestaurantRestController.REST_URL + "/";

    @Test
    void getWithMenuForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + RESTAURANT_A_ID + "/with-menu"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(RestaurantMapper.toTo(RESTAURANT_A)));
    }

    @Test
    void getAllWithMenuForToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "with-menu"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(Stream.of(RESTAURANT_A, RESTAURANT_B)
                        .map(RestaurantMapper::toTo)
                        .collect(Collectors.toList()))
                );
    }

}