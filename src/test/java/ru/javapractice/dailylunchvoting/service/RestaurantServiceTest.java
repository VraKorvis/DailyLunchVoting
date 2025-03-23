package ru.javapractice.dailylunchvoting.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;

import static ru.javapractice.dailylunchvoting.service.RestaurantData.*;
import static ru.javapractice.dailylunchvoting.service.RestaurantData.RESTAURANT_A_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:/spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class RestaurantServiceTest {

    @Autowired
    private RestaurantService service;

    @Test
    public void create() {
        var created = service.create(getNew());
        int newId = created.id();
        var newRestaurant = getNew();
        newRestaurant.setId(newId);
        MATCHER.assertMatch(created, newRestaurant);
    }

    @Test
    public void get() {
        var restaurant = service.get(RESTAURANT_A_ID);
        MATCHER.assertMatch(restaurant, RESTAURANT_A);
    }

    @Test
    public void getAll() {
        var restaurants = service.getAll();
        MATCHER.assertMatch(restaurants, RESTAURANT_A, RESTAURANT_B, RESTAURANT_C);
    }

    @Test
    public void update() {
        var updated = getUpdated(RESTAURANT_A);
        service.update(updated);
        MATCHER.assertMatch(service.get(RESTAURANT_A_ID), updated);
    }
}