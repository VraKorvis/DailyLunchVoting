package ru.javapractice.dailylunchvoting.restaurant.web;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javapractice.dailylunchvoting.AbstractControllerTest;
import ru.javapractice.dailylunchvoting.app.config.AppConfig;
import ru.javapractice.dailylunchvoting.app.config.SecurityTestConfig;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.common.util.JsonUtil;
import ru.javapractice.dailylunchvoting.mapper.MenuMapperService;
import ru.javapractice.dailylunchvoting.restaurant.model.Menu;
import ru.javapractice.dailylunchvoting.restaurant.service.MenuService;
import ru.javapractice.dailylunchvoting.restaurant.to.AssignedMenuTo;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javapractice.dailylunchvoting.restaurant.RestaurantMenuData.*;

@WebMvcTest(AdminMenuAssignmentController.class)
@Import({
        AdminMenuAssignmentControllerTest.TestConfig.class,
        AppConfig.class,
        SecurityTestConfig.class
})
@Slf4j
class AdminMenuAssignmentControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminMenuAssignmentController.REST_URL + '/';

    @TestConfiguration
    static class TestConfig {
        @Bean
        public MenuMapperService menuMapperService() {
            return new MenuMapperService();
        }

        @Bean
        public MenuService menuService() {
            return Mockito.mock(MenuService.class);
        }
    }

    @Autowired
    MenuMapperService menuMapper;
    @Autowired
    MenuService menuService;

    @Test
    @WithMockUser
    @DisplayName("create(): succeeds when creating and assigning menu to restaurant")
    void createAndAssignMenuToRestaurant() throws Exception {

        var newMenu = MENU_1;
        var restaurant = MENU_1.getRestaurant();
        var id = restaurant.id();

        AssignedMenuTo newMenuTo = menuMapper.toAssignedMenuTo(newMenu);

        when(menuService.create(any(AssignedMenuTo.class), anyInt()))
                .thenReturn(newMenu);
        when(menuService.getTodayMenuForRestaurantOrThrow(restaurant.id()))
                .thenReturn(newMenu);

        ResultActions resultActions = perform(MockMvcRequestBuilders.post(REST_URL + id + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(REST_URL + id + "/menu")))
                .andExpect(jsonPath("$.pricedMenuItemTos").isArray())
                .andExpect(jsonPath("$.pricedMenuItemTos.length()").value(newMenuTo.getPricedMenuItemTos().size()));

        AssignedMenuTo createdMenuTo = MENU_TO_MATCHER.readFromJson(resultActions);
        newMenuTo.setId(createdMenuTo.getId());
        MENU_TO_MATCHER.assertMatch(createdMenuTo, newMenuTo);

        Menu actualMenu = menuService.getTodayMenuForRestaurantOrThrow(restaurant.id());
        MENU_TO_MATCHER.assertMatch(menuMapper.toAssignedMenuTo(actualMenu), newMenuTo);
    }

    @Test
    @WithMockUser
    @DisplayName("create(): fails when creating menu for non-existing restaurant")
    void createMenuForNonExistingRestaurant() throws Exception {

        var id = RESTAURANT_NOT_FOUND_ID;
        AssignedMenuTo newMenuTo = menuMapper.toAssignedMenuTo(MENU_1);
        when(menuService.create(any(AssignedMenuTo.class), eq(id)))
                .thenThrow(new NotFoundException("Restaurant with id=" + id + " not found"));

        perform(MockMvcRequestBuilders.post(REST_URL + id + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("update(): succeeds when updating assigned menu for a restaurant")
    void updateAssignedMenu() throws Exception {

        var updatedMenu = MENU_1;
        var restaurant = updatedMenu.getRestaurant();
        var id = restaurant.id();

        AssignedMenuTo updatedMenuTo = menuMapper.toAssignedMenuTo(updatedMenu);

        when(menuService.create(any(AssignedMenuTo.class), anyInt()))
                .thenReturn(updatedMenu);

        perform(MockMvcRequestBuilders.put(REST_URL + id + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedMenuTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

    }

}