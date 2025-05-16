package ru.javapractice.dailylunchvoting.restaurant.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.javapractice.dailylunchvoting.app.config.ConstConfig;
import ru.javapractice.dailylunchvoting.common.exception.ConflictException;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.mapper.MenuMapperService;
import ru.javapractice.dailylunchvoting.restaurant.model.AssignedMenuItem;
import ru.javapractice.dailylunchvoting.restaurant.model.Menu;
import ru.javapractice.dailylunchvoting.restaurant.model.MenuItem;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.restaurant.repository.AssignedMenuItemRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.MenuItemRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.MenuRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.restaurant.to.AssignedMenuTo;
import ru.javapractice.dailylunchvoting.restaurant.to.PricedMenuItemTo;
import ru.javapractice.dailylunchvoting.util.OperationTimeChecker;
import ru.javapractice.dailylunchvoting.util.TimeProvider;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static ru.javapractice.dailylunchvoting.restaurant.MenuItemData.BURGER;
import static ru.javapractice.dailylunchvoting.restaurant.RestaurantMenuData.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private MenuItemRepository menuItemRepository;
    @Mock
    private AssignedMenuItemRepository assignedMenuItemRepository;

    @InjectMocks
    private MenuMapperService menuMapperService;
    @InjectMocks
    private MenuService menuService;
    @Mock
    private TimeProvider mockedTimeProvider;

    @Test
    @DisplayName("create(): throws ConflictException when menu already exists")
    void createShouldThrowConflictWhenMenuAlreadyExists() {
        Menu newMenu = MENU_1;
        AssignedMenuTo menuTo = menuMapperService.toAssignedMenuTo(newMenu);

        try (MockedStatic<OperationTimeChecker> mockedOp = Mockito.mockStatic(OperationTimeChecker.class)) {
            mockedOp.when(() -> OperationTimeChecker.isPastDate(newMenu.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);
            mockedOp.when(() -> OperationTimeChecker.hasVotingStarted(newMenu.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);

            when(restaurantRepository.findById(RESTAURANT_A_ID))
                    .thenReturn(Optional.of(newMenu.getRestaurant()));

            when(menuRepository.findByRestaurantIdForToday(RESTAURANT_A_ID))
                    .thenReturn(Optional.of(newMenu));

            ConflictException ex = assertThrows(ConflictException.class, () ->
                    menuService.create(menuTo, RESTAURANT_A_ID)
            );

            assertEquals(
                    "Menu for restaurant with id=" + RESTAURANT_A_ID + " for today already exists",
                    ex.getMessage()
            );

            Mockito.verify(menuRepository).findByRestaurantIdForToday(RESTAURANT_A_ID);
        }
    }

    @Test
    @DisplayName("create(): throws ConflictException when create menu for past date")
    void createShouldThrowConflictWhenPastDate() {
        Menu newMenu = getNewMenu();
        LocalDate past = LocalDate.now().minusDays(1);
        newMenu.setMenuDate(past);

        LocalDate targetDate = LocalDate.now();
        when(mockedTimeProvider.nowDate()).thenReturn(targetDate);

        AssignedMenuTo menuTo = menuMapperService.toAssignedMenuTo(newMenu);

        ConflictException ex = assertThrows(ConflictException.class, () ->
                menuService.create(menuTo, RESTAURANT_A_ID)
        );
        assertEquals("Cannot create/edit menu for past date " + past, ex.getMessage());
    }

    @Test
    @DisplayName("create(): throws ConflictException when edit today after voting has started")
    void createShouldThrowConflictWhenAfterStartToday() {
        Menu newMenu = getNewMenu();
        LocalDate today = LocalDate.now();
        AssignedMenuTo menuTo = menuMapperService.toAssignedMenuTo(newMenu);

        try (var mocked = Mockito.mockStatic(OperationTimeChecker.class)) {
            mocked.when(() -> OperationTimeChecker.isPastDate(today, mockedTimeProvider)).thenReturn(false);
            mocked.when(() -> OperationTimeChecker.hasVotingStarted(today, mockedTimeProvider)).thenReturn(true);

            ConflictException ex = assertThrows(ConflictException.class, () ->
                    menuService.create(menuTo, RESTAURANT_A_ID)
            );
            assertEquals(
                    "Cannot edit today's menu after voting has started at "
                    + ConstConfig.VOTING_START_TIME,
                    ex.getMessage()
            );
        }
    }

    @Test
    @DisplayName("create(): succeeds when editing today before voting starts")
    void createShouldSucceedWhenTodayBeforeVoting() {
        Menu newMenu = getNewMenu();
        LocalDate menuDate = LocalDate.now();
        newMenu.setMenuDate(menuDate);

        AssignedMenuTo menuTo = menuMapperService.toAssignedMenuTo(newMenu);

        try (var mocked = Mockito.mockStatic(OperationTimeChecker.class)) {
            mocked.when(() -> OperationTimeChecker.isPastDate(menuDate, mockedTimeProvider)).thenReturn(false);
            mocked.when(() -> OperationTimeChecker.hasVotingStarted(menuDate, mockedTimeProvider)).thenReturn(false);
            mocked.when(() -> OperationTimeChecker.isFutureDate(menuDate, mockedTimeProvider)).thenReturn(false);

            when(restaurantRepository.findById(RESTAURANT_A_ID)).thenReturn(Optional.of(RESTAURANT_A));
            List<MenuItem> menuItems = menuMapperService.toMenuItems(menuTo.getPricedMenuItemTos());
            when(menuItemRepository.findAllById(any())).thenReturn(menuItems);
            when(menuRepository.findByRestaurantIdForToday(RESTAURANT_A_ID)).thenReturn(Optional.empty());

            when(menuRepository.save(any(Menu.class))).thenAnswer(invocation -> {
                Menu menu = invocation.getArgument(0);
                menu.setId(1);
                return menu;
            });

            when(assignedMenuItemRepository.save(any(AssignedMenuItem.class)))
                    .thenAnswer(inv -> inv.getArgument(0));

            Menu result = menuService.create(menuTo, RESTAURANT_A_ID);

            assertEquals(menuDate, result.getMenuDate());
            assertEquals(RESTAURANT_A_ID, result.getRestaurant().id());
        }
    }

    @Test
    @DisplayName("create(): throws NotFoundException when restaurant not found")
    void createShouldThrowNotFoundWhenRestaurantNotFound() {
        Menu newMenu = getNewMenu();

        int restaurantId = RESTAURANT_A_ID;
        AssignedMenuTo menuTo = new AssignedMenuTo(null, LocalDate.now(), List.of(
                new PricedMenuItemTo(1, "Burger", BigDecimal.valueOf(100.00))
        ));

        try (MockedStatic<OperationTimeChecker> mockedOp = Mockito.mockStatic(OperationTimeChecker.class)) {
            mockedOp.when(() -> OperationTimeChecker.isPastDate(newMenu.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);
            mockedOp.when(() -> OperationTimeChecker.hasVotingStarted(newMenu.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);

            when(restaurantRepository.findById(restaurantId))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () ->
                    menuService.create(menuTo, restaurantId));

            Mockito.verify(restaurantRepository).findById(restaurantId);
        }
    }

    @Test
    @DisplayName("create(): throws NotFoundException when one or more menu items are missing")
    void createShouldThrowNotFoundWhenMenuItemMissing() {
        Menu newMenu = getNewMenu();

        int restaurantId = RESTAURANT_A_ID;
        int itemId = MENU_1.getId();
        AssignedMenuTo menuTo = new AssignedMenuTo(null, LocalDate.now(), List.of(
                new PricedMenuItemTo(itemId, "Burger", BigDecimal.valueOf(100.00))
        ));

        try (MockedStatic<OperationTimeChecker> mockedOp = Mockito.mockStatic(OperationTimeChecker.class)) {
            mockedOp.when(() -> OperationTimeChecker.isPastDate(newMenu.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);
            mockedOp.when(() -> OperationTimeChecker.hasVotingStarted(newMenu.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);

            when(menuRepository.findByRestaurantIdForToday(restaurantId))
                    .thenReturn(Optional.empty());

            when(restaurantRepository.findById(restaurantId))
                    .thenReturn(Optional.of(new Restaurant()));

            when(menuItemRepository.findAllById(List.of(itemId)))
                    .thenReturn(List.of());

            assertThrows(NotFoundException.class, () ->
                    menuService.create(menuTo, restaurantId));
        }
    }

    @Test
    @DisplayName("create(): throws NotFoundException when menu items not found")
    void createShouldThrowWhenPriceNull() {
        int restaurantId = RESTAURANT_A_ID;
        int itemId = BURGER.id();
        MenuItem menuItem = new MenuItem();
        menuItem.setId(itemId);

        PricedMenuItemTo dto = new PricedMenuItemTo(itemId, "null", null);
        AssignedMenuTo menuTo = new AssignedMenuTo(null, LocalDate.now(), List.of(dto));

        try (MockedStatic<OperationTimeChecker> mockedOp = Mockito.mockStatic(OperationTimeChecker.class)) {
            mockedOp.when(() -> OperationTimeChecker.isPastDate(menuTo.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);
            mockedOp.when(() -> OperationTimeChecker.hasVotingStarted(menuTo.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);

            when(menuRepository.findByRestaurantIdForToday(restaurantId))
                    .thenReturn(Optional.empty());
            when(restaurantRepository.findById(restaurantId))
                    .thenReturn(Optional.of(new Restaurant()));
            when(menuItemRepository.findAllById(List.of(itemId)))
                    .thenReturn(List.of(menuItem));
            when(menuRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            assertThrows(IllegalArgumentException.class, () ->
                    menuService.create(menuTo, restaurantId));
        }
    }

}