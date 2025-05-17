package ru.javapractice.dailylunchvoting.restaurant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javapractice.dailylunchvoting.AbstractIntegrationServiceTest;
import ru.javapractice.dailylunchvoting.restaurant.model.MenuItem;

import java.util.List;

import static ru.javapractice.dailylunchvoting.restaurant.MenuItemData.*;

public class MenuItemServiceTest extends AbstractIntegrationServiceTest {

    @Autowired
    private MenuItemService service;

    @Test
    public void create() {
        var created = service.create(getNew());
        var createdId = created.id();
        var newMenuItem = getNew();
        newMenuItem.setId(createdId);
        MATCHER.assertMatch(created, newMenuItem);
    }

    @Test
    public void get() {
        MATCHER.assertMatch(service.get(BURGER_ID), BURGER);
    }

    @Test
    public void getAll() {
        List<MenuItem> items = service.getAll();
        MATCHER.assertMatch(items, getAllSorted());
    }

    @Test
    public void update() {
        var updatedBurger = getUpdated(BURGER);
        service.update(updatedBurger);
        MATCHER.assertMatch(service.get(BURGER_ID), updatedBurger);
    }

}