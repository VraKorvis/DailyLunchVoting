package ru.javapractice.dailylunchvoting.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javapractice.dailylunchvoting.model.MenuItem;

import java.util.List;

import static ru.javapractice.dailylunchvoting.service.testdata.MenuItemData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:/spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
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