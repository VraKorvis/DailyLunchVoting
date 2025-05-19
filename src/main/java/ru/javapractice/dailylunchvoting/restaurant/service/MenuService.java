package ru.javapractice.dailylunchvoting.restaurant.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javapractice.dailylunchvoting.app.config.ConstConfig;
import ru.javapractice.dailylunchvoting.common.exception.ConflictException;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.restaurant.model.*;
import ru.javapractice.dailylunchvoting.restaurant.repository.AssignedMenuItemRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.MenuRepository;
import ru.javapractice.dailylunchvoting.restaurant.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.restaurant.to.AssignedMenuTo;
import ru.javapractice.dailylunchvoting.restaurant.to.PricedMenuItemTo;
import ru.javapractice.dailylunchvoting.util.OperationTimeChecker;
import ru.javapractice.dailylunchvoting.util.TimeProvider;

import java.time.LocalDate;
import java.util.*;

import static ru.javapractice.dailylunchvoting.app.config.CacheKeys.*;
import static ru.javapractice.dailylunchvoting.app.config.CacheNames.*;
import static ru.javapractice.dailylunchvoting.common.MessageConstants.*;

@Service
@AllArgsConstructor
@Slf4j
public class MenuService {

    private final MenuItemService menuItemService;
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
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
    @Caching(evict = {
            @CacheEvict(value = RESTAURANTS_WITH_TODAY_MENU_LIST, allEntries = true, beforeInvocation = true),
            @CacheEvict(value = RESTAURANTS_WITH_TODAY_MENU_PAGE, allEntries = true, beforeInvocation = true),
            @CacheEvict(value = RESTAURANT, key = ID, beforeInvocation = true)
    })
    public Menu create(AssignedMenuTo menuTo, int restaurantId) {
        Assert.notNull(menuTo, "menu must not be null");

        var targetDate = menuTo.getMenuDate();
        ensureMenuEditingPeriod(targetDate);
        var restaurant = restaurantRepository.getExisted(restaurantId);

        if (existsMenu(restaurantId, targetDate)) {
            throw new ConflictException(MENU_ALREADY_EXISTS.formatted(restaurantId));
        }

        var newMenu = new Menu(null, targetDate, restaurant, new ArrayList<>());
        var savedMenu = menuRepository.save(newMenu);
        List<AssignedMenuItem> assignments = prepareAssignmentsFromTo(savedMenu, menuTo);
        checkPriceIsNull(assignments);
        assignedMenuItemRepository.saveAll(assignments);

        savedMenu.getAssignedMenuItems().clear();
        savedMenu.getAssignedMenuItems().addAll(assignments);
        return savedMenu;
    }

    private void checkPriceIsNull(List<AssignedMenuItem> assignments) {
        assignments.forEach(assignedMenuItem -> Optional.ofNullable(assignedMenuItem.getPrice())
                .orElseThrow(() -> new IllegalArgumentException(MENU_ITEM_PRICE_NULL.formatted(assignedMenuItem.getPrice()))));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = RESTAURANTS_WITH_TODAY_MENU_LIST, allEntries = true, beforeInvocation = true),
            @CacheEvict(value = RESTAURANTS_WITH_TODAY_MENU_PAGE, allEntries = true, beforeInvocation = true),
            @CacheEvict(value = RESTAURANT, key = ID, beforeInvocation = true)
    })
    public void update(AssignedMenuTo menuTo, int restaurantId) {
        Assert.notNull(menuTo, "menuTo must not be null");

        var targetDate = menuTo.getMenuDate();
        ensureMenuEditingPeriod(targetDate);
        var assignedMenu = fetchMenuOrThrow(restaurantId, targetDate);
        var assignments = prepareAssignmentsFromTo(assignedMenu, menuTo);

        replaceAssignments(assignedMenu, assignments);
        checkPriceIsNull(assignments);
        menuRepository.save(assignedMenu);
    }

    public Menu fetchMenuOrThrow(int restaurantId, LocalDate menuDate) {
        return menuRepository.findByRestaurantIdAndMenuDate(restaurantId, menuDate)
                .orElseThrow(() -> new NotFoundException(MENU_NOT_ASSIGNED.formatted(restaurantId, menuDate)));
    }

    private void replaceAssignments(Menu assignedMenu, List<AssignedMenuItem> assignments) {
        var currentAssignments = assignedMenu.getAssignedMenuItems();
        currentAssignments.clear();
        currentAssignments.addAll(assignments);
    }

    private void ensureMenuEditingPeriod(LocalDate menuDate) {
        if (OperationTimeChecker.isPastDate(menuDate, timeProvider)) {
            throw new ConflictException(CANNOT_MODIFY_PAST_DATE.formatted(menuDate));
        }
        if (OperationTimeChecker.isFutureDate(menuDate, timeProvider)) {
            return;
        }
        if (OperationTimeChecker.hasVotingStarted(menuDate, timeProvider)) {
            throw new ConflictException(CANNOT_MODIFY_AFTER_VOTING.formatted(ConstConfig.VOTING_START_TIME));
        }
    }

    private boolean existsMenu(int restaurantId, LocalDate menuDate) {
        return menuRepository.existsByRestaurantIdAndMenuDate(restaurantId, menuDate);
    }

    private List<AssignedMenuItem> prepareAssignmentsFromTo(Menu menu, AssignedMenuTo assignedMenuTo) {
        var itemTos = assignedMenuTo.getPricedMenuItemTos();
        var ids = itemTos.stream()
                .map(PricedMenuItemTo::getId)
                .toList();

        Map<Integer, MenuItem> itemsById = menuItemService.getItemMap();
        var notFoundIds = ids.stream()
                .filter(id -> !itemsById.containsKey(id))
                .toList();

        if (!notFoundIds.isEmpty()) {
            throw new NotFoundException(MENU_ITEMS_NOT_FOUND.formatted(notFoundIds));
        }

        return itemTos.stream()
                .map(dto -> {
                    MenuItem menuItem = itemsById.get(dto.getId());
                    return new AssignedMenuItem(menu, menuItem, dto.getPrice());
                })
                .toList();
    }
}
