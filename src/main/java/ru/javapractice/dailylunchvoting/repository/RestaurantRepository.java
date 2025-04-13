package ru.javapractice.dailylunchvoting.repository;

import ru.javapractice.dailylunchvoting.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    Restaurant get(Integer id);
    Restaurant getReferenceById(Integer id);
    List<Restaurant> getAll();
    List<Restaurant> getAllRestaurantsWithMenuForToday();
    Restaurant save(Restaurant restaurant);
    boolean delete(Integer id);

    Restaurant getWithMenuForToday(Integer id);
}
