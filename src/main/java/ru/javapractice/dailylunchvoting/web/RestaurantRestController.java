package ru.javapractice.dailylunchvoting.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.service.MenuService;

import java.util.List;

public class RestaurantRestController {
    private final Logger log = LoggerFactory.getLogger(RestaurantRestController.class);

    private MenuService menuService;

    public List<Restaurant> getAll() {
        log.info("getAll()");
        return null;
    }

    public Restaurant get(int id) {
        log.info("get({})", id);
        return null;
    }

    public Restaurant create(Restaurant restaurant) {
        log.info("create({})", restaurant);
        return null;
    }

    public void delete(int id) {
        log.info("delete({})", id);
    }

    public void update(Restaurant restaurant) {
        log.info("update({})", restaurant);
    }

}
