package ru.javapractice.dailylunchvoting.restaurant.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.javapractice.dailylunchvoting.common.exception.NotFoundException;
import ru.javapractice.dailylunchvoting.mapper.RestaurantMapper;
import ru.javapractice.dailylunchvoting.mapper.RestaurantMapperService;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.restaurant.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantTo;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantWithAssignedMenuTo;

import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantService {

    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final RestaurantMapperService restaurantMapperService;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantRepository repository;

    public Restaurant create(RestaurantTo restaurantDto) {
        return repository.save(new Restaurant(restaurantDto.getName()));
    }

    public void update(Restaurant restaurant) {
        repository.save(restaurant);
    }

    public RestaurantTo get(int id) {
        return restaurantMapper.toRestaurantTo(repository.getExisted(id));
    }

    public List<RestaurantTo> getAll() {
        return restaurantMapper.toRestaurantTos(repository.findAll(SORT_NAME));
    }

    public List<RestaurantWithAssignedMenuTo> getAllWithAssignedMenuForToday() {
        return restaurantMapperService.toWithAssignedMenuTos(repository.findAllWithAssignedMenuForToday());
    }

    public List<RestaurantWithAssignedMenuTo> getAllWithoutAssignedMenuForToday() {
        return restaurantMapperService.toWithAssignedMenuTos(repository.findAllWithoutAssignedMenuForToday());
    }

}
