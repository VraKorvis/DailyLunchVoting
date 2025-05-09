package ru.javapractice.dailylunchvoting.restaurant.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.mapper.RestaurantMapperService;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.restaurant.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantTo;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantWithAssignedMenuTo;

import java.util.List;

@AllArgsConstructor
@Service
public class RestaurantService {

    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final RestaurantMapperService mapperService;
    private final RestaurantRepository repository;

    public Restaurant create(RestaurantTo restaurantDto) {
        return repository.save(new Restaurant(restaurantDto.getName()));
    }

    public void update(Restaurant restaurant) {
        repository.save(restaurant);
    }

    public Restaurant get(int id) {
        return repository.getExisted(id);
    }

    public List<Restaurant> getAll() {
        return repository.findAll(SORT_NAME);
    }

    public RestaurantWithAssignedMenuTo getWithMenuForToday(int id) {
        Restaurant restaurant = repository.findByIdWithMenuForToday(id)
                .orElseThrow(() -> new NotFoundException("Restaurant with id=" + id + " not found"));
        return mapperService.toWithMenuTo(restaurant);
    }

    public List<RestaurantWithAssignedMenuTo> getAllWithAssignedMenuForToday() {
        List<Restaurant> allWithAssignedMenuForToday = repository.getAllWithAssignedMenuForToday();
        return mapperService.toWithMenuToList(allWithAssignedMenuForToday);
    }

    public List<RestaurantWithAssignedMenuTo> getAllWithoutAssignedMenuForToday() {
        List<Restaurant> allWithoutAssignedMenuForToday = repository.getAllWithoutAssignedMenuForToday();
        return mapperService.toWithMenuToList(allWithoutAssignedMenuForToday);
    }

}
