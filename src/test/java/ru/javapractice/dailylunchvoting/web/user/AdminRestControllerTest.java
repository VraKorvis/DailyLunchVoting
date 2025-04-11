package ru.javapractice.dailylunchvoting.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javapractice.dailylunchvoting.model.User;
import ru.javapractice.dailylunchvoting.service.UserService;
import ru.javapractice.dailylunchvoting.web.AbstractControllerTest;
import ru.javapractice.dailylunchvoting.web.json.JsonUtil;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javapractice.dailylunchvoting.testdata.UserData.*;

class AdminRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestController.REST_URL + '/';

    @Autowired
    private UserService userService;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + ADMIN_ID))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(ADMIN));
    }

    @Test
    void getByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-email?email=" + USER_1.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(USER_1));
    }

    @Test
    void setEnable() throws Exception {
        Map<String, Object> enableRequest = new HashMap<>();
        enableRequest.put("enable", false);

        perform(MockMvcRequestBuilders.patch(REST_URL + USER_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(enableRequest)))
                .andDo(print())
                .andExpect(status().isNoContent());

        User updatedUser = userService.get(USER_1_ID);
        assertFalse(updatedUser.isEnabled());
    }

    @Test
    void update() throws Exception {
        User updated = getUpdated(USER_1);
        perform(MockMvcRequestBuilders.put(REST_URL + USER_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER_1_ID), updated);
    }

    @Test
    void createWithLocation() throws Exception {
        User newUser = getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newUser)))
                .andExpect(status().isCreated());

        User created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userService.get(newId), newUser);
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(ADMIN, GUEST, USER_1, USER_2));
    }

}