package ru.javapractice.dailylunchvoting.restaurant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.javapractice.dailylunchvoting.restaurant.model.MenuItem;

import java.util.List;

import static ru.javapractice.dailylunchvoting.restaurant.MenuItemData.*;

@SpringBootTest
@Transactional
public class MenuItemServiceTest {

    @Autowired
    private MenuItemService service;

    @Test
    public void create() {
        var created = service.create(getNew());
        int createdId = created.id();
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