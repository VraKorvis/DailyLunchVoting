package ru.javapractice.dailylunchvoting.restaurant.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javapractice.dailylunchvoting.common.error.ErrorType;
import ru.javapractice.dailylunchvoting.common.exception.AppException;
import ru.javapractice.dailylunchvoting.common.exception.ConflictException;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.restaurant.model.*;
import ru.javapractice.dailylunchvoting.restaurant.repository.AssignedMenuItemRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.MenuItemRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.MenuRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.restaurant.to.PricedMenuItemTo;
import ru.javapractice.dailylunchvoting.restaurant.to.MenuTo;
import ru.javapractice.dailylunchvoting.util.OperationTimeChecker;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class MenuService {

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final AssignedMenuItemRepository assignedMenuItemRepository;

    public Menu get(int id) {
        return menuRepository.getReferenceById(id);
    }

    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Transactional
    public Menu create(MenuTo menuTo, int restaurantId) {
        Assert.notNull(menuTo, "menu must not be null");

        ensureMenuEditingPeriod();
        Restaurant restaurant = findRestaurantById(restaurantId);

        if (isMenuAssignedForToday(restaurantId)) {
            throw new ConflictException("Menu for restaurant with id=" + restaurantId + " for today already exists");
        }

        Menu newMenu = new Menu(null, LocalDate.now(), restaurant, new ArrayList<>());
        List<AssignedMenuItem> assignments = prepareAssignmentsFromDto(newMenu, menuTo.getPricedMenuItemTos());

        newMenu.setAssignedMenuItems(assignments);
        Menu savedMenu = menuRepository.save(newMenu);
        saveAssignedMenuItems(assignments);
        return savedMenu;
    }

    @Transactional
    public void update(MenuTo menuTo, int restaurantId) {
        Assert.notNull(menuTo, "menuTo must not be null");

        ensureMenuEditingPeriod();
        Menu assignedMenu = getTodayMenuForRestaurantOrThrow(restaurantId);
        List<AssignedMenuItem> assignments = prepareAssignmentsFromDto(assignedMenu, menuTo.getPricedMenuItemTos());

        replaceAssignments(assignedMenu, assignments);
        menuRepository.save(assignedMenu);
        saveAssignedMenuItems(assignments);
    }

    private void replaceAssignments(Menu assignedMenu, List<AssignedMenuItem> assignments) {
        List<AssignedMenuItem> currentAssignments = assignedMenu.getAssignedMenuItems();
        currentAssignments.clear();
        currentAssignments.addAll(assignments);
    }

    private void ensureMenuEditingPeriod() {
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

    private Menu getTodayMenuForRestaurantOrThrow(int restaurantId) {
        return menuRepository.findByRestaurantIdForToday(restaurantId)
                .orElseThrow(() -> new NotFoundException(
                        "Menu for restaurant with id=" + restaurantId + " for today has not been assigned yet"));
    }

    private List<AssignedMenuItem> prepareAssignmentsFromDto(Menu menu, List<PricedMenuItemTo> itemTos) {
        List<Integer> ids = itemTos.stream()
                .map(PricedMenuItemTo::getId)
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
                    return new AssignedMenuItem(menu, menuItem, dto.getPrice());
                })
                .toList();
    }

    @Transactional
    public void saveAssignedMenuItems(List<AssignedMenuItem> assignments) {
        for (AssignedMenuItem assignment : assignments) {
            Optional.ofNullable(assignment.getPrice())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Price must not be null (menuItem id=" + assignment.getMenuItem().getId() + ")"
                    ));
            assignedMenuItemRepository.save(assignment);
        }
    }
}
