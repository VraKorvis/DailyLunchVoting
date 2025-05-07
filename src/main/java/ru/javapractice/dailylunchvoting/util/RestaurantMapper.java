package ru.javapractice.dailylunchvoting.util;

import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.restaurant.model.Menu;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantWithMenuTo;

import java.time.LocalDate;
import java.util.Optional;

public class RestaurantMapper {

    public static RestaurantWithMenuTo toTo(Restaurant restaurant) {

        Menu todaysMenu = getTodaysMenu(restaurant)
                .orElseThrow(() -> new NotFoundException("No menu found for today for restaurant with ID "
                        + restaurant.getId() + " (" + restaurant.getName() + ")"));

        return new RestaurantWithMenuTo(
                restaurant.getId(),
                restaurant.getName(),
                MenuMapper.toTo(todaysMenu)
        );

    }

    private static Optional<Menu> getTodaysMenu(Restaurant restaurant) {
        return restaurant.getMenus().stream()
                .filter(menu -> menu.getMenuDate().equals(LocalDate.now()))
                .findFirst();
    }

}
