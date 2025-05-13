package ru.javapractice.dailylunchvoting.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.javapractice.dailylunchvoting.restaurant.RestaurantMenuData;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RestaurantMapperTest {

    @Autowired
    private RestaurantMapper restaurantMapper;

    @Test
    public void shouldMapRestaurantToRestaurantTo() {
        var restaurant = RestaurantMenuData.RESTAURANT_A;

        var restaurantTo = restaurantMapper.toRestaurantTo(restaurant);

        assertEquals(restaurant.getId(), restaurantTo.getId());
        assertEquals(restaurant.getName(), restaurantTo.getName());
    }
}