package ru.javapractice.dailylunchvoting.restaurant.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.AbstractControllerTest;
import ru.javapractice.dailylunchvoting.common.util.JsonUtil;
import ru.javapractice.dailylunchvoting.mapper.MenuMapperService;
import ru.javapractice.dailylunchvoting.restaurant.model.Menu;
import ru.javapractice.dailylunchvoting.restaurant.service.MenuService;
import ru.javapractice.dailylunchvoting.restaurant.to.AssignedMenuTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javapractice.dailylunchvoting.restaurant.RestaurantMenuData.*;
import static ru.javapractice.dailylunchvoting.user.UserData.ADMIN_MAIL;

@Transactional
class AdminMenuAssignmentControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminMenuAssignmentController.REST_URL + '/';

    private final LocalDateTime localDateTimeNow = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(9, 0));
    private final LocalTime timeNow = LocalTime.of(6, 0);
    private final LocalDate dateNow = LocalDate.now().minusDays(1);

    @Autowired
    MenuMapperService menuMapper;
    @Autowired
    MenuService menuService;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @DisplayName("create(): succeeds when creating and assigning menu to restaurant before voting starts")
    void createAndAssignMenuToRestaurant() throws Exception {
        try (MockedStatic<LocalDateTime> mockedDateTime = mockStatic(LocalDateTime.class, CALLS_REAL_METHODS);
             MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, CALLS_REAL_METHODS);
             MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {

            mockedDateTime.when(LocalDateTime::now).thenReturn(localDateTimeNow);
            mockedTime.when(LocalTime::now).thenReturn(timeNow);
            mockedDate.when(LocalDate::now).thenReturn(dateNow);

            AssignedMenuTo newMenuTo = createMenuWithMocked(NEW_MENU);

            ResultActions resultActions = perform(MockMvcRequestBuilders.post(REST_URL + "100006/menu")
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

            Menu actualMenu = menuService.getTodayMenuForRestaurantOrThrow(RESTAURANT_C.id());
            MENU_TO_MATCHER.assertMatch(menuMapper.toAssignedMenuTo(actualMenu), newMenuTo);
        }
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @DisplayName("create(): fails when creating menu for non-existing restaurant")
    void createMenuForNonExistingRestaurant() throws Exception {

        try (MockedStatic<LocalDateTime> mockedDateTime = mockStatic(LocalDateTime.class, CALLS_REAL_METHODS);
             MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, CALLS_REAL_METHODS);
             MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {

            mockedDateTime.when(LocalDateTime::now).thenReturn(localDateTimeNow);
            mockedTime.when(LocalTime::now).thenReturn(timeNow);
            mockedDate.when(LocalDate::now).thenReturn(dateNow);

            AssignedMenuTo newMenuTo = createMenuWithMocked(NEW_MENU);

            perform(MockMvcRequestBuilders.post(REST_URL + "999999/menu")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(newMenuTo)))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    @DisplayName("update(): succeeds when updating assigned menu for a restaurant")
    void updateAssignedMenu() throws Exception {

        try (MockedStatic<LocalDateTime> mockedDateTime = mockStatic(LocalDateTime.class, CALLS_REAL_METHODS);
             MockedStatic<LocalTime> mockedTime = mockStatic(LocalTime.class, CALLS_REAL_METHODS);
             MockedStatic<LocalDate> mockedDate = mockStatic(LocalDate.class, CALLS_REAL_METHODS)
        ) {

            mockedDateTime.when(LocalDateTime::now).thenReturn(localDateTimeNow);
            mockedTime.when(LocalTime::now).thenReturn(timeNow);
            mockedDate.when(LocalDate::now).thenReturn(dateNow);

            AssignedMenuTo updatedMenuTo = createMenuWithMocked(UPDATED_MENU);

            ResultActions resultActions = perform(MockMvcRequestBuilders.put(REST_URL + "100004/menu")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.writeValue(updatedMenuTo)))
                    .andDo(print())
                    .andExpect(status().isNoContent());

            MENU_TO_MATCHER.assertMatch(menuMapper.toAssignedMenuTo(menuService.get(UPDATED_MENU.getId())), updatedMenuTo);
        }

    }

    // WARNING: Date shifted by +1 due to mocked LocalDate.now()(-1)
    private AssignedMenuTo createMenuWithMocked(Menu menu) {
        menu.setMenuDate(dateNow.plusDays(1));
        return menuMapper.toAssignedMenuTo(menu);
    }
}