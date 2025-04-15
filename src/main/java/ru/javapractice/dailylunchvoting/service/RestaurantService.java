package ru.javapractice.dailylunchvoting.service;

import org.springframework.stereotype.Service;
import ru.javapractice.dailylunchvoting.model.Restaurant;
import ru.javapractice.dailylunchvoting.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.to.RestaurantWithMenuTo;
import ru.javapractice.dailylunchvoting.util.RestaurantMapper;

import java.util.List;
import java.util.stream.Collectors;

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

    public Restaurant get(int id) {
        return checkNotFound(repository.get(id), id);
    }

    public RestaurantWithMenuTo getWithMenuForToday(int id) {
        return RestaurantMapper.toTo(checkNotFound(repository.findByIdWithMenuForToday(id), id));
    }

    public List<Restaurant> getAll() {
        return repository.getAll();
    }

    public void update(Restaurant restaurant) {
        checkNotFound(repository.save(restaurant), restaurant.id());
    }

    public List<RestaurantWithMenuTo> getAllWithMenuForToday() {
        return repository.getAllWithMenuForToday().stream()
                .map(RestaurantMapper::toTo).collect(Collectors.toList());
    }

    public List<RestaurantWithMenuTo> getAllWithAssignedMenuForToday() {
        return repository.getAllWithAssignedMenuForToday().stream()
                .map(RestaurantMapper::toTo).collect(Collectors.toList());
    }

    public List<RestaurantWithMenuTo> getAllWithoutAssignedMenuForToday() {
        return repository.getAllWithoutAssignedMenuForToday().stream()
                .map(RestaurantMapper::toTo).collect(Collectors.toList());
    }
}
