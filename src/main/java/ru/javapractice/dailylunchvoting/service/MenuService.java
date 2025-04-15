package ru.javapractice.dailylunchvoting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javapractice.dailylunchvoting.exception.ConflictException;
import ru.javapractice.dailylunchvoting.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.model.Menu;
import ru.javapractice.dailylunchvoting.model.MenuItem;
import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.repository.datajpa.DataJpaMenuItemRepository;
import ru.javapractice.dailylunchvoting.repository.datajpa.DataJpaMenuRepository;
import ru.javapractice.dailylunchvoting.repository.datajpa.DataJpaRestaurantRepository;
import ru.javapractice.dailylunchvoting.to.MenuItemTo;
import ru.javapractice.dailylunchvoting.to.MenuTo;
import ru.javapractice.dailylunchvoting.util.MenuMapper;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.checkNotFound;

@Service
public class MenuService {

    private final DataJpaMenuRepository menuRepository;
    private final DataJpaMenuItemRepository menuItemRepository;
    private final DataJpaRestaurantRepository restaurantRepository;

    public MenuService(DataJpaMenuRepository menuRepository, DataJpaMenuItemRepository menuItemsRepository, DataJpaRestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.menuItemRepository = menuItemsRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public Menu get(int id) {
        return checkNotFound(menuRepository.getReferenceById(id), id);
    }

    public List<Menu> getAll() {
        return menuRepository.getAll();
    }

    public MenuTo create(MenuTo menuTo, int restaurantId) {
        Assert.notNull(menuTo, "menu must not be null");

        if (!restaurantRepository.existsById(restaurantId)) {
            throw new NotFoundException("Can't assign menu. Restaurant with id=" + restaurantId + " not found");
        }

        Optional<Menu> menuTodays = menuRepository.findByRestaurantIdForToday(restaurantId);
        if (menuTodays.isPresent()) {
            throw new ConflictException("Can't assign menu. Menu for restaurant with id=" + restaurantId + " for today already exists");
        }

        Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);

        List<MenuItem> resolveMenuItems = resolveMenuItems(menuTo.getItems());
        Menu newMenu = new Menu(null, LocalDate.now(), restaurant, resolveMenuItems);

        return MenuMapper.toTo(menuRepository.save(newMenu));
    }

    @Transactional
    public void update(MenuTo menuTo, int restaurantId) {
        Assert.notNull(menuTo, "menuTo must not be null");

        if (!restaurantRepository.existsById(restaurantId)) {
            throw new NotFoundException("Can't update assigned menu. Restaurant with id=" + restaurantId + " not found");
        }

        Menu menu = menuRepository.findById(menuTo.getId())
                .orElseThrow(() -> new NotFoundException("Can't update assigned menu. Menu with id=" + menuTo.id() + " not found"));

        menu.getItems().clear();
        menu.setItems(resolveMenuItems(menuTo.getItems()));
    }

    private List<MenuItem> resolveMenuItems(List<MenuItemTo> itemTos) {

        validateNoDuplicateNames(itemTos);

        List<MenuItem> result = new ArrayList<>();
        List<MenuItem> newItemsToSave = new ArrayList<>();

        Map<Integer, MenuItem> itemsById = fetchExistingItemsById(itemTos);
        Map<String, List<MenuItem>> itemsByName = fetchExistingItemsByName(itemTos);

        for (MenuItemTo to : itemTos) {
            MenuItem existing = null;
            if (to.getId() != null && !itemsById.containsKey(to.getId())) {
                throw new NotFoundException("MenuItem with id=" + to.getId() + " not found");
            }
            if (to.getId() != null) {
                existing = itemsById.get(to.getId());
                if (existing != null) {
                    if (!existing.getName().equals(to.getName())) {
                        throw new ConflictException("Item with id=" + to.getId() +
                                " exists, but with different name='" + existing.getName() + "', got='" + to.getName() + "'");
                    }
                    if (!existing.getPrice().equals(to.getPrice())) {
                        MenuItem newItem = new MenuItem(to.getName(), to.getPrice());
                        newItemsToSave.add(newItem);
                        result.add(newItem);
                    } else {
                        result.add(existing);
                    }
                    continue;
                }
            }

            List<MenuItem> sameNameItems = itemsByName.getOrDefault(to.getName(), List.of());
            Optional<MenuItem> matchByNameAndPrice = sameNameItems.stream()
                    .filter(i -> i.getPrice().equals(to.getPrice()))
                    .findFirst();

            if (matchByNameAndPrice.isPresent()) {
                result.add(matchByNameAndPrice.get());
            } else {
                MenuItem newItem = new MenuItem(to.getName(), to.getPrice());
                newItemsToSave.add(newItem);
                result.add(newItem);
            }
        }

        if (!newItemsToSave.isEmpty()) {
            menuItemRepository.saveAll(newItemsToSave);
        }

        return result;
    }

    private void validateNoDuplicateNames(List<MenuItemTo> itemTos) {
        Set<String> uniqueNames = new HashSet<>();
        for (MenuItemTo to : itemTos) {
            if (!uniqueNames.add(to.getName())) {
                throw new IllegalArgumentException("Duplicate menu item in request: " + to.getName());
            }
        }
    }

    private Map<Integer, MenuItem> fetchExistingItemsById(List<MenuItemTo> itemTos) {
        List<Integer> ids = itemTos.stream()
                .map(MenuItemTo::getId)
                .filter(Objects::nonNull)
                .toList();

        return menuItemRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(MenuItem::getId, Function.identity()));
    }

    private Map<String, List<MenuItem>> fetchExistingItemsByName(List<MenuItemTo> itemTos) {
        Set<String> names = itemTos.stream().map(MenuItemTo::getName).collect(Collectors.toSet());

        return menuItemRepository.findByNames(names).stream()
                .collect(Collectors.groupingBy(MenuItem::getName));
    }


}
