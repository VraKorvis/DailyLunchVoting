package ru.javapractice.dailylunchvoting.restaurant.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javapractice.dailylunchvoting.common.error.ErrorType;
import ru.javapractice.dailylunchvoting.common.exception.AppException;
import ru.javapractice.dailylunchvoting.common.exception.ConflictException;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.restaurant.model.*;
import ru.javapractice.dailylunchvoting.restaurant.repository.MenuHistoryRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.MenuItemRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.MenuRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.restaurant.to.MenuItemTo;
import ru.javapractice.dailylunchvoting.restaurant.to.MenuTo;
import ru.javapractice.dailylunchvoting.util.OperationTimeChecker;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MenuService {

    private static final Logger log = LoggerFactory.getLogger(MenuService.class);

    MenuRepository menuRepository;
    RestaurantRepository restaurantRepository;
    MenuItemRepository menuItemRepository;
    MenuHistoryRepository menuHistoryRepository;

    public Menu get(int id) {
        return menuRepository.getReferenceById(id);
    }

    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Transactional
    public Menu create(MenuTo menuTo, int restaurantId) {
        Assert.notNull(menuTo, "menu must not be null");

        validateMenuAssignmentTimeWindow();
        Restaurant restaurant = findRestaurantById(restaurantId);

        if (isMenuAssignedForToday(restaurantId)) {
            throw new ConflictException("Menu for restaurant with id=" + restaurantId + " for today already exists");
        }

        Menu newMenu = new Menu(null, LocalDate.now(), restaurant, new ArrayList<>());
        List<MenuItemAssignment> assignments = resolveMenuItemAssignmentsStrict(newMenu, menuTo.getItems());

        newMenu.setMenuItemAssignments(assignments);
        Menu savedMenu = menuRepository.save(newMenu);
        savePriceHistoryForMenuItems(assignments);
        return savedMenu;
    }

    @Transactional
    public void update(MenuTo menuTo, int restaurantId, int menuId) {
        Assert.notNull(menuTo, "menuTo must not be null");

        validateMenuAssignmentTimeWindow();
        Menu assignedMenu = findMenuOfRestaurantByIds(menuId, restaurantId);
        List<MenuItemAssignment> assignments = resolveMenuItemAssignmentsStrict(assignedMenu, menuTo.getItems());

        updateAssignments(assignedMenu, assignments);
        menuRepository.save(assignedMenu);
        savePriceHistoryForMenuItems(assignments);
    }

    private void updateAssignments(Menu assignedMenu, List<MenuItemAssignment> assignments) {
        List<MenuItemAssignment> currentAssignments = assignedMenu.getMenuItemAssignments();
        currentAssignments.clear();
        currentAssignments.addAll(assignments);
    }

    private void validateMenuAssignmentTimeWindow() {
        if (!OperationTimeChecker.canAssignMenu()) {
            throw new AppException("Can't assign/edit menu. Time to vote for restaurant", ErrorType.APP_ERROR);
        }
    }

    private Restaurant findRestaurantById(int restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant with id=" + restaurantId + " not found"));
    }

    private boolean isMenuAssignedForToday(int restaurantId) {
        return menuRepository.findByRestaurantIdForToday(restaurantId).isPresent();
    }

    private Menu findMenuOfRestaurantByIds(int menuId, int restaurantId) {
        Optional<List<Menu>> menusOptional = menuRepository.findMenusByRestaurantId(restaurantId);

        List<Menu> menus = menusOptional
                .orElseThrow(() -> new NotFoundException("No menus found for restaurant with id=" + restaurantId));

        return menus.stream()
                .filter(menu -> menu.getId().equals(menuId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Menu with id=" + menuId + " not found in restaurant with id=" + restaurantId));
    }

    private List<MenuItemAssignment> resolveMenuItemAssignmentsStrict(Menu menu, List<MenuItemTo> itemTos) {
        List<Integer> ids = itemTos.stream()
                .map(MenuItemTo::getId)
                .toList();

        List<MenuItem> dbItems = menuItemRepository.findAllById(ids);

        if (dbItems.size() != ids.size()) {
            Set<Integer> foundIds = dbItems.stream()
                    .map(MenuItem::getId)
                    .collect(Collectors.toSet());
            List<Integer> notFoundIds = ids.stream()
                    .filter(id -> !foundIds.contains(id))
                    .toList();
            throw new NotFoundException("MenuItems not found for IDs: " + notFoundIds);
        }

        Map<Integer, MenuItem> dbItemsById = dbItems.stream()
                .collect(Collectors.toMap(MenuItem::getId, Function.identity()));

        return itemTos.stream()
                .map(dto -> {
                    MenuItem menuItem = dbItemsById.get(dto.getId());
                    return new MenuItemAssignment(menu, menuItem, dto.getPrice());
                })
                .toList();
    }

    @Transactional
    public void savePriceHistoryForMenuItems(List<MenuItemAssignment> assignments) {
        for (MenuItemAssignment assignment : assignments) {
            Optional.ofNullable(assignment.getPrice())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Price must not be null (menuItem id=" + assignment.getMenuItem().getId() + ")"
                    ));
            menuHistoryRepository.save(assignment);
        }
    }
}
