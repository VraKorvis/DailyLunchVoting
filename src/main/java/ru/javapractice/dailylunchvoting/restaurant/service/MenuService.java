package ru.javapractice.dailylunchvoting.restaurant.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javapractice.dailylunchvoting.app.config.CacheNames;
import ru.javapractice.dailylunchvoting.app.config.ConstConfig;
import ru.javapractice.dailylunchvoting.common.exception.ConflictException;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.restaurant.model.*;
import ru.javapractice.dailylunchvoting.restaurant.repository.AssignedMenuItemRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.MenuItemRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.MenuRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.restaurant.to.AssignedMenuTo;
import ru.javapractice.dailylunchvoting.restaurant.to.PricedMenuItemTo;
import ru.javapractice.dailylunchvoting.util.OperationTimeChecker;
import ru.javapractice.dailylunchvoting.util.TimeProvider;

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
    private final TimeProvider timeProvider;

    @Cacheable("todayMenus")
    public Menu get(int id) {
        return menuRepository.getExisted(id);
    }

    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Transactional
    @CacheEvict(value = {CacheNames.RESTAURANTS_WITH_TODAY_MENU, CacheNames.RESTAURANT}, allEntries = true)
    public Menu create(AssignedMenuTo menuTo, int restaurantId) {
        Assert.notNull(menuTo, "menu must not be null");

        LocalDate targetDate = menuTo.getMenuDate();
        ensureMenuEditingPeriod(targetDate);
        Restaurant restaurant = findRestaurantById(restaurantId);

        if (isMenuAssignedForToday(restaurantId)) {
            throw new ConflictException("Menu for restaurant with id=" + restaurantId + " for today already exists");
        }

        Menu newMenu = new Menu(null, targetDate, restaurant, new ArrayList<>());
        List<AssignedMenuItem> assignments = prepareAssignmentsFromTo(newMenu, menuTo);

        newMenu.setAssignedMenuItems(assignments);
        Menu savedMenu = menuRepository.save(newMenu);
        saveAssignedMenuItems(assignments);
        log.info("Menu {} for restaurant {} assigned for {}", savedMenu.id(), restaurantId, targetDate);
        return savedMenu;
    }

    @Transactional
    @CacheEvict(value = {CacheNames.RESTAURANTS_WITH_TODAY_MENU, CacheNames.RESTAURANT}, beforeInvocation = true, allEntries = true)
    public void update(AssignedMenuTo menuTo, int restaurantId) {
        Assert.notNull(menuTo, "menuTo must not be null");

        var targetDate = menuTo.getMenuDate();
        ensureMenuEditingPeriod(targetDate);
        var assignedMenu = getTodayMenuForRestaurantOrThrow(restaurantId);
        var assignments = prepareAssignmentsFromTo(assignedMenu, menuTo);

        replaceAssignments(assignedMenu, assignments);
        menuRepository.save(assignedMenu);
        saveAssignedMenuItems(assignments);
    }

    public Menu getTodayMenuForRestaurantOrThrow(int restaurantId) {
        return menuRepository.findByRestaurantIdForToday(restaurantId)
                .orElseThrow(() -> new NotFoundException(
                        "Menu for restaurant with id=" + restaurantId + " for today has not been assigned yet"));
    }

    private void replaceAssignments(Menu assignedMenu, List<AssignedMenuItem> assignments) {
        var currentAssignments = assignedMenu.getAssignedMenuItems();
        currentAssignments.clear();
        currentAssignments.addAll(assignments);
    }

    private void ensureMenuEditingPeriod(LocalDate menuDate) {
        if (OperationTimeChecker.isPastDate(menuDate, timeProvider)) {
            throw new ConflictException("Cannot create/edit menu for past date " + menuDate);
        }
        if (OperationTimeChecker.isFutureDate(menuDate, timeProvider)) {
            return;
        }
        if (OperationTimeChecker.hasVotingStarted(menuDate, timeProvider)) {
            throw new ConflictException(
                    "Cannot edit today's menu after voting has started at " + ConstConfig.VOTING_START_TIME
            );
        }
    }

    private Restaurant findRestaurantById(int restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant with id=" + restaurantId + " not found"));
    }

    private boolean isMenuAssignedForToday(int restaurantId) {
        return menuRepository.findByRestaurantIdForToday(restaurantId).isPresent();
    }

    private List<AssignedMenuItem> prepareAssignmentsFromTo(Menu menu, AssignedMenuTo assignedMenuTo) {
        var itemTos = assignedMenuTo.getPricedMenuItemTos();
        var ids = itemTos.stream()
                .map(PricedMenuItemTo::getId)
                .toList();

        var dbItems = menuItemRepository.findAllById(ids);

        if (dbItems.size() != ids.size()) {
            var foundIds = dbItems.stream()
                    .map(MenuItem::getId)
                    .collect(Collectors.toSet());
            var notFoundIds = ids.stream()
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
