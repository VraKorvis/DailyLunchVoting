package ru.javapractice.dailylunchvoting.service;

import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.repository.datajpa.RestaurantRepository;

import java.util.List;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.checkNotFound;

public class RestaurantService {
    private RestaurantRepository repository;

    public Restaurant create(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public void delete(int id) {
        checkNotFound(repository.delete(id), id);
    }

    public Restaurant get(int id) {
        return checkNotFound(repository.get(id), id);
    }

    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    public void update(Restaurant restaurant) {
        checkNotFound(repository.save(restaurant), restaurant.getId());
    }
}
