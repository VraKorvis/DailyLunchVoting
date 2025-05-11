package ru.javapractice.dailylunchvoting.restaurant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import ru.javapractice.dailylunchvoting.mapper.RestaurantMapperService;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantTo;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantWithAssignedMenuTo;

import java.util.List;
import java.util.stream.Stream;

import static ru.javapractice.dailylunchvoting.restaurant.RestaurantMenuData.*;

@SpringBootTest
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class RestaurantServiceTest {

    @Autowired
    private RestaurantMapperService restaurantMapperService;

    @Autowired
    private RestaurantService service;

    @Test
    public void create() {
        var created = service.create(new RestaurantTo(null, getNew().getName()));
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
    public void getWithMenuForToday() {
        var restaurant = service.getWithMenuForToday(RESTAURANT_A_ID);
        RESTAURANT_TO_MATCHER.assertMatch(restaurant, restaurantMapperService.toWithAssignedMenuTo(RESTAURANT_A));
    }

    @Test
    public void getAll() {
        var restaurants = service.getAll();
        MATCHER.assertMatch(restaurants, RESTAURANT_A, RESTAURANT_B, RESTAURANT_C);
    }

    @Test
    public void getAllWithAssignedMenuForToday() {
        var restaurants = service.getAllWithAssignedMenuForToday();
        List<RestaurantWithAssignedMenuTo> restaurantWithMenuTos = Stream.of(RESTAURANT_A, RESTAURANT_B)
                .map((r) -> restaurantMapperService.toWithAssignedMenuTo(r) )
                .toList();
        RESTAURANT_TO_MATCHER.assertMatch(restaurants, restaurantWithMenuTos);
    }

    @Test
    public void update() {
        var updated = getUpdated(RESTAURANT_A);
        service.update(updated);
        MATCHER.assertMatch(service.get(RESTAURANT_A_ID), updated);
    }
}