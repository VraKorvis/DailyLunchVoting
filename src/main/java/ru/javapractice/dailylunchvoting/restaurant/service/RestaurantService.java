package ru.javapractice.dailylunchvoting.restaurant.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.restaurant.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantWithMenuTo;
import ru.javapractice.dailylunchvoting.util.RestaurantMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final RestaurantRepository repository;

    public RestaurantService(RestaurantRepository repository) {
        this.repository = repository;
    }

    public Restaurant create(Restaurant restaurant) {
        return repository.save(restaurant);
    }

    public Restaurant get(int id) {
        return repository.getExisted(id);
    }

    public RestaurantWithMenuTo getWithMenuForToday(int id) {
        return RestaurantMapper.toTo(repository.findByIdWithMenuForToday(id).orElseThrow(() -> new NotFoundException("Restaurant with id=" + id + " not found")));
    }

    public List<Restaurant> getAll() {
        return repository.findAll(SORT_NAME);
    }

    public void update(Restaurant restaurant) {
        repository.save(restaurant);
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
