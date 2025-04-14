package ru.javapractice.dailylunchvoting.util;

import ru.javapractice.dailylunchvoting.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.model.Menu;
import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.to.RestaurantWithMenuTo;

import java.time.LocalDate;
import java.util.Optional;

public class RestaurantUtil {

    public static RestaurantWithMenuTo toTo(Restaurant restaurant) {

        Menu todaysMenu = getTodaysMenu(restaurant)
                .orElseThrow(() -> new NotFoundException("No menu found for today for restaurant with ID "
                        + restaurant.getId() + " (" + restaurant.getName() + ")"));

        return new RestaurantWithMenuTo(
                restaurant.getId(),
                restaurant.getName(),
                MenuUtil.toTo(todaysMenu)
        );

    }

    private static Optional<Menu> getTodaysMenu(Restaurant restaurant) {
        return restaurant.getMenus().stream()
                .filter(menu -> menu.getMenuDate().equals(LocalDate.now()))
                .findFirst();
    }

}
