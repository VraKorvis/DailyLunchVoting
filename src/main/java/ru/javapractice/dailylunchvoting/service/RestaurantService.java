package ru.javapractice.dailylunchvoting.service;

import org.springframework.stereotype.Service;
import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.repository.RestaurantRepository;

import java.util.List;

import static ru.javapractice.dailylunchvoting.util.ValidationUtil.checkNotFound;

@Service
public class RestaurantService {

    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public Restaurant create(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public void delete(int id) {
        throw new UnsupportedOperationException("Deletion is not allowed. All data is stored in the database as history.");
    }

    public Restaurant get(int id) {
        return checkNotFound(repository.get(id), id);
    }

    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    public void update(Restaurant restaurant) {
        checkNotFound(repository.save(restaurant), restaurant.id());
    }
}
