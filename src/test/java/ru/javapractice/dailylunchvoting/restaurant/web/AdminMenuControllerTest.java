package ru.javapractice.dailylunchvoting.restaurant.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.AbstractControllerTest;
import ru.javapractice.dailylunchvoting.common.util.JsonUtil;
import ru.javapractice.dailylunchvoting.mapper.MenuMapperService;
import ru.javapractice.dailylunchvoting.restaurant.model.Menu;
import ru.javapractice.dailylunchvoting.restaurant.service.MenuService;
import ru.javapractice.dailylunchvoting.restaurant.to.AssignedMenuTo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javapractice.dailylunchvoting.restaurant.RestaurantMenuData.*;

@Transactional
class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminMenuController.REST_URL + '/';

    @Autowired
    MenuMapperService menuMapper;
    @Autowired
    MenuService menuService;

    @Test
    void createAndAssignMenuToRestaurant() throws Exception {
        AssignedMenuTo newMenuTo = menuMapper.toAssignedMenuTo(NEW_MENU);
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/100006/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.pricedMenuItemTos").isArray())
                .andExpect(jsonPath("$.pricedMenuItemTos.length()").value(newMenuTo.getPricedMenuItemTos().size()));

        AssignedMenuTo createdMenuTo = MENU_TO_MATCHER.readFromJson(resultActions);
        newMenuTo.setId(createdMenuTo.getId());
        MENU_TO_MATCHER.assertMatch(createdMenuTo, newMenuTo);

        Menu actualMenu = menuService.getTodayMenuForRestaurantOrThrow(100006);
        MENU_TO_MATCHER.assertMatch(menuMapper.toAssignedMenuTo(actualMenu), newMenuTo);

        assertNotNull(createdMenuTo.getId(), "ID должен быть установлен после создания");
    }

    @Test
    void createMenuForNonExistingRestaurant() throws Exception {
        AssignedMenuTo newMenuTo = menuMapper.toAssignedMenuTo(NEW_MENU);
        perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/999999/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andExpect(status().isNotFound()); // Ожидаем статус 404
    }

    @Test
    void createAndAssignMenuToRestaurantWithWrongMenuItemsId() throws Exception {
        AssignedMenuTo newMenuTo = menuMapper.toAssignedMenuTo(WRONG_MENU);
        MvcResult result = perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/100006/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void createAndAssignMenuToRestaurantWithWrongRestaurantId() throws Exception {
        AssignedMenuTo newMenuTo = menuMapper.toAssignedMenuTo(NEW_MENU);
        ResultActions resultActions = perform(MockMvcRequestBuilders.post(REST_URL + "restaurants/10/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAssignedMenu() throws Exception {
        AssignedMenuTo updatedMenuTo = menuMapper.toAssignedMenuTo(UPDATED_MENU);
        ResultActions resultActions = perform(MockMvcRequestBuilders.put(REST_URL + "restaurants/100004/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedMenuTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        MENU_TO_MATCHER.assertMatch(menuMapper.toAssignedMenuTo(menuService.get(UPDATED_MENU.getId())), updatedMenuTo);
    }
}