package ru.javapractice.dailylunchvoting.restaurant.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import ru.javapractice.dailylunchvoting.AbstractUnitServiceTest;
import ru.javapractice.dailylunchvoting.app.config.ConstConfig;
import ru.javapractice.dailylunchvoting.common.exception.ConflictException;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.mapper.MenuMapperService;
import ru.javapractice.dailylunchvoting.restaurant.model.Menu;
import ru.javapractice.dailylunchvoting.restaurant.model.MenuItem;
import ru.javapractice.dailylunchvoting.restaurant.repository.AssignedMenuItemRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.MenuRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.restaurant.to.AssignedMenuTo;
import ru.javapractice.dailylunchvoting.restaurant.to.PricedMenuItemTo;
import ru.javapractice.dailylunchvoting.util.OperationTimeChecker;
import ru.javapractice.dailylunchvoting.util.TimeProvider;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static ru.javapractice.dailylunchvoting.common.MessageConstants.*;
import static ru.javapractice.dailylunchvoting.restaurant.MenuItemData.*;
import static ru.javapractice.dailylunchvoting.restaurant.RestaurantMenuData.*;

public class MenuServiceTest extends AbstractUnitServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private AssignedMenuItemRepository assignedMenuItemRepository;
    @Mock
    private MenuItemService menuItemService;
    @Mock
    private TimeProvider mockedTimeProvider;

    @InjectMocks
    private MenuMapperService menuMapperService;
    @InjectMocks
    private MenuService menuService;


    @Test
    @DisplayName("create(): throws ConflictException when menu already exists")
    void createShouldThrowConflictWhenMenuAlreadyExists() {
        var newMenu = MENU_1;
        var menuTo = menuMapperService.toAssignedMenuTo(newMenu);

        var targetDate = menuTo.getMenuDate();
        try (MockedStatic<OperationTimeChecker> mockedOp = Mockito.mockStatic(OperationTimeChecker.class)) {
            mockedOp.when(() -> OperationTimeChecker.isPastDate(newMenu.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);
            mockedOp.when(() -> OperationTimeChecker.hasVotingStarted(newMenu.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);

            when(menuRepository.existsByRestaurantIdAndMenuDate(RESTAURANT_A_ID, targetDate))
                    .thenReturn(true);

            ConflictException ex = assertThrows(ConflictException.class, () ->
                    menuService.create(menuTo, RESTAURANT_A_ID)
            );

            assertEquals(MENU_ALREADY_EXISTS.formatted(RESTAURANT_A_ID), ex.getMessage());

            Mockito.verify(menuRepository).existsByRestaurantIdAndMenuDate(RESTAURANT_A_ID, targetDate);
        }
    }

    @Test
    @DisplayName("create(): throws ConflictException when create menu for past date")
    void createShouldThrowConflictWhenPastDate() {
        var newMenu = getNewMenu();
        var past = LocalDate.now().minusDays(1);
        newMenu.setMenuDate(past);

        var targetDate = LocalDate.now();
        when(mockedTimeProvider.nowDate()).thenReturn(targetDate);

        AssignedMenuTo menuTo = menuMapperService.toAssignedMenuTo(newMenu);

        ConflictException ex = assertThrows(ConflictException.class, () ->
                menuService.create(menuTo, RESTAURANT_A_ID)
        );
        assertEquals(CANNOT_MODIFY_PAST_DATE.formatted(past), ex.getMessage());
    }

    @Test
    @DisplayName("create(): throws ConflictException when edit today after voting has started")
    void createShouldThrowConflictWhenAfterStartToday() {
        var newMenu = getNewMenu();
        var today = LocalDate.now();
        AssignedMenuTo menuTo = menuMapperService.toAssignedMenuTo(newMenu);

        try (var mocked = Mockito.mockStatic(OperationTimeChecker.class)) {
            mocked.when(() -> OperationTimeChecker.isPastDate(today, mockedTimeProvider)).thenReturn(false);
            mocked.when(() -> OperationTimeChecker.hasVotingStarted(today, mockedTimeProvider)).thenReturn(true);

            ConflictException ex = assertThrows(ConflictException.class, () ->
                    menuService.create(menuTo, RESTAURANT_A_ID)
            );
            assertEquals(CANNOT_MODIFY_AFTER_VOTING.formatted(ConstConfig.VOTING_START_TIME), ex.getMessage());
        }
    }

    @Test
    @DisplayName("create(): succeeds when editing today before voting starts")
    void createShouldSucceedWhenTodayBeforeVoting() {
        var newMenu = getNewMenu();
        var menuDate = LocalDate.now();
        newMenu.setMenuDate(menuDate);

        var menuTo = menuMapperService.toAssignedMenuTo(newMenu);

        try (var mocked = Mockito.mockStatic(OperationTimeChecker.class)) {
            mocked.when(() -> OperationTimeChecker.isPastDate(menuDate, mockedTimeProvider)).thenReturn(false);
            mocked.when(() -> OperationTimeChecker.hasVotingStarted(menuDate, mockedTimeProvider)).thenReturn(false);
            mocked.when(() -> OperationTimeChecker.isFutureDate(menuDate, mockedTimeProvider)).thenReturn(false);

            List<MenuItem> menuItems = menuMapperService.toMenuItems(menuTo.getPricedMenuItemTos());

            Map<Integer, MenuItem> itemsMap = menuItems.stream()
                    .collect(Collectors.toMap(MenuItem::getId, Function.identity()));

            when(assignedMenuItemRepository.saveAll(any()))
                    .thenAnswer(invocation -> invocation.getArgument(0));
            when(restaurantRepository.getExisted(anyInt())).thenReturn(RESTAURANT_A);
            when(menuItemService.getItemMap()).thenReturn(itemsMap);
            when(menuRepository.save(any(Menu.class))).thenAnswer(invocation -> {
                Menu menu = invocation.getArgument(0);
                menu.setId(1);
                return menu;
            });

            var result = menuService.create(menuTo, RESTAURANT_A_ID);

            assertEquals(menuDate, result.getMenuDate());
            assertEquals(RESTAURANT_A_ID, result.getRestaurant().id());

            Mockito.verify(assignedMenuItemRepository).saveAll(any());
            Mockito.verify(menuRepository).save(any());
            Mockito.verify(restaurantRepository).getExisted(RESTAURANT_A_ID);
            Mockito.verify(menuItemService).getItemMap();
        }
    }

    @Test
    @DisplayName("create(): throws NotFoundException when restaurant not found")
    void createShouldThrowNotFoundWhenRestaurantNotFound() {

        var newMenu = getNewMenu();
        var restaurantId = RESTAURANT_A_ID;
        AssignedMenuTo menuTo = new AssignedMenuTo(null, LocalDate.now(), List.of(
                new PricedMenuItemTo(1, "Burger", BigDecimal.valueOf(100.00))
        ));

        try (MockedStatic<OperationTimeChecker> mockedOp = Mockito.mockStatic(OperationTimeChecker.class)) {
            mockedOp.when(() -> OperationTimeChecker.isPastDate(newMenu.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);
            mockedOp.when(() -> OperationTimeChecker.hasVotingStarted(newMenu.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);

            when(restaurantRepository.getExisted(restaurantId))
                    .thenThrow(new NotFoundException(RESTAURANT_NOT_FOUND.formatted(restaurantId)));


            var ex = assertThrows(NotFoundException.class, () ->
                    menuService.create(menuTo, restaurantId));

            assertEquals(RESTAURANT_NOT_FOUND.formatted(restaurantId), ex.getMessage());
            Mockito.verify(restaurantRepository).getExisted(restaurantId);
        }
    }

    @Test
    @DisplayName("create(): throws NotFoundException when one or more menu items are missing")
    void createShouldThrowNotFoundWhenMenuItemMissing() {
        var newMenu = getNewMenu();

        var menuTo = new AssignedMenuTo(null, LocalDate.now(), List.of(
                new PricedMenuItemTo(UNKNOWN_ID, "Unknown Item", BigDecimal.valueOf(100.00))
        ));

        try (MockedStatic<OperationTimeChecker> mockedOp = Mockito.mockStatic(OperationTimeChecker.class)) {
            mockedOp.when(() -> OperationTimeChecker.isPastDate(newMenu.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);
            mockedOp.when(() -> OperationTimeChecker.hasVotingStarted(newMenu.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);

            var ex = assertThrows(NotFoundException.class, () ->
                    menuService.create(menuTo, RESTAURANT_A_ID));

            List<Integer> notFoundIds = menuTo.getPricedMenuItemTos().stream()
                    .map(PricedMenuItemTo::getId)
                    .toList();

            assertEquals(MENU_ITEMS_NOT_FOUND.formatted(notFoundIds), ex.getMessage());
        }
    }

    @Test
    @DisplayName("create(): throws IllegalArgumentException when menu item price is null")
    void createShouldThrowWhenPriceNull() {
        int itemId = BURGER.id();
        var menuItem = new MenuItem();
        menuItem.setId(itemId);

        var dto = new PricedMenuItemTo(itemId, "null", null);
        var menuTo = new AssignedMenuTo(null, LocalDate.now(), List.of(dto));

        try (MockedStatic<OperationTimeChecker> mockedOp = Mockito.mockStatic(OperationTimeChecker.class)) {
            mockedOp.when(() -> OperationTimeChecker.isPastDate(menuTo.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);
            mockedOp.when(() -> OperationTimeChecker.hasVotingStarted(menuTo.getMenuDate(), mockedTimeProvider))
                    .thenReturn(false);

            when(menuRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            when(menuItemService.getItemMap()).thenReturn(Map.of(itemId, menuItem));

            assertThrows(IllegalArgumentException.class, () ->
                    menuService.create(menuTo, RESTAURANT_A_ID));

            Mockito.verify(menuRepository).save(any());
            Mockito.verify(menuItemService).getItemMap();
        }
    }
}