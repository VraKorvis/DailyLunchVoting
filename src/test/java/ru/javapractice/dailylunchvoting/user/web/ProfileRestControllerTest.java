package ru.javapractice.dailylunchvoting.user.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.common.util.JsonUtil;
import ru.javapractice.dailylunchvoting.mapper.UserMapper;
import ru.javapractice.dailylunchvoting.user.model.User;
import ru.javapractice.dailylunchvoting.user.repository.UserRepository;
import ru.javapractice.dailylunchvoting.AbstractControllerTest;
import ru.javapractice.dailylunchvoting.user.to.UserTo;


import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javapractice.dailylunchvoting.user.UserData.*;
import static ru.javapractice.dailylunchvoting.user.web.ProfileController.REST_URL;

@SpringBootTest
@Transactional
class ProfileRestControllerTest extends AbstractControllerTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper userMapper;

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(USER_1));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Operation not supported")));
    }

    @Test
    void register() throws Exception {
        var newTo = new UserTo(null, "newName", "newemail@ya.ru", "newPassword");
        var newUser = userMapper.createNewFromTo(newTo);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        var created = USER_MATCHER.readFromJson(action);
        int newId = created.id();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(repository.getExisted(newId), newUser);
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void update() throws Exception {
        var updatedTo = new UserTo(null, "newName", USER_1_MAIL, "newPassword");
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        var updatedUser = new User(USER_1);
        userMapper.updateFromTo(updatedUser, updatedTo);
        USER_MATCHER.assertMatch(repository.getExisted(USER_1_ID), updatedUser);
    }

    @Test
    void registerInvalid() throws Exception {
        var newTo = new UserTo(null, null, null, null);
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void updateInvalid() throws Exception {
        var updatedTo = new UserTo(null, null, "password", null);
        perform(MockMvcRequestBuilders.put(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_1_MAIL)
    void updateDuplicate() throws Exception {
        var updatedTo = new UserTo(null, "newName", ADMIN_MAIL, "newPassword");
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string(containsString(UniqueMailValidator.EXCEPTION_DUPLICATE_EMAIL)));
    }
}