package ru.javapractice.dailylunchvoting.restaurant.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javapractice.dailylunchvoting.AbstractIntegrationServiceTest;
import ru.javapractice.dailylunchvoting.mapper.RestaurantMapper;
import ru.javapractice.dailylunchvoting.mapper.RestaurantMapperService;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantTo;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantWithAssignedMenuTo;

import java.util.List;
import java.util.stream.Stream;

import static ru.javapractice.dailylunchvoting.restaurant.RestaurantMenuData.*;

public class RestaurantServiceTest extends AbstractIntegrationServiceTest {

    @Autowired
    private RestaurantMapperService restaurantMapperService;
    @Autowired
    private RestaurantMapper restaurantMapper;

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
        MATCHER_TO.assertMatch(restaurant, restaurantMapper.toRestaurantTo(RESTAURANT_A));
    }

    @Test
    public void getAll() {
        var restaurants = service.getAll();
        var rTos = restaurantMapper.toRestaurantTos(List.of(RESTAURANT_A, RESTAURANT_B, RESTAURANT_C));
        MATCHER_TO.assertMatch(restaurants, rTos);
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
        MATCHER_TO.assertMatch(service.get(RESTAURANT_A_ID), restaurantMapper.toRestaurantTo(updated));
    }
}