package ru.javapractice.dailylunchvoting.repository.datajpa;

import ru.javapractice.dailylunchvoting.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    Restaurant get(String id);
    List<Restaurant> getAll();
    Restaurant save(Restaurant restaurant);
    boolean delete(Integer id);

}
