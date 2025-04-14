package ru.javapractice.dailylunchvoting.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.javapractice.dailylunchvoting.model.Menu;
import ru.javapractice.dailylunchvoting.model.MenuItem;
import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.repository.datajpa.DataJpaMenuItemRepository;
import ru.javapractice.dailylunchvoting.repository.datajpa.DataJpaMenuRepository;
import ru.javapractice.dailylunchvoting.repository.datajpa.DataJpaRestaurantRepository;
import ru.javapractice.dailylunchvoting.to.MenuItemTo;
import ru.javapractice.dailylunchvoting.to.MenuTo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public Menu create(MenuTo menuTo, int restaurantId) {
        Assert.notNull(menuTo, "menu must not be null");

        Restaurant restaurant = restaurantRepository.getReferenceById(restaurantId);
        List<MenuItem> resolveMenuItems = resolveMenuItems(menuTo.getItems());
        Menu menu = new Menu(null, LocalDate.now(), restaurant, resolveMenuItems);

        return menuRepository.save(menu);
    }

    public Menu get(int id) {
        return checkNotFound(menuRepository.getReferenceById(id), id);
    }

    public List<Menu> getAll() {
        return menuRepository.getAll();
    }

    @Transactional
    public void update(MenuTo menuTo) {
        Assert.notNull(menuTo, "menuTo must not be null");

        List<MenuItem> resolveMenuItems = resolveMenuItems(menuTo.getItems());
        Menu menu = menuRepository.getReferenceById(menuTo.id());
        menu.setMenuItems(resolveMenuItems);
    }

    private List<MenuItem> resolveMenuItems(List<MenuItemTo> items) {
        List<MenuItemTo> existing = items.stream()
                .filter(to -> to.getId() != null).toList();

        List<MenuItemTo> newOnes = items.stream()
                .filter(to -> to.getId() == null).toList();

        List<MenuItem> resolved = new ArrayList<>();

        resolved.addAll(menuItemRepository.findAllById(
                existing.stream().map(MenuItemTo::id).toList()
        ));

        resolved.addAll(menuItemRepository.saveAll(
                newOnes.stream()
                        .map(to -> new MenuItem(to.getName(), to.getPrice()))
                        .toList()
        ));

        return resolved;
    }

}
