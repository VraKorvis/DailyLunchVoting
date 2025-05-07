package ru.javapractice.dailylunchvoting.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.common.util.JsonUtil;
import ru.javapractice.dailylunchvoting.restaurant.service.MenuService;
import ru.javapractice.dailylunchvoting.restaurant.to.MenuTo;
import ru.javapractice.dailylunchvoting.restaurant.web.AdminMenuController;
import ru.javapractice.dailylunchvoting.util.MenuMapper;
import ru.javapractice.dailylunchvoting.util.TestUtil;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javapractice.dailylunchvoting.restaurant.RestaurantMenuData.*;

@Transactional
class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminMenuController.REST_URL + '/';

    @Autowired
    MenuService menuService;

    @Test
    void createAndAssignMenuToRestaurant() throws Exception {
        MenuTo newMenuTo = MenuMapper.toTo(NEW_MENU);
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/100006/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andDo(print())
                .andExpect(status().isCreated());

        MenuTo createdMenuTo = MENU_TO_MATCHER.readFromJson(resultActions);
        newMenuTo.setId(createdMenuTo.getId());
        TestUtil.assignGeneratedIds(newMenuTo, createdMenuTo);
        MENU_TO_MATCHER.assertMatch(createdMenuTo, newMenuTo);

    }

    @Test
    void createAndAssignMenuToRestaurantWithWrongMenuItemsId() throws Exception {
        MenuTo newMenuTo = MenuMapper.toTo(WRONG_MENU);
        MvcResult result = perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/100006/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void createAndAssignMenuToRestaurantWithWrongRestaurantId() throws Exception {
        MenuTo newMenuTo = MenuMapper.toTo(NEW_MENU);
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/10/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAssignedMenu() throws Exception {
        MenuTo updatedMenuTo = MenuMapper.toTo(UPDATED_MENU);
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(REST_URL + "restaurants/100004/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedMenuTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MENU_TO_MATCHER.assertMatch(MenuMapper.toTo(menuService.get(UPDATED_MENU.getId())), updatedMenuTo);
    }
}