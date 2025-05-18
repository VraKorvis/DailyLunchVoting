package ru.javapractice.dailylunchvoting.restaurant.service;

import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.javapractice.dailylunchvoting.app.config.CacheKeys;
import ru.javapractice.dailylunchvoting.app.config.CacheNames;
import ru.javapractice.dailylunchvoting.mapper.RestaurantMapper;
import ru.javapractice.dailylunchvoting.mapper.RestaurantMapperService;
import ru.javapractice.dailylunchvoting.restaurant.model.Restaurant;
import ru.javapractice.dailylunchvoting.restaurant.repository.RestaurantRepository;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantTo;
import ru.javapractice.dailylunchvoting.restaurant.to.RestaurantWithAssignedMenuTo;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantService {

    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final RestaurantMapperService restaurantMapperService;
    private final RestaurantMapper restaurantMapper;
    private final RestaurantRepository repository;

    @CacheEvict(value = {
            CacheNames.RESTAURANT_LIST,
            CacheNames.RESTAURANTS_WITH_TODAY_MENU,
            CacheNames.RESTAURANT
    },
            allEntries = true)
    public Restaurant create(RestaurantTo restaurantDto) {
        return repository.save(new Restaurant(restaurantDto.getName()));
    }

    @CacheEvict(value = {
            CacheNames.RESTAURANT_LIST,
            CacheNames.RESTAURANTS_WITH_TODAY_MENU,
            CacheNames.RESTAURANT
    },
            allEntries = true)
    public void update(Restaurant restaurant) {
        repository.save(restaurant);
    }

    @Cacheable(value = CacheNames.RESTAURANT, key = CacheKeys.ID)
    public RestaurantTo get(int id) {
        return restaurantMapper.toRestaurantTo(repository.getExisted(id));
    }

    @Cacheable(CacheNames.RESTAURANT_LIST)
    public List<RestaurantTo> getAll() {
        return restaurantMapper.toRestaurantTos(repository.findAll(SORT_NAME));
    }

    @Cacheable(CacheNames.RESTAURANTS_WITH_TODAY_MENU)
    public List<RestaurantWithAssignedMenuTo> findAllWithAssignedMenuForToday() {
        return findAllWithAssignedMenuForDate(LocalDate.now());
    }

    public List<RestaurantWithAssignedMenuTo> findAllWithAssignedMenuForDate(LocalDate date) {
        return restaurantMapperService.toWithAssignedMenuTos(repository.findAllWithAssignedMenuForDate(date));
    }

    public List<RestaurantWithAssignedMenuTo> findAllWithoutAssignedMenuForToday() {
        return restaurantMapperService.toWithAssignedMenuTos(repository.findAllWithoutAssignedMenuForDate(LocalDate.now()));
    }

}
