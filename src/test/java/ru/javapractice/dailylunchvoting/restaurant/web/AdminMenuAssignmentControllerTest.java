package ru.javapractice.dailylunchvoting.restaurant.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javapractice.dailylunchvoting.AbstractControllerTest;
import ru.javapractice.dailylunchvoting.app.config.AppConfig;
import ru.javapractice.dailylunchvoting.app.config.SecurityTestConfig;
import ru.javapractice.dailylunchvoting.common.exception.ConflictException;
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
import static ru.javapractice.dailylunchvoting.common.MessageConstants.*;
import static ru.javapractice.dailylunchvoting.common.error.ErrorType.*;
import static ru.javapractice.dailylunchvoting.restaurant.RestaurantMenuData.*;

@WebMvcTest(AdminMenuAssignmentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({
        AdminMenuAssignmentControllerTest.TestConfig.class,
        AppConfig.class,
        SecurityTestConfig.class,
})
class AdminMenuAssignmentControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminMenuAssignmentController.REST_URL + '/';

    @Autowired
    MenuMapperService menuMapper;
    @Autowired
    MenuService menuService;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public MenuMapperService menuMapperService() {
            return new MenuMapperService();
        }

        @Bean
        public MenuService menuService() {
            return mock(MenuService.class);
        }
    }

    @Test
    @WithMockUser
    @DisplayName("create(): succeeds when creating and assigning menu to restaurant for today")
    void createAndAssignMenuToRestaurantForToday() throws Exception {

        var newMenu = MENU_1;
        var restaurant = MENU_1.getRestaurant();
        var id = restaurant.id();

        AssignedMenuTo newMenuTo = createNewMenuTo(newMenu);
        newMenuTo.setId(null);

        when(menuService.create(any(AssignedMenuTo.class), anyInt()))
                .thenReturn(newMenu);
        when(menuService.fetchMenuOrThrow(restaurant.id(),newMenuTo.getMenuDate()))
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

        Menu actualMenu = menuService.fetchMenuOrThrow(restaurant.id(), newMenuTo.getMenuDate());
        MENU_TO_MATCHER.assertMatch(createNewMenuTo(actualMenu), newMenuTo);
    }

    private AssignedMenuTo createNewMenuTo(Menu newMenu) {
        return menuMapper.toAssignedMenuTo(newMenu);
    }

    @Test
    @WithMockUser
    @DisplayName("create(): fails with 404 when creating menu for non-existing restaurant")
    void createMenuForNonExistingRestaurant() throws Exception {

        var restaurantId = RESTAURANT_NOT_FOUND_ID;
        AssignedMenuTo newMenuTo = createNewMenuTo(MENU_1);
        newMenuTo.setId(null);

        when(menuService.create(any(AssignedMenuTo.class), eq(restaurantId)))
                .thenThrow(new NotFoundException(RESTAURANT_NOT_FOUND.formatted(restaurantId)));

        perform(MockMvcRequestBuilders.post(REST_URL + restaurantId + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuTo)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.title").value(NOT_FOUND.title))
                .andExpect(jsonPath("$.detail").value(RESTAURANT_NOT_FOUND.formatted(restaurantId)));

    }

    @Test
    @WithMockUser
    @DisplayName("update(): succeeds when updating assigned menu for a restaurant")
    void updateAssignedMenu() throws Exception {

        var updatedMenu = MENU_1;
        var restaurant = updatedMenu.getRestaurant();
        var id = restaurant.id();

        AssignedMenuTo updatedMenuTo = createNewMenuTo(updatedMenu);

        doNothing().when(menuService).update(updatedMenuTo, id);

        perform(MockMvcRequestBuilders.put(REST_URL + id + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedMenuTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(menuService).update(updatedMenuTo, id);
    }

    @Test
    @WithMockUser
    @DisplayName("update(): fails with 409 when menu already exists for given restaurant")
    void updateFailsWhenMenuAlreadyExists() throws Exception {
        int restaurantId = 100003;
        AssignedMenuTo menuTo = createNewMenuTo(MENU_1);
        menuTo.setId(null);

        doThrow(new ConflictException(MENU_ALREADY_EXISTS.formatted(restaurantId)))
                .when(menuService).update(any(AssignedMenuTo.class), eq(restaurantId));

        perform(MockMvcRequestBuilders.put(REST_URL + restaurantId + "/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(menuTo)))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith("application/problem+json"))
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.title").value(DATA_CONFLICT.title))
                .andExpect(jsonPath("$.detail").value(MENU_ALREADY_EXISTS.formatted(restaurantId)));
    }
}