package ru.javapractice.dailylunchvoting.repository;

import ru.javapractice.dailylunchvoting.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    Restaurant get(Integer id);
    Restaurant getReferenceById(Integer id);
    List<Restaurant> getAll();
    List<Restaurant> getAllWithMenuForToday();
    Restaurant save(Restaurant restaurant);

    Restaurant findByIdWithMenuForToday(Integer id);
}
